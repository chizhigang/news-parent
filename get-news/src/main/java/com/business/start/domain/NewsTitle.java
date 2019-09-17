package com.business.start.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewsTitle {

    private String groupId; // 消息id
    private String title;  // 标题
    private String category; // 新闻类别
    private String source; // 来源
    private String dataSource; // 数据来源
    private String content; // 消息内容
    private Long releasTeime; // 消息时间（非新增时间） 排序已此时间为主
}
