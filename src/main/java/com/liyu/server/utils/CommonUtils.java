package com.liyu.server.utils;

import java.util.UUID;

public class CommonUtils {
    public static String UUIDGenerator() {
        UUID uuid = UUID.randomUUID();
        String str = uuid.toString();
        return str.substring(0, 8) + str.substring(9, 13) + str.substring(14, 18) + str.substring(19, 23) + str.substring(24);
    }
}
