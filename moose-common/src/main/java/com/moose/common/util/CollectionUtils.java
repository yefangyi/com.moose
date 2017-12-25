package com.moose.common.util;

import org.springframework.lang.UsesJava8;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class CollectionUtils {

    @UsesJava8
    public static <T, K> List<K> mapping(Collection<T> source, Function<T, K> apply) {
        Assert.notNull(source);
        Assert.notNull(apply);

        return source.stream().filter(ObjectUtils::isNotEmpty).map(apply).collect(Collectors.toList());
    }

}
