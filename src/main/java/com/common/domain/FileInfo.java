package com.common.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigInteger;

public class FileInfo {

    @Id
    private String id;
    private String fileName;
    private String extension;
    private String date;
    private double size;

    public FileInfo() {
    }

    public FileInfo(String fileName, String extension, String date, double size) {
        this.fileName = fileName;
        this.extension = extension;
        this.date = date;
        this.size = size;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "FileInfo{" +
                "id=" + id +
                ", fileName='" + fileName + '\'' +
                ", extension='" + extension + '\'' +
                ", date='" + date + '\'' +
                ", size=" + size +
                '}';
    }
}
