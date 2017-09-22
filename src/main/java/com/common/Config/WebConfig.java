package com.common.Config;

import com.common.controller.util.convertor.MyCustomEnumConverter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * Created by Kirill Stoianov on 21/09/17.
 */
@Configuration
public class WebConfig  extends WebMvcConfigurationSupport {
    @Override
    public FormattingConversionService mvcConversionService() {
        FormattingConversionService f = super.mvcConversionService();
        f.addConverter(new MyCustomEnumConverter());
        return f;
    }
}