package com.example.filedemo.service;

import com.example.filedemo.entity.UploadFile;
import com.example.filedemo.exception.FileStorageException;
import com.example.filedemo.exception.MyFileNotFoundException;
import com.example.filedemo.property.FileStorageProperties;
import com.example.filedemo.repository.FileDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    @Autowired
    public FileDAO fileDAO;

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("업로드 된 파일을 저장할 디렉토리를 만들 수 없습니다..", ex);
        }
    }

    public UploadFile storeFile(MultipartFile file) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // 파일명에 부적절한 문자가 있는지 확인한다.
            if(fileName.contains("..")) {
                throw new FileStorageException("파일명에 부적절한 문자가 포함되어 있습니다." + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            UploadFile uploadFile = new UploadFile(fileName, file.getSize(), file.getContentType());
            fileDAO.save(uploadFile);


            return uploadFile;
        } catch (IOException ex) {
            throw new FileStorageException("파일 업로드 실패 " + fileName + ". 댜시 시도하십시오", ex);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("파일을 찾을 수 없습니다 " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("파일을 찾을 수 없습니다." + fileName, ex);
        }
    }
}
