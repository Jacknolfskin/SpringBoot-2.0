package com.vector.test.web;

import com.vector.test.entity.TRiver;
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
@RequestMapping(value = "river")
public class RiverController extends GeoJsonServicesImpl<TRiver, Integer> {
    @Autowired
    @Qualifier("sqlManagerFactoryBeanGIS")
    private SQLManager sqlManagerGIS;

    @RequestMapping(value = "save")
    @ResponseBody
    public Integer save(@RequestParam("feature") String feature) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Feature fea = mapper.readValue(feature, Feature.class);
            TRiver river = new TRiver();
            BeanUtils.populate(river, fea.getProperties());
            Geometry geometry = geometryConvert.geometryDeserialize(fea.getGeometry());
            if (geometry != null) {
                PGobject pGobject = new PGobject();
                pGobject.setType("Geometry");
                geometry.setSRID(4326);
                pGobject.setValue(WKBWriter.toHex(new WKBWriter(2, true).write(geometry)));
                river.setShape(pGobject);
            }
            return sqlManagerGIS.insert(TRiver.class, river);
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
