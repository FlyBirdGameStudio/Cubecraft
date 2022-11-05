package io.flybird.util.file.nbt;
import io.flybird.util.file.nbt.tag.*;

import java.io.*;

public abstract class NBTBase
{
    public String key;
    
    public NBTBase() {
        super();
        this.key = null;
    }

    public abstract void writeTagContents(final DataOutput dataOutput) throws IOException;

    public abstract void readTagContents(final DataInput dataInput) throws IOException;
    
    public abstract byte getType();
    
    public String getKey() {
        if (this.key == null) {
            return "";
        }
        return this.key;
    }
    
    public NBTBase setKey(final String string) {
        this.key = string;
        return this;
    }

    public static NBTBase createTagOfType(final byte byte1) {
        switch (byte1) {
            case 0: {
                return new NBTTagEnd();
            }
            case 1: {
                return new NBTTagByte();
            }
            case 2: {
                return new NBTTagShort();
            }
            case 3: {
                return new NBTTagInt();
            }
            case 4: {
                return new NBTTagLong();
            }
            case 5: {
                return new NBTTagFloat();
            }
            case 6: {
                return new NBTTagDouble();
            }
            case 7: {
                return new NBTTagByteArray();
            }
            case 8: {
                return new NBTTagString();
            }
            case 9: {
                return new NBTTagList();
            }
            case 10: {
                return new NBTTagCompound();
            }
            case 11: {
                return new NBTTagIntArray();
            }
            default: {
                return null;
            }
        }
    }
    
    public static String getTagName(final byte byte1) {
        switch (byte1) {
            case 0: {
                return "TAG_End";
            }
            case 1: {
                return "TAG_Byte";
            }
            case 2: {
                return "TAG_Short";
            }
            case 3: {
                return "TAG_Int";
            }
            case 4: {
                return "TAG_Long";
            }
            case 5: {
                return "TAG_Float";
            }
            case 6: {
                return "TAG_Double";
            }
            case 7: {
                return "TAG_Byte_Array";
            }
            case 8: {
                return "TAG_String";
            }
            case 9: {
                return "TAG_List";
            }
            case 10: {
                return "TAG_Compound";
            }
            case 11:{
            	return "TAG_Int_Array";
            }
            default: {
                return "UNKNOWN";
            }
        }
    }
}
