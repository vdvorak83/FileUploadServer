package com.common.service.impl;

import com.common.domain.FileInfo;
import com.common.persistence.FileInfoRepository;
import com.common.service.GenericEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileInfoService implements GenericEntityService<FileInfo,String>{

    @Autowired
    private FileInfoRepository repository;

    @Override
    public FileInfo save(FileInfo newInstance) {
        return this.repository.save(newInstance);
    }

    @Override
    public void delete(String primaryKey) {
        this.repository.delete(primaryKey);
    }

    @Override
    public void deleteAll() {
        this.repository.deleteAll();
    }

    @Override
    public void update(FileInfo persistentInstance) {
        this.repository.save(persistentInstance);
    }

    @Override
    public FileInfo find(String primaryKey) {
        return this.repository.findOne(primaryKey);
    }

    @Override
    public List<FileInfo> findAll() {
        return this.repository.findAll();
    }

    @Override
    public boolean exists(String primaryKey) {
        return this.repository.exists(primaryKey);
    }
}
