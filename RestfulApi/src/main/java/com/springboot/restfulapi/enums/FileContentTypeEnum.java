package com.springboot.restfulapi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 根据选择的出行方式推荐速度
 */
@Getter
@AllArgsConstructor
public enum FileContentTypeEnum {

    /**
     * 1	图片
     * 2	文档
     * 3	视频
     * 4	音乐
     */
    IMAGE(1, "image"),

    FILE(2, "file"),

    AUDIO_VIDEO(3, "audiovideo"),

    AUDIO(4, "audio"),

    TETX(5, "text");

    private final Integer code;

    private final String type;

    public static String getValue(Integer key) {
        for (FileContentTypeEnum ele : values()) {
            if (ele.getCode().equals(key)) {
                return ele.getType();
            }
        }
        return null;
    }
}
