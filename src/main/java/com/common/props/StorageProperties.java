package com.common.props;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by Kirill Stoianov on 19/09/17.
 */
@ConfigurationProperties("storage")
public class StorageProperties {

    /**
     * Folder location for storing files
     */
    private  String location = "upload-dir";
    private final String tmp = location+"/tmp";
    private final String smallDir = location + "/small"; //400x300
    private final String mediumDir = location + "/medium"; //900x600
    private final String bigDir = location + "/big"; //900x600

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSmallDir() {
        return smallDir;
    }

    public String getMediumDir() {
        return mediumDir;
    }

    public String getBigDir() {
        return bigDir;
    }

    public String getTmpDir() {
        return tmp;
    }
}

