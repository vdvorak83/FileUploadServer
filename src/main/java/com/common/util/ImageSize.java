package com.common.util;

/**
 * Created by Kirill Stoianov on 20/09/17.
 */
public enum ImageSize {


    SMALL {
        @Override
        public String toString() {
            return SMALL_VALUE;
        }
    },
    MEDIUM {
        @Override
        public String toString() {
            return MEDIUM_VALUE;
        }
    },

    BIG {
        @Override
        public String toString() {
            return BIG_VALUE;
        }
    };

    public static final String SMALL_VALUE = "small";
    private static final String MEDIUM_VALUE = "medium";
    private static final String BIG_VALUE = "big";

    public static ImageSize fromString(String value) {
        if (value == null) return null;
        switch (value.toLowerCase()) {
            case SMALL_VALUE:
                return SMALL;
            case MEDIUM_VALUE:
                return MEDIUM;
            case BIG_VALUE:
                return BIG;
            default:
                return null;
        }
    }

}
