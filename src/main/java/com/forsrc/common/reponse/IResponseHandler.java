package com.forsrc.common.reponse;

public interface IResponseHandler<T> {

  T createResponse(Boolean success, Integer code, String message, Object data);

}
