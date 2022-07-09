package com.sunrisestudio.util.container.uuidMap;

public interface UUIDGetter<T extends UUID<?>> {
    boolean equalsAnother(UUIDGetter<T> uid);

    T getUID();
}
