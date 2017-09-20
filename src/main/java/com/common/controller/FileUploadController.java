package com.common.controller;

import com.common.domain.FileInfo;
import com.common.exception.StorageFileNotFoundException;
import com.common.service.impl.FileInfoService;
import com.common.service.impl.StorageService;
import com.common.util.ImageSize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Kirill Stoianov on 19/09/17.
 */
@RestController
@RequestMapping(value = "/image-store")
public class FileUploadController {

    @Autowired
    private StorageService storageService;

    @Autowired
    private FileInfoService infoService;


    @RequestMapping(value = "/upload/single", method = RequestMethod.POST)
    public ResponseEntity<List<String>> uploadSingle(@RequestParam("file") MultipartFile file) {
        List<String> ids = new ArrayList<>();

        // TODO: 20/09/17 validate fileName

        storageService.store(file);
        final FileInfo fileInfo = infoService.save(new FileInfo(file.getOriginalFilename(), file.getContentType(), System.currentTimeMillis() + "", file.getSize()));
        ids.add(fileInfo.getId());
        return ResponseEntity.status(200).body(ids);
    }

    @RequestMapping(value = "/upload/multiple", method = RequestMethod.POST)
    public ResponseEntity<List<String>> uploadAll(@RequestParam("file") MultipartFile[] form) {
        List<String> ids = new ArrayList<>();

        Arrays.asList(form).forEach(file -> {
            storageService.store(file);
            final FileInfo fileInfo = infoService.save(new FileInfo(file.getOriginalFilename(), file.getContentType(), System.currentTimeMillis() + "", file.getSize()));
            ids.add(fileInfo.getId());
        });

        return ResponseEntity.status(HttpStatus.OK).body(ids);
    }


    // TODO: 20/09/17   GET /image-storage/imgID/Size;
    @RequestMapping(value = "/{fileId}/{size}", method = RequestMethod.GET)
    public ResponseEntity<Resource> findById(@PathVariable("fileId") String fileId, @PathVariable("size")ImageSize size) {
        final FileInfo fileInfo = this.infoService.find(fileId);
        if (fileInfo == null) {
            return ResponseEntity.badRequest().build();
        }

        final String fileName = fileInfo.getFileName();

        Resource file = storageService.loadAsResource(fileName,size);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }


    @RequestMapping(value = "/delete/{fileId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteById(@PathVariable("fileId") String fileId) {
        final FileInfo fileInfo = this.infoService.find(fileId);
        if (fileInfo != null) {
            this.infoService.delete(fileId);
            this.storageService.delete(fileInfo.getFileName());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }


    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }
}
