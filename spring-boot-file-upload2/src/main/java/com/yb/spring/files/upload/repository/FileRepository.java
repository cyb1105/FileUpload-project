package com.yb.spring.files.upload.repository;

import com.yb.spring.files.upload.model.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileInfo, Integer> {

}
