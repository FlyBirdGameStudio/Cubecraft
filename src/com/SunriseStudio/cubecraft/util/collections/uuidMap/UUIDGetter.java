package com.SunriseStudio.cubecraft.util.collections.uuidMap;

public interface UUIDGetter<T extends UUID<?>> {
    boolean equalsAnother(UUIDGetter<T> uid);

    T getUID();
}
