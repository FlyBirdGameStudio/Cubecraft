package io.flybird.cubecraft.register;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.flybird.cubecraft.auth.SessionService;
import io.flybird.cubecraft.client.Cubecraft;
import io.flybird.cubecraft.client.render.model.block.BlockModel;
import io.flybird.cubecraft.client.render.model.block.BlockModelFace;
import io.flybird.cubecraft.client.render.model.block.IBlockModelComponent;
import io.flybird.cubecraft.client.render.model.block.serialize.BlockModelCompDeserializer;
import io.flybird.cubecraft.client.render.model.block.serialize.BlockModelDeserializer;
import io.flybird.cubecraft.client.render.model.block.serialize.BlockModelFaceDeserializer;
import io.flybird.util.network.packet.Packet;
import io.flybird.cubecraft.server.CubecraftServer;
import io.flybird.util.DebugInfoHandler;
import io.flybird.util.container.namespace.NameSpacedConstructingMap;
import io.flybird.util.container.namespace.NameSpacedRegisterMap;

/**
 * simple register entry set...
 */
public class Registry {
    private static DebugInfoHandler debugInfoHandler = new DebugInfoHandler();
    private static final Gson gson = new GsonBuilder().registerTypeAdapter(BlockModel.class, new BlockModelDeserializer()).registerTypeAdapter(BlockModelFace.class, new BlockModelFaceDeserializer()).registerTypeAdapter(IBlockModelComponent.class, new BlockModelCompDeserializer()).create();
    private static Cubecraft client;
    private static CubecraftServer server;
    private static NameSpacedRegisterMap<SessionService, ?> sessionServiceMap = new NameSpacedRegisterMap<>(null);
    private static NameSpacedConstructingMap<Packet> packets= new NameSpacedConstructingMap<>();


    private Registry() {
        throw new RuntimeException("you should not create instance of this!");
    }

    public static void setClient(Cubecraft client) {
        Registry.client = client;
    }

    public static Cubecraft getClient() {
        return client;
    }

    public static NameSpacedConstructingMap<Packet> getPackets() {
        return packets;
    }

    public static void setServer(CubecraftServer server) {
        Registry.server = server;
    }

    public static CubecraftServer getServer() {
        return server;
    }

    public static Gson getJsonReader() {
        return gson;
    }

    public static DebugInfoHandler getDebugInfoHandler() {
        return debugInfoHandler;
    }

    public static NameSpacedRegisterMap<SessionService, ?> getSessionServiceMap() {
        return sessionServiceMap;
    }
}