package com.forsrc.common.extend.bean;

import com.forsrc.common.constant.Enum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "字段对象", description = "字段对象。")
@Data
public class Field {

  @ApiModelProperty(value = "字段名称", name = "name", required = true)
  private String name;
  
  @ApiModelProperty(value = "字段标题", name = "title", required = true)
  private String title;

  @ApiModelProperty(value = "字段类型", name = "type", required = false)
  private String type;

  @ApiModelProperty(value = "字段长度", name = "length", required = false)
  private int length;

  @ApiModelProperty(value = "导出字段类型", name = "exportFieldType", required = false)
  private transient Enum.ExportFieldType exportFieldType;

}