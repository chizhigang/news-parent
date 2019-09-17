package com.business.start.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewsImage {

    private String groupId ;   // 消息id
    private String iamgeName ; // 图片名称
    private String iamgeOldName ; // 图片原来的名称
    private String iamgeSuffix ; // 图片后缀
    private String creatTime ; // 创建时间

}
