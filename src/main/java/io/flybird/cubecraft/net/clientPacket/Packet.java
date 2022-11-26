package io.flybird.cubecraft.net.clientPacket;

import io.flybird.util.event.Event;
import io.netty.buffer.ByteBuf;

import java.lang.reflect.InvocationTargetException;

public interface Packet extends Event {

    void writePacketData(ByteBuf buffer);

    void readPacketData(ByteBuf buffer);

    static Packet createPacket(String id){
        Packet pkt=null;
        try {
            pkt = (Packet) Class.forName(id).getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException |
                 ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return pkt;
    }

}
