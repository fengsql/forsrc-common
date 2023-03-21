package com.forsrc.common.extend.work;

import com.forsrc.common.constant.Code;
import com.forsrc.common.constant.Enum;
import com.forsrc.common.exception.CommonException;
import com.forsrc.common.extend.base.DefaultFieldConvertor;
import com.forsrc.common.extend.base.IFieldConvertor;
import com.forsrc.common.extend.bean.Field;
import com.forsrc.common.extend.bean.ReqExport;
import com.forsrc.common.extend.tool.ToolExport;
import com.forsrc.common.spring.base.BService;
import com.forsrc.common.spring.base.IDao;
import com.forsrc.common.tool.Tool;
import com.forsrc.common.tool.ToolBean;
import com.forsrc.common.tool.ToolJson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class ExportExcel extends BService {

  private static final String dao_prefix_or_suffix = "Dao";
  private static final String param_queryField = "queryField";

  private IFieldConvertor fieldConvertor;

  public ExportExcel() {
    this.fieldConvertor = new DefaultFieldConvertor();
  }

  public ExportExcel(IFieldConvertor fieldConvertor) {
    Assert.notNull(fieldConvertor, "not init 'fieldConvertor'");
    this.fieldConvertor = fieldConvertor;
  }

  //<<---------------------------------------- public ----------------------------------------

  public void work(HttpServletRequest request, HttpServletResponse response, ReqExport reqExport) {
    doWork(request, response, reqExport);
  }

  //>>---------------------------------------- public ----------------------------------------

  //<<---------------------------------------- private ----------------------------------------

  //<<<---------------------------------------- work ----------------------------------------

  private void doWork(HttpServletRequest request, HttpServletResponse response, ReqExport reqExport) {
    //    String param = request.getParameter("param");
    //    log.info("export param: " + param);
    //    if (Tool.isNull(param)) {
    //      throw new CommonException(Code.PARAM_EMPTY.getCode(), "参数'param'为空");
    //    }
    //    ReqExport reqExport = ToolJson.toBean(param, ReqExport.class);
    checkExport(reqExport);
    export(response, reqExport);
  }

  private void export(HttpServletResponse response, ReqExport reqExport) {
    List<Field> fields = reqExport.getFields();
    List<Map<String, Object>> data = getData(reqExport);
    if (data == null) {
      throw new CommonException(Code.ERROR.getCode(), "查询数据为空");
    }
    String table = reqExport.getTable();
    String title = reqExport.getTitle();
    ToolExport.export(response, table, title, fields, data);
  }

  //>>>---------------------------------------- work ----------------------------------------

  //<<<---------------------------------------- getData ----------------------------------------

  private List<Map<String, Object>> getData(ReqExport reqExport) {
    Map map = getCondition(reqExport);
    IDao dao = getDao(reqExport);
    return dao.selectList(map);
  }

  private Map getCondition(ReqExport reqExport) {
    Map map;
    String condition = reqExport.getCondition();
    if (Tool.isNull(condition)) {
      map = new HashMap();
    } else {
      map = ToolJson.toMap(condition);
    }

    List<Field> fields = reqExport.getFields();
    String queryField = getQueryField(fields);
    map.put(param_queryField, queryField);
    return map;
  }

  private IDao getDao(ReqExport reqExport) {
    String tableName = reqExport.getTable();
    String name = Tool.toLowerFirst(dao_prefix_or_suffix) + Tool.toUpperFirst(tableName);
    IDao dao = null;
    try {
      dao = (IDao) ToolBean.getBean(name);
    } catch (Exception e) {
      try {
        name = Tool.toLowerFirst(tableName) + Tool.toUpperFirst(dao_prefix_or_suffix);
        dao = (IDao) ToolBean.getBean(name);
      } catch (Exception e1) {
      }
    }
    if (dao == null) {
      throw new CommonException(Code.ERROR.getCode(), "表名称错误. tableName: " + tableName);
    }
    return dao;
  }

  private String getQueryField(List<Field> fields) {
    StringBuilder stringBuilder = new StringBuilder();
    int index = 0;
    int count = fields.size();
    for (Field field : fields) {
      setExportFieldType(field);
      String name = toQueryField(field);
      stringBuilder.append(name);
      index++;
      if (index < count) {
        stringBuilder.append(",");
      }
    }
    return stringBuilder.toString();
  }

  private void setExportFieldType(Field field) {
    String type = field.getType();
    Enum.ExportFieldType exportFieldType = Enum.ExportFieldType.get(type);
    if (exportFieldType == null) {
      throw new CommonException(Code.PARAM_INVALID.getCode(), "无效数据类型. name: " + field.getName() + ". type: " + type);
    }
    field.setExportFieldType(exportFieldType);
  }

  private String toQueryField(Field field) {
    String name = Tool.toString(field.getName());
    if (Tool.isNull(name)) {
      throw new CommonException(Code.ERROR.getCode(), "字段名称为空. title: " + field.getTitle());
    }
    if (name.indexOf(" ") > 0) {
      throw new CommonException(Code.ERROR.getCode(), "字段名称格式错误. fieldName: " + name);
    }
    Enum.ExportFieldType exportFieldType = field.getExportFieldType();
    switch (exportFieldType) {
      case datetime_:
        name = toQueryField_datetime(name);
        break;
    }
    return name;
  }

  private String toQueryField_datetime(String name) {
    String value = fieldConvertor.formatDatetime(name);
    value += " " + name;
    return value;
  }

  //>>>---------------------------------------- getData ----------------------------------------

  //<<<---------------------------------------- inner ----------------------------------------

  private void checkExport(ReqExport reqExport) {
    if (reqExport == null) {
      throw new CommonException(Code.PARAM_INVALID.getCode(), "参数格式错误");
    }
    if (reqExport.getFields() == null || reqExport.getFields().size() == 0) {
      throw new CommonException(Code.PARAM_INVALID.getCode(), "未指定查询字段");
    }
    if (Tool.isNull(reqExport.getTable())) {
      throw new CommonException(Code.PARAM_INVALID.getCode(), "未指定表名");
    }
  }

  //>>>---------------------------------------- inner ----------------------------------------

  //<<<---------------------------------------- tool ----------------------------------------

  //>>>---------------------------------------- tool ----------------------------------------

  //>>---------------------------------------- private ----------------------------------------

}
