package com.example.filedemo.repository;

import com.example.filedemo.entity.UploadFile;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FileDAO extends JpaRepository<UploadFile, Integer> {
}
