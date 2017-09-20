package com.common.persistence;

import com.common.domain.FileInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FileInfoRepository extends MongoRepository<FileInfo,String> {
//    @Override
//    boolean exists(String s);
}
