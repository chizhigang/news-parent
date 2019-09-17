package com.business.start.domain;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IPBean {

    public static final String TYPE_HTTPS = "HTTPS";
    public static final String TYPE_HTTP = "HTTP";

    private String ip;
    private int port;
    private String type;

}
