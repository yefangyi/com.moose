package com.moose.common.util;

import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public abstract class EntityAccessor {

    public static Map<String, Object> toMap(Object object, Supplier<Map<String, Object>> supplier) {
        Assert.notNull(object, "object is null");
        Assert.notNull(supplier, "supplier is null");

        Map<String, Object> contain = supplier.get();
        ReflectionUtils.doWithFields(object.getClass(), (field) -> {
            if(!Modifier.isStatic(field.getModifiers()) && !Modifier.isFinal(field.getModifiers())) {
                ReflectionUtils.makeAccessible(field);
                contain.put(field.getName(), ReflectionUtils.getField(field, object));
            }
        });
        return contain;
    }

    public static Map<String, Object> toMap(Object object) {
        return toMap(object, HashMap::new);
    }

}
