package com.yb.spring.files.upload.controller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import com.yb.spring.files.upload.message.ResponseMessage;
import com.yb.spring.files.upload.model.FileInfo;
import com.yb.spring.files.upload.service.FilesStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

@Controller
@CrossOrigin("http://localhost:3000")
public class FilesController {

    @Autowired
    FilesStorageService storageService;

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(@RequestParam("user") String user) {
        storageService.deleteAll(user);

        return new ResponseEntity<String>("deleted..", HttpStatus.OK);
    }


    @PostMapping("/upload")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("user") String user, @RequestParam("userkey") String userkey) {
        String message = "";
        try {
            storageService.save(file, user, userkey);

            message = "Uploaded the file successfully: " + file.getOriginalFilename();

            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }


//  public String getJsonFile(){
//
//    return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
//  }

    @GetMapping("/files")
    public ResponseEntity<List<FileInfo>> getListFiles(@RequestParam("user") String user) throws IOException {
        List<FileInfo> fileInfos = storageService.loadAll(user).map(path -> {
            String filename = path.getFileName().toString();
//      String url = MvcUriComponentsBuilder.fromMethodName(
//              FilesController.class,
//              "getFile",
//              path.getFileName().toString()
//      ).build().toString();
            return new FileInfo(filename);
        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
    }

//  @GetMapping("/files/{filename:.+}")
//  public ResponseEntity<Resource> getFile(@PathVariable String filename) {
//    Resource file = storageService.load(filename);
//    return ResponseEntity.ok()
//        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
//  }
}
