package com.forsrc.common.constant;

public enum Code {
  OK(0, "成功"), //
  ERROR(-1, "错误"), //
  SUCCESS(200, "成功"), //
  FAIL(500, "失败"), //

  // common
  COMMON_EXCEPTION(1000, "出现错误"),  //
  ERROR_EXCEPTION(1001, "出现错误"), //

  // authentication
  AUTHENTICATION_EXCEPTION(1005, "授权错误"), //
  AUTHENTICATION_DENY(1006, "没有权限"), //
  AUTHENTICATION_EMPTY(1007, "没有凭证"), //
  // user
  USER_EXCEPTION(1010, "用户异常"), //
  USER_LOGIN_FAIL(1011, "登录失败"), //
  USER_NOT_LOGIN(1012, "用户未登录"), //
  USER_ACCOUNT_EXPIRED(1013, "账号已过期"), //
  USER_CREDENTIALS_ERROR(1014, "密码错误"), //
  USER_CREDENTIALS_EXPIRED(1015, "密码过期"), //
  USER_ACCOUNT_DISABLE(1016, "账号不可用"), //
  USER_ACCOUNT_LOCKED(1017, "账号被锁定"), //
  USER_ACCOUNT_NOT_EXIST(1018, "账号不存在"), //
  USER_ACCOUNT_ALREADY_EXIST(1019, "账号已存在"), //
  USER_ACCOUNT_USE_BY_OTHERS(1020, "账号下线"), //

  // object
  OBJECT_NULL(1100, "对象为空"), //
  BEAN_NULL(1101, "Bean对象为空"), //
  SETTING_ERROR(1110, "配置错误"), //
  NAME_ERROR(1120, "命名错误"), //
  // param
  PARAM_EXCEPTION(1200, "参数错误"), //
  PARAM_EMPTY(1201, "参数为空"), //
  PARAM_INVALID(1202, "参数无效"), //
  PARAM_TYPE_ERROR(1203, "参数类型错误"), //
  PARAM_LOSE(1204, "缺少参数"), //
  // socket
  SOCKET_EXCEPTION(1300, "socket 错误"), //
  ;

  Integer code;
  String msg;

  Code(Integer code, String msg) {
    this.code = code;
    this.msg = msg;
  }

  public Integer getCode() {
    return code;
  }

  public String getMsg() {
    return msg;
  }
}
