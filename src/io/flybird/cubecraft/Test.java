package io.flybird.cubecraft;

import io.flybird.cubecraft.world.chunk.Chunk;
import io.flybird.cubecraft.world.chunk.ChunkPos;
import io.flybird.util.file.nbt.NBTBuilder;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class Test {
    public static void main(String[] args) throws Exception {
        File f=new File("F:/chunk.nbt");
        NBTBuilder.write(new Chunk(null,new ChunkPos(0,1,33)).getData(),new DataOutputStream(new FileOutputStream(f)));
    }
}
