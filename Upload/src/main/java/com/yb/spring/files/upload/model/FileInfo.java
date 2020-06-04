package com.yb.spring.files.upload.model;

import lombok.Data;

import javax.persistence.*;
import java.io.File;


@Entity
@Data
public class FileInfo {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column
  private Long id;

  @Column
  private String name;

  @Column
  private String user;

  @Column
  private String userkey;

public FileInfo(String name){
  this.name = name;
}
  public FileInfo(String name, String user, String userkey) {
    this.name = name;
    this.user = user;
    this.userkey = userkey;
  }



}
