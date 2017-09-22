package com.common.persistence;

import com.common.domain.FileInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface FileInfoRepository extends MongoRepository<FileInfo,String> {

}
