package io.flybird.util.file;

import io.flybird.util.file.nbt.NBTTagCompound;

/**
 * simple data io object standard
 * @author GrassBlock2022
 */
public interface NBTDataIO {
    NBTTagCompound getData();
    void setData(NBTTagCompound tag);
}
