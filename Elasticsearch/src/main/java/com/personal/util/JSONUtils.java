package com.personal.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by sead on 17/9/30.
 */
public class JSONUtils {
    // 定义jackson对象
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static <T> T parseObject(String json, Class clazz) {
        return (T) JSON.parseObject(json, clazz);
    }

    public static <T> T parseArray(String json, Class clazz) {
        return (T) JSON.parseArray(json, clazz);
    }

    public static <T> T parseArrayNoQ(String json, Class clazz) {
        return (T) JSON.parseArray("["+json+"]", clazz);
    }

    /**
     * 输出为空参数
     * @param object
     * @return
     */
    public static String toJSONString(Object object) {
        return JSON.toJSONString(object, filter, SerializerFeature.WriteMapNullValue);
    }

    private static ValueFilter filter = new ValueFilter() {
        public Object process(Object obj, String s, Object v) {
            if (v == null || "null".equals(v)) {
                return "";
            }
            return v;
        }
    };

    public static Object parse(String json) {
        return JSON.parse(json);
    }

    /**
     * 只输出不为空的参数
     * @param object
     * @return
     */
    public static String toJSONStringNonNULL(Object object) {
        return JSONObject.toJSONString(object);
    }

}
