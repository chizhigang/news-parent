package com.business.start.constants;

import lombok.Getter;

@Getter
public enum NewsTypeEnum {

    REDIAN(1,"热点"),
    KEJI(2,"科技"),
    YULE(3,"娱乐"),
    YOUXI(4,"游戏"),
    TIYU(5,"体育"),
    QICHE(6,"汽车"),
    CAIJING(7,"财经"),
    GAOXIAO(8,"搞笑"),
    JUNSHI(9,"军事"),
    GUOJI(10,"国际"),
    SHISHANG(11,"时尚"),
    LVYOU(12,"旅游"),
    TANSUO(13,"探索"),
    YUER(14,"育儿"),
    YANGSHENG(15,"养生"),
    MEIWEN(16,"美文"),
    LISHI(17,"历史"),
    MEISHI(18,"美食");

    private Integer code ;

    private String message ;

    NewsTypeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
