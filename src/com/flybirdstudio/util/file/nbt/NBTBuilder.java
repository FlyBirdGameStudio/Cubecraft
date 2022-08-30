package com.flybirdstudio.util.file.nbt;

import com.flybirdstudio.util.file.nbt.tag.NBTTagCompound;
import com.flybirdstudio.util.file.nbt.tag.NBTTagEnd;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;

public class NBTBuilder {
    private static final HashMap<Type,NBTDeserializer<Object>> deserializers=new HashMap<>();
    private static final HashMap<Type,NBTSerializer<Object>> serializers=new HashMap<>();

    public static void registerTypeAdapter(Type t,NBTSerializer<Object> serializer,NBTDeserializer<Object> deserializer){
        serializers.put(t,serializer);
        deserializers.put(t,deserializer);
    }

    public static <T> NBTTagCompound serialize(Type typeofT, T obj){
        return serializers.get(typeofT).serialize(obj);
    }

    public static <T>void deserialize(Type typeofT,T obj, DataOutput output){
        try {
            write(serializers.get(typeofT).serialize(obj),output);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    public static <T> T deserialize(Type typeofT, NBTTagCompound tag){
        return (T) deserializers.get(typeofT).deserialize(tag);
    }

    public static <T> T deserialize(Type typeofT, DataInput input){
        try {
            return (T) deserializers.get(typeofT).deserialize((NBTTagCompound) read(input));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static NBTBase read(final DataInput dataInput) throws IOException {
        final byte byte1 = dataInput.readByte();
        if (byte1 == 0) {
            return new NBTTagEnd();
        }
        final NBTBase tagOfType = NBTBase.createTagOfType(byte1);
        tagOfType.key = dataInput.readUTF();
        tagOfType.readTagContents(dataInput);
        return tagOfType;
    }

    public static void write(final NBTBase hm, final DataOutput dataOutput) throws IOException {
        dataOutput.writeByte(hm.getType());
        if (hm.getType() == 0) {
            return;
        }
        dataOutput.writeUTF(hm.getKey());
        hm.writeTagContents(dataOutput);
    }
}
