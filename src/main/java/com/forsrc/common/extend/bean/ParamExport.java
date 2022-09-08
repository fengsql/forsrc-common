package com.forsrc.common.extend.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel(value = "导出数据请求的参数", description = "导出数据请求的参数。")
@Data
public class ParamExport {

  @ApiModelProperty(value = "导出字段，从相应综合查询 selectRelative 接口的 RepObject.ObjectRow 中获取", name = "fields", dataType = "List", required = true)
  private List<Field> fields;

  @ApiModelProperty(value = "请求参数，为相应综合查询的 ReqObject 转换为 json 字符串", name = "reqParam", required = false)
  private String reqParam;

  @ApiModelProperty(value = "标题", name = "title", required = false)
  private String title;

}