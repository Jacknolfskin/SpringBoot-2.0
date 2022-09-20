package com.vector.test.web;


import com.vector.test.entity.TBuild;
import com.vector.test.entity.TPoi;
import com.vector.test.entity.TRiver;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.beetl.sql.core.SQLManager;
import org.beetl.sql.core.SQLReady;
import org.locationtech.jts.geom.Envelope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Controller
@RequestMapping(value = "vectortile")
public class VectorTileControllerImpl extends VectorTileController {
    @Value("${cache.vector-tile-geoserver-path}")
    public String cachePath;

    @Value("${region.split}")
    public String regionSplit;
    @Value("${cache.maxz}")
    private Integer tmaxz;
    @Value("${cache.minz}")
    private Integer tminz;

    public static Map<Integer, Long> info = new ConcurrentHashMap<>();// 总数 原子操作
    public static AtomicInteger successCount = new AtomicInteger();// 总数 原子操作
    public static AtomicInteger zoom = new AtomicInteger();// 总数 原子操作

    private static final ForkJoinPool pool = new ForkJoinPool();

    @Autowired
    @Qualifier("sqlManagerFactoryBeanGIS")
    private SQLManager sqlManagerGIS;

    /**
     * 进来的是XYZ scheme
     *
     * @param layerName
     * @param x
     * @param y
     * @param z
     * @return
     */
    @RequestMapping(value = "vt/{z}/{x}/{y}.mvt", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<InputStreamResource> generateVectorTiles(
            @RequestParam(value = "layerName", defaultValue = "vtdemo") String layerName,
            @RequestParam(value = "CRS") String crs,
            @PathVariable("x") Integer x,
            @PathVariable("y") Integer y,
            @PathVariable("z") Integer z
    ) throws Exception {
        ReadWriteLock lock = new ReentrantReadWriteLock();

        final Lock readLock = lock.readLock();
        final Lock writeLock = lock.writeLock();

        File file = new File(cachePath + File.separator + layerName + File.separator + z + File.separator + x + File.separator + String.format("%d.%s", y, "mvt"));
        if (!file.exists()) {
            //#region 下载内容
            double[] bboxs = new double[]{0, 0, 0, 0};
            if (crs.equalsIgnoreCase("EPSG:4326"))
                bboxs = new GlobalGeodetic("", 256).tileLatLonBounds(x, y, z);
            else if (crs.equalsIgnoreCase("EPSG:3857"))
                bboxs = new GlobalMercator(256).tileLatLonBounds(x, y, z);
            else throw new Exception("不支持的地理坐标系");

            Map<String, List> entityMap = new ConcurrentHashMap<>();
            String sql = "SELECT t.* FROM t_build t  WHERE ST_Intersects (st_setsrid(t.shape,4326),ST_MakeEnvelope(" + bboxs[1] + "," + bboxs[0] + "," + bboxs[3] + "," + bboxs[2] + ",4326))";
            List<TBuild> entityList = sqlManagerGIS.execute(new SQLReady(sql), TBuild.class);
            if (entityList.size() > 0) entityMap.put("t_build", entityList);

            sql = "SELECT t.* FROM t_river t  WHERE ST_Intersects (st_setsrid(t.shape,4326),ST_MakeEnvelope(" + bboxs[1] + "," + bboxs[0] + "," + bboxs[3] + "," + bboxs[2] + ",4326))";
            List<TRiver> tRiverList = sqlManagerGIS.execute(new SQLReady(sql), TRiver.class);
            if (tRiverList.size() > 0) entityMap.put("t_river", tRiverList);

            sql = "SELECT t.* FROM t_poi t  WHERE ST_Intersects (st_setsrid(t.shape,4326),ST_MakeEnvelope(" + bboxs[1] + "," + bboxs[0] + "," + bboxs[3] + "," + bboxs[2] + ",4326))";
            List<TPoi> tPois = sqlManagerGIS.execute(new SQLReady(sql), TPoi.class);
            if (tPois.size() > 0) entityMap.put("t_poi", tPois);

            try {
                if (entityMap.size() <= 0)
                    return downloadFile(readLock, file);

                byte[] res = produceMap(entityMap, bboxs);
                if (res == null || res.length <= 0)
                    return downloadFile(readLock, file);

                try {
                    writeLock.lock();
                    if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
                    FileOutputStream fos = new FileOutputStream(file);
                    fos.write(res, 0, res.length);
                    fos.flush();
                    fos.close();
                    System.out.println("增加：" + file.getAbsolutePath());
                } finally {
                    writeLock.unlock();
                }


            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return downloadFile(readLock, file);
            //endregion
        } else {
            return downloadFile(readLock, file);
        }
    }

    @RequestMapping(value = "buildCache/{layerName}")
    @ResponseBody
    public void buildCach(@PathVariable("layerName") String layerName,
                          @RequestParam(value = "CRS") String crs,
                          @RequestParam(value = "extent", required = false) String extent) {
        String[] str = extent.split(",");
        if (str.length == 4) {
            Double xmin = Double.parseDouble(str[0]);
            Double ymin = Double.parseDouble(str[1]);
            Double xmax = Double.parseDouble(str[2]);
            Double ymax = Double.parseDouble(str[3]);
            Envelope envelope = new Envelope(xmin, xmax, ymin, ymax);
            GlobalMercator mercator = new GlobalMercator(256);
            double[] min = mercator.latLonToMeters(envelope.getMinY(), envelope.getMinX());
            double[] max = mercator.latLonToMeters(envelope.getMaxY(), envelope.getMaxX());

            //#region 计算
            for (int tz = tmaxz; tz > tminz - 1; tz--) {
                int[] tminxy = mercator.metersToTile(min[0], min[1], tz);
                int[] tmaxxy = mercator.metersToTile(max[0], max[1], tz);
                tminxy = new int[]{Math.max(0, tminxy[0]), Math.max(0, tminxy[1])};
                tmaxxy = new int[]{(int) Math.min(Math.pow(2, tz) - 1, tmaxxy[0]), (int) Math.min(Math.pow(2, tz) - 1, tmaxxy[1])};
                info.put(tz, (long) ((tmaxxy[1] - (tminxy[1] - 1)) * (tmaxxy[0] + 1 - tminxy[0])));
                for (int tx = tminxy[0]; tx < tmaxxy[0] + 1; tx++) {
                    pool.execute(new DownloadTask(layerName, crs, tz, tminxy[1], tmaxxy[1], tx));
                }
            }
            //endregion
        }
    }

    @RequestMapping(value = "clearCache/{layerName}", method = RequestMethod.GET)
    @ResponseBody
    public Integer clearCache(@PathVariable("layerName") String layerName,
                              @RequestParam(value = "CRS") String crs,
                              @RequestParam(value = "extent", required = false) String extent) throws Exception {
        if (null == extent) {
            String filePath = cachePath + File.separator + layerName;
            if (new File(filePath).exists()) {
                new Thread(() -> FileUtils.delFolder(filePath)).start();
            }
        } else {
            String[] str = extent.split(",");
            if (str.length == 4) {
                Envelope envelope = new Envelope(Double.parseDouble(str[0]), Double.parseDouble(str[2]), Double.parseDouble(str[1]), Double.parseDouble(str[3]));

                double[] min = new double[0], max = new double[0];
                GlobalMercator mercator = null;
                GlobalGeodetic geodetic = null;

                if (crs.equalsIgnoreCase("EPSG:4326")) {
                    geodetic = new GlobalGeodetic("", 256);
                } else if (crs.equalsIgnoreCase("EPSG:3857")) {
                    mercator = new GlobalMercator(256);
                    min = mercator.latLonToMeters(envelope.getMinY(), envelope.getMinX());
                    max = mercator.latLonToMeters(envelope.getMaxY(), envelope.getMaxX());
                } else throw new Exception("不支持的地理坐标系");


                //#region 计算
                for (int tz = tmaxz; tz > tminz - 1; tz--) {
                    int[] tminxy = new int[0], tmaxxy = new int[0];
                    if ((crs.equalsIgnoreCase("EPSG:3857"))) {
                        tminxy = mercator.metersToTile(min[0], min[1], tz);
                        tmaxxy = mercator.metersToTile(max[0], max[1], tz);
                    } else if (crs.equalsIgnoreCase("EPSG:4326")) {
                        tminxy = geodetic.lonlatToTile(envelope.getMinX(), envelope.getMinY(), tz);
                        tmaxxy = geodetic.lonlatToTile(envelope.getMaxX(), envelope.getMaxY(), tz);
                    }

                    tminxy = new int[]{Math.max(0, tminxy[0]), Math.max(0, tminxy[1])};
                    tmaxxy = new int[]{(int) Math.min(Math.pow(2, tz) - 1, tmaxxy[0]), (int) Math.min(Math.pow(2, tz) - 1, tmaxxy[1])};

                    for (int tx = tminxy[0]; tx < tmaxxy[0] + 1; tx++) {
                        for (int ty = tmaxxy[1]; ty > tminxy[1] - 1; ty--) {

                            File file = new File(cachePath + File.separator + layerName + File.separator + tz + File.separator + tx + File.separator + ty + ".mvt");
                            if (file.exists())
                                file.delete();
                            System.out.println("删除：" + file.getAbsolutePath());
                        }
                    }
                }
                return 1;
                //endregion
            }
        }
        return -1;
    }

    public ResponseEntity<InputStreamResource> downloadFile(Lock readLock, File filePath) {
        if (filePath.exists()) {
            try {
                readLock.lock();
                FileSystemResource file = new FileSystemResource(filePath);

                return ResponseEntity.ok().contentLength(file.contentLength())
                        .contentType(MediaType.parseMediaType(MediaType.APPLICATION_OCTET_STREAM_VALUE))
                        .body(new InputStreamResource(file.getInputStream()));
            } catch (IOException e) {
                return ResponseEntity.noContent().build();
            } finally {
                readLock.unlock();
            }
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    private class DownloadTask extends RecursiveTask<Void> {
        private static final long serialVersionUID = 1L;

        private static final int THRESHOLD = 1000;
        private String layerName;
        private String crs;
        private int tz;
        private int start;
        private int end;
        private int tx;

        public DownloadTask(String layerName, String crs, int tz, int start, int end, int tx) {
            this.layerName = layerName;
            this.crs = crs;
            this.tz = tz;
            this.start = start;
            this.end = end;
            this.tx = tx;
        }

        @Override
        protected Void compute() {
            if (end - (start - 1) <= THRESHOLD) {
                for (int ty = end; ty > start - 1; ty--) {
                    try {
                        generateVectorTiles(layerName, crs, tx, ty, tz);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    successCount.getAndIncrement();
                    zoom = new AtomicInteger(tz);
                }
            }
            int m = (start - 1) + (end - (start - 1)) / 2;
            DownloadTask task1 = new DownloadTask(layerName, crs, tz, start, m, tx);
            DownloadTask task2 = new DownloadTask(layerName, crs, tz, m, end, tx);
            invokeAll(task1, task2);
            return null;
        }
    }
}
