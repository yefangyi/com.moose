package com.moose.common.support.cache;

import java.util.function.Function;

/**
 * Created by apple on 2018/9/23.
 */
public interface InitBuildable<T, K, V> extends Builable<T> {

    <K1 extends K, V1 extends V> T build(Function<? super K1, V1> apply);

}
