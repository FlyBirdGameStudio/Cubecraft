package io.flybird.util.file.nbt;

import io.flybird.util.file.nbt.tag.NBTTagCompound;

public interface NBTSerializer<T> {
    NBTTagCompound serialize(T object);
}
