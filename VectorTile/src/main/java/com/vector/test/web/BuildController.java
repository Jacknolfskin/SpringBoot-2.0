package com.vector.test.web;

import cn.com.enersun.dgpmicro.service.GeoJsonServicesImpl;
import com.vector.test.entity.TBuild;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;
import org.beetl.sql.core.SQLManager;
import org.geojson.Feature;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.WKBWriter;
import org.postgresql.util.PGobject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

@Controller
@RequestMapping(value = "build")
public class BuildController extends GeoJsonServicesImpl<TBuild, Integer> {
    @Autowired
    @Qualifier("sqlManagerFactoryBeanGIS")
    private SQLManager sqlManagerGIS;

    @RequestMapping(value = "save")
    @ResponseBody
    public Integer save(@RequestParam("feature") String feature) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Feature fea = mapper.readValue(feature, Feature.class);
            TBuild build = new TBuild();
            BeanUtils.populate(build, fea.getProperties());
            Geometry geometry = geometryConvert.geometryDeserialize(fea.getGeometry());
            if (geometry != null) {
                PGobject pGobject = new PGobject();
                pGobject.setType("Geometry");
                geometry.setSRID(4326);
                pGobject.setValue(WKBWriter.toHex(new WKBWriter(2, true).write(geometry)));
                build.setShape(pGobject);
            }
           return sqlManagerGIS.insert(TBuild.class, build);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

}
