package com.moose.common.util;


import com.moose.common.encrypt.Base64;
import org.springframework.lang.UsesJava8;
import org.springframework.util.ObjectUtils;

import javax.annotation.Nullable;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class StringUtils extends org.springframework.util.StringUtils {

    private static final Charset UTF_8 = StandardCharsets.UTF_8;

    public static String base64Encode(@Nullable String str) {
        return hasLength(str) ? Base64.encodeToString(str.getBytes(UTF_8)) : str;
    }

    public static String base64Decode(@Nullable String str) {
        return hasLength(str) ? new String(Base64.decodeFromString(str), UTF_8) : str;
    }

    public static boolean isNotEmpty(@Nullable String str) {
        return !isEmpty(str);
    }

    @UsesJava8
    public static String join(@Nullable String separator, @Nullable String... strings) {
        if (ObjectUtils.isEmpty(strings)) {
            return null;
        }
        return Stream.of(strings)
                .filter(StringUtils::isNotEmpty)
                .collect(Collectors.joining(separator));
    }

    @UsesJava8
    public static boolean isAnyEmpty(@Nullable String... strArr) {
        if(ObjectUtils.isEmpty(strArr)) {
            return true;
        }
        return Arrays.asList(strArr).stream().anyMatch(StringUtils::isEmpty);
    }
}
