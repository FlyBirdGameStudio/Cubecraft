package io.flybird.cubecraft.auth;

import io.flybird.util.file.nbt.tag.NBTTagCompound;
import io.netty.buffer.ByteBuf;

public interface SessionService {
    boolean validSession(Session session);
    NBTTagCompound write(Session session);
    void read(Session session, NBTTagCompound tag);
    String genUUID(Session session);
}
