package com.forsrc.common.extend.bean;

import lombok.Data;

import java.util.List;

@Data
public class Table {

  private int id;
  
  private String name;

  private String title;

  private int tableType;

  private List<Field> fields;

}