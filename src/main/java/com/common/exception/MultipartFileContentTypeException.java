package com.common.exception;

/**
 * Created by Kirill Stoianov on 22/09/17.
 */
public class MultipartFileContentTypeException extends Exception{
    public MultipartFileContentTypeException(String contentType) {
        super("Content type [" + contentType +"] of multipart file is wrong. Use next format, for example: [image/png]");
    }
}
