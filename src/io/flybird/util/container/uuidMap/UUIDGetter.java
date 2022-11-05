package io.flybird.util.container.uuidMap;

public interface UUIDGetter<T extends UUID<?>> {
    boolean equalsAnother(UUIDGetter<T> uid);

    T getUID();
}
