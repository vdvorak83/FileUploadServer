package com.common.exception;

/**
 * Created by Kirill Stoianov on 19/09/17.
 */
public class FileExtensionFormatException extends Exception {

    public FileExtensionFormatException(String extensionName) {
        super("Upload files with extension [" +extensionName +"] is unsupported");
    }
}
