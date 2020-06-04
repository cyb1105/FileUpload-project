package com.yb.spring.files.upload.service;

import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FilesStorageService {
//  public void init();

  public void save(MultipartFile file, String user, String userkey);

  public Resource load(String filename);

  public void deleteAll(String user);

  public Stream<Path> loadAll( String user);

}
