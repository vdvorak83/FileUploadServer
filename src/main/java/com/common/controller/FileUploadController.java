package com.common.controller;

import com.common.domain.FileInfo;
import com.common.exception.*;
import com.common.service.impl.FileInfoService;
import com.common.service.impl.StorageService;
import com.common.util.ImageHelper;
import com.common.util.ImageSize;
import com.common.util.SupportedImageExtesion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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
    public ResponseEntity<List<String>> uploadSingle(@RequestParam("file") MultipartFile file)
            throws FileExtensionFormatException, StorageException, FileAlreadyExistsException, MultipartFileContentTypeException {
        List<String> ids = new ArrayList<>();
        final List<FileInfo> allInfo = this.infoService.findAll();

        //check is file name is not exists
        final boolean anyMatch = allInfo.stream().anyMatch(fileInfo -> {
            return fileInfo.getFileName().equals(file.getOriginalFilename());
        });

        //throw exception
        if (anyMatch) throw new FileAlreadyExistsException(file.getOriginalFilename());

        //throw exception if file extension if unsupported
        //or content type was set not correctly
        String fileExtension = ImageHelper.getFileExtension(file.getOriginalFilename());

        if (fileExtension == null){
            throw new MultipartFileContentTypeException(file.getContentType());
        }

        if (SupportedImageExtesion.fromString(fileExtension) == null) {
            throw new FileExtensionFormatException(fileExtension);
        }


        storageService.store(file);

        final FileInfo fileInfo = infoService.save(new FileInfo(file.getOriginalFilename(), fileExtension, System.currentTimeMillis() + "", file.getSize()));
        ids.add(fileInfo.getId());

        System.out.println(fileInfo.toString());

        return ResponseEntity.status(200).body(ids);
    }

    @RequestMapping(value = "/upload/multiple", method = RequestMethod.POST)
    public ResponseEntity<List<String>> uploadAll(@RequestParam("file") MultipartFile[] data) throws FileAlreadyExistsException, FileExtensionFormatException, StorageException, MultipartFileContentTypeException {
        //result list
        List<String> ids = new ArrayList<>();

        //get all file info
        final List<FileInfo> allInfo = this.infoService.findAll();

        //convert array to list
        final List<MultipartFile> multipartFiles = Arrays.asList(data);

        //check is file name is not exists
        final boolean anyMatch = allInfo.stream()
                .anyMatch(fileInfo -> {
//                    return fileInfo.getFileName().equals(file.getOriginalFilename());
                    return multipartFiles.stream().anyMatch(multipartFile -> {
                        return fileInfo.getFileName().equals(multipartFile.getOriginalFilename());
                    });
                });

        //throw exception
        if (anyMatch) {
            final List<String> collect = multipartFiles.stream().map(multipartFile -> {
                return multipartFile.getOriginalFilename();
            }).collect(Collectors.toList());
            throw new FileAlreadyExistsException(collect);
        }


        //save files and fileInfo
        for (MultipartFile file : multipartFiles) {

            //throw exception if file extension if unsupported
            //or content type was set not correctly
            final String[] split = file.getContentType().split("/");
            String fileExtension = split[1];

            if (split.length!=2){
                throw new MultipartFileContentTypeException(file.getContentType());
            }

            if (SupportedImageExtesion.fromString(fileExtension) == null) {
                throw new FileExtensionFormatException(split[1]);
            }


            //save file
            storageService.store(file);

            //save info
            final FileInfo fileInfo = infoService.save(new FileInfo(file.getOriginalFilename(), split[1], System.currentTimeMillis() + "", file.getSize()));

            //add file id to result list
            ids.add(fileInfo.getId());
        }

        return ResponseEntity.status(HttpStatus.OK).body(ids);
    }


    @RequestMapping(value = "/{fileId}/{size}", method = RequestMethod.GET)
    public ResponseEntity<Resource> findById(@PathVariable("fileId") String fileId, @PathVariable("size") ImageSize size)
            throws StorageFileNotFoundException, MalformedURLException, FileInfoNotFoundException {

        final FileInfo fileInfo = this.infoService.find(fileId);
        if (fileInfo == null) throw new FileInfoNotFoundException(fileId);

        final String fileName = fileInfo.getFileName();

        Resource file = null;

        file = storageService.loadAsResource(fileName, size);

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }


    @RequestMapping(value = "/delete/{fileId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteById(@PathVariable("fileId") String fileId) throws FileInfoNotFoundException {
        final FileInfo fileInfo = this.infoService.find(fileId);
        if (fileInfo == null) throw new FileInfoNotFoundException(fileId);
        this.infoService.delete(fileId);
        this.storageService.delete(fileInfo.getFileName());
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/delete/all", method = RequestMethod.DELETE)
    public ResponseEntity deleteById() throws FileInfoNotFoundException {
        this.infoService.deleteAll();
        this.infoService.deleteAll();
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(FileInfoNotFoundException.class)
    public ResponseEntity<?> handleFileInfoNotFoundException(FileInfoNotFoundException e) {
        return ResponseEntity.badRequest().body(e);
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exc);
    }

    @ExceptionHandler(MalformedURLException.class)
    public ResponseEntity<?> handleMalformedURLException(MalformedURLException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
    }

    @ExceptionHandler(FileExtensionFormatException.class)
    public ResponseEntity<?> handleFileExtensionFormatException(FileExtensionFormatException e) {
        return ResponseEntity.badRequest().body(e);
    }

    @ExceptionHandler(StorageException.class)
    public ResponseEntity<?> handleStorageException(StorageException e) {
        return ResponseEntity.badRequest().body(e);
    }

    @ExceptionHandler(FileAlreadyExistsException.class)
    public ResponseEntity<?> handleFileAlreadyExistsException(FileAlreadyExistsException e) {
        return ResponseEntity.badRequest().body(e);
    }

    @ExceptionHandler(MultipartFileContentTypeException.class)
    public ResponseEntity<?> handleMultipartFileContentTypeException(MultipartFileContentTypeException e) {
        return ResponseEntity.badRequest().body(e);
    }


}
