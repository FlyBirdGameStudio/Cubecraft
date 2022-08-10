package com.sunrisestudio.util.container;

public interface KeyGetter<K extends KeyHashCodeGetter> {
    K getKey();
}
