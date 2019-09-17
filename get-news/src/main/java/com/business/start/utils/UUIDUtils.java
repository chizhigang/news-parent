package com.business.start.utils;

import java.util.UUID;

public class UUIDUtils {

    public static String getUuidFor8() {
        StringBuffer shortBuffer = new StringBuffer();
        String uuid = UUID.randomUUID().toString();
        String[] split = uuid.split("-");
        System.out.println(split[0]);
        return split[0];
    }

    public static String getId() {
        return System.currentTimeMillis() + UUIDUtils.getUuidFor8();
    }

    public static void main(String[] args) {
        getUuidFor8();
    }
}
