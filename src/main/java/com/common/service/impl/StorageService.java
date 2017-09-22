package com.common.service.impl;

import com.common.exception.FileExtensionFormatException;
import com.common.exception.MultipartFileContentTypeException;
import com.common.exception.StorageException;
import com.common.exception.StorageFileNotFoundException;
import com.common.props.StorageProperties;
import com.common.util.ImageHelper;
import com.common.util.ImageSize;
import com.common.util.SupportedImageExtesion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

/**
 * Created by Kirill Stoianov on 19/09/17.
 */
@Service
public class StorageService {

    private final Path rootLocation;
    private final Path smallSizeLocation;
    private final Path mediumSizeLocation;
    private final Path bigSizeLocation;
    private final Path tmpLocation;

    @Autowired
    public StorageService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
        this.smallSizeLocation = Paths.get(properties.getSmallDir());
        this.mediumSizeLocation = Paths.get(properties.getMediumDir());
        this.bigSizeLocation = Paths.get(properties.getBigDir());
        this.tmpLocation = Paths.get(properties.getTmpDir());
    }

    public void store(MultipartFile file) throws FileExtensionFormatException, StorageException, MultipartFileContentTypeException {

        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new StorageException(
                        "Cannot store file with relative path outside current directory "
                                + filename);
            }

            String fileExtension = ImageHelper.getFileExtension(file.getOriginalFilename());


            //throw exception if file extension if unsupported
            if (fileExtension == null){
                throw new MultipartFileContentTypeException(file.getContentType());
            }

            if (SupportedImageExtesion.fromString(fileExtension) == null) {
                throw new FileExtensionFormatException(fileExtension);
            }

            //save file to tmp folder
            Files.copy(file.getInputStream(), this.tmpLocation.resolve(filename), StandardCopyOption.REPLACE_EXISTING);

            //get path to tmp file
            final String path = this.tmpLocation.resolve(filename).toFile().getPath();

            //resize image to destination sizes
            BufferedImage smallImage = null;
            BufferedImage mediumImage = null;
            BufferedImage bigImage = null;

//            smallImage = ImageHelper.resizeImage(ImageIO.read(new File(path)), ImageHelper.SMALL_WIDTH, ImageHelper.SMALL_HEIGHT);
//            mediumImage = ImageHelper.resizeImage(ImageIO.read(new File(path)), ImageHelper.MEDIUM_WIDTH, ImageHelper.MEDIUM_HEIGHT);
//            bigImage = ImageHelper.resizeImage(ImageIO.read(new File(path)), ImageHelper.BIG_WIDTH, ImageHelper.BIG_HEIGHT);

            smallImage = ImageHelper.resizeWithAspectRatio(ImageIO.read(new File(path)), ImageHelper.SMALL_WIDTH, ImageHelper.SMALL_HEIGHT,true);
            mediumImage = ImageHelper.resizeWithAspectRatio(ImageIO.read(new File(path)), ImageHelper.MEDIUM_WIDTH, ImageHelper.MEDIUM_HEIGHT,true);
            bigImage = ImageHelper.resizeWithAspectRatio(ImageIO.read(new File(path)), ImageHelper.BIG_WIDTH, ImageHelper.BIG_HEIGHT,true);


            //save resized images to destination folders
            ImageIO.write(smallImage, fileExtension, new File(this.smallSizeLocation.resolve(filename).toFile().getPath()));
            ImageIO.write(mediumImage, fileExtension, new File(this.mediumSizeLocation.resolve(filename).toFile().getPath()));
            ImageIO.write(bigImage, fileExtension, new File(this.bigSizeLocation.resolve(filename).toFile().getPath()));

            //remove source image  from tmp folder
            new File(path).delete();

        } catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
        }
    }

    public Stream<Path> loadAll() throws StorageException {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(path -> this.rootLocation.relativize(path));
        } catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }

    }

    public Path load(String filename, ImageSize imageSize) {
        switch (imageSize) {
            case SMALL:
                return this.smallSizeLocation.resolve(filename);
            case MEDIUM:
                return this.mediumSizeLocation.resolve(filename);
            case BIG:
                return this.bigSizeLocation.resolve(filename);
            default:
                return null;
        }
    }

    public Resource loadAsResource(String filename, ImageSize size) throws MalformedURLException, StorageFileNotFoundException {

            Path file = this.load(filename, size);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageFileNotFoundException("Could not read file: " + filename);

            }
    }

    public void delete(String fileName) {
        this.delete(fileName, ImageSize.SMALL);
        this.delete(fileName, ImageSize.MEDIUM);
        this.delete(fileName, ImageSize.BIG);
    }

    public void delete(String fileNmae, ImageSize size) {
        this.load(fileNmae, size).toFile().delete();
    }

    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
        FileSystemUtils.deleteRecursively(tmpLocation.toFile());
        FileSystemUtils.deleteRecursively(smallSizeLocation.toFile());
        FileSystemUtils.deleteRecursively(mediumSizeLocation.toFile());
        FileSystemUtils.deleteRecursively(bigSizeLocation.toFile());
    }

    public void init() throws StorageException {
        try {
            Files.createDirectories(rootLocation);
            Files.createDirectories(tmpLocation);
            Files.createDirectories(smallSizeLocation);
            Files.createDirectories(mediumSizeLocation);
            Files.createDirectories(bigSizeLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }
}
