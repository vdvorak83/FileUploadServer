package com.common.exception;

import com.common.domain.FileInfo;

/**
 * Created by Kirill Stoianov on 22/09/17.
 */
public class FileInfoNotFoundException extends Exception{
    public FileInfoNotFoundException(String fileId) {
        super(FileInfo.class.getSimpleName() + " with id = " + fileId + " is not found.");
    }
}
