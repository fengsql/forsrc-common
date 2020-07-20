package com.forsrc.common.spring.bean.dto;

import lombok.Data;

@Data
public class TokenInfo {
    String phoneNumber;
    Integer userId;
    Long validTime;
    String pad;

}
