package io.flybird.util.file.nbt;

import io.flybird.util.file.nbt.tag.NBTTagCompound;

public interface NBTDeserializer<T> {
    T deserialize(NBTTagCompound tag);
}
