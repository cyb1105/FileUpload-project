package com.yb.spring.files.upload.model;

import lombok.Data;

import javax.persistence.*;


@Entity
@Data
public class FileInfo {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column
  private Long id;

  @Column
  private String name;


  public FileInfo(String name) {
    this.name = name;

  }

}
