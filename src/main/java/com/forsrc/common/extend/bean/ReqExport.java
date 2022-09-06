package com.forsrc.common.extend.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel(value = "通用导出数据请求的参数", description = "通用导出数据请求的参数。")
@Data
public class ReqExport {

  @ApiModelProperty(value = "表名称", name = "table", required = true)
  private String table;

  @ApiModelProperty(value = "标题", name = "title", required = true)
  private String title;

  @ApiModelProperty(value = "导出字段列表", name = "fields", required = true)
  private List<Field> fields;

  @ApiModelProperty(value = "查询条件", name = "condition", required = false)
  private String condition;

}