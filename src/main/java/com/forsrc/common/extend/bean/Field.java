package com.forsrc.common.extend.bean;

import com.forsrc.common.constant.Enum;
import lombok.Data;

@Data
public class Field {

  private String name;
  
  private String title;

  private String type;

  private int length;

  private transient Enum.ExportFieldType exportFieldType;

}