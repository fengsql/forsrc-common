package com.forsrc.common.extend.bean;

import lombok.Data;

import java.util.List;

@Data
public class ReqExport {

  private String table;

  private String title;

  private List<Field> fields;

  private String condition;

}