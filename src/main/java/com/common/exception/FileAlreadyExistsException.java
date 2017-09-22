package com.common.exception;

import java.util.List;

/**
 * Created by Kirill Stoianov on 21/09/17.
 */
public class FileAlreadyExistsException extends Exception {
    public FileAlreadyExistsException(String fileName) {
        super("File name [" + fileName + "] has already exists. All name must be unique.");
    }

    public FileAlreadyExistsException(List<String> fileNames) {
        super("Some of files names [" + (fileNames!=null? fileNames.toString() : null)+ "] has already exists. All name must be unique.");
    }
}
