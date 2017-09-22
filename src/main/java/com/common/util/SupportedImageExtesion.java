package com.common.util;

/**
 * Created by Kirill Stoianov on 22/09/17.
 */
public enum SupportedImageExtesion {
    JPEG{
        @Override
        public String toString() {
            return JPEG_VALUE;
        }
    },
    JPG{
        @Override
        public String toString() {
            return JPG_VALUE;
        }
    },
    PNG{
        @Override
        public String toString() {
            return PNG_VALUE;
        }
    };

    public static final String JPEG_VALUE = "jpeg";
    public static final String JPG_VALUE = "jpg";
    public static final String PNG_VALUE = "png";

    public static SupportedImageExtesion fromString(String extension){
        if (extension==null)return null;
        final String s = extension.toLowerCase();
        switch (s){
            case JPEG_VALUE:return JPEG;
            case JPG_VALUE:return JPG;
            case PNG_VALUE:return PNG;
            default:return null;
        }
    }
}
