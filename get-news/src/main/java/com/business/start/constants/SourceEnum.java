package com.business.start.constants;

import lombok.Getter;

@Getter
public enum SourceEnum {

    JINRITOUTIAO(1,"今日头条"),
    QINBAOSHAN(2,"秦宝山提供");

    private Integer code ;
    private String message ;

    SourceEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
