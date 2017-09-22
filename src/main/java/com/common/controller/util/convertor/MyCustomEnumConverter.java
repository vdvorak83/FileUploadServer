package com.common.controller.util.convertor;

import com.common.util.ImageSize;
import org.springframework.core.convert.converter.Converter;

/**
 * Created by Kirill Stoianov on 21/09/17.
 */
public class MyCustomEnumConverter implements Converter<String,ImageSize> {

    @Override
    public ImageSize convert(String s) {
        return ImageSize.fromString(s);
    }
}