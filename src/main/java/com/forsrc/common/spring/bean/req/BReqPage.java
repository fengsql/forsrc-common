package com.forsrc.common.spring.bean.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "查询的基础请求参数", description = "查询的基础和分页请求参数")
@Data
public class BReqPage {

  @ApiModelProperty(value = "每页记录数，0 (或不传)取默认值，-1 查询所有", name = "pageSize", dataType = "Integer", required = false)
  private Integer pageSize;

  @ApiModelProperty(value = "分页索引号，0 (或不传)为第一页，当为 0 (或不传)时将查询总记录数，其它值不查询总记录数", name = "pageIndex", dataType = "Integer", required = false)
  private Integer pageIndex;

  @ApiModelProperty(value = "排序字段，默认为主键字段", name = "orderBy", dataType = "Integer", required = false)
  private String orderBy;

  @ApiModelProperty(value = "排序方式，true 降序排列，默认为降序排列", name = "desc", dataType = "Integer", required = false)
  private Boolean desc;

  @ApiModelProperty(value = "开始记录位置，不可设置，根据 pageSize * pageIndex 获得", name = "startIndex", dataType = "Integer", required = false, hidden = true)
  private transient Integer startIndex;

  @ApiModelProperty(value = "限制返回总记录数，小于等于 0 不限制，仅查询总记录数时用到", name = "rownum", dataType = "Integer", required = false, hidden = true)
  private Integer rownum;

}
