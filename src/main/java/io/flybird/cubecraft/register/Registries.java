package io.flybird.cubecraft.register;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.flybird.cubecraft.auth.SessionService;

import io.flybird.cubecraft.client.Cubecraft;
import io.flybird.cubecraft.network.packet.Packet;
import io.flybird.cubecraft.server.CubecraftServer;
import io.flybird.cubecraft.world.IWorld;
import io.flybird.cubecraft.world.IWorldProvider;
import io.flybird.cubecraft.world.biome.BiomeMap;
import io.flybird.cubecraft.world.block.Block;
import io.flybird.cubecraft.world.entity.Entity;
import io.flybird.cubecraft.world.item.Inventory;
import io.flybird.cubecraft.world.item.Item;
import io.flybird.cubecraft.world.worldGen.pipeline.ChunkGeneratorPipeline;
import io.flybird.util.file.XmlReader;
import io.flybird.util.I18nHelper;
import io.flybird.util.DebugInfoHandler;
import io.flybird.util.container.namespace.NameSpacedConstructingMap;
import io.flybird.util.container.namespace.NameSpacedRegisterMap;

/**
 * simple register entry set...
 */
public class Registries {
    public static final DebugInfoHandler DEBUG_INFO = new DebugInfoHandler();
    public static final GsonBuilder GSON_BUILDER=new GsonBuilder();
    public static final XmlReader FAML_READER = new XmlReader();

    //common
    public static final NameSpacedRegisterMap<SessionService, ?> SESSION_SERVICE = new NameSpacedRegisterMap<>(null);
    public static final NameSpacedConstructingMap<Packet> PACKET = new NameSpacedConstructingMap<>();
    public static final I18nHelper I18N =new I18nHelper();

    public static CubecraftServer SERVER;

    //content
    public static final NameSpacedRegisterMap<Block, ?> BLOCK_BEHAVIOR = new NameSpacedRegisterMap<>(null);
    public static final NameSpacedRegisterMap<Block, Block> BLOCK = new NameSpacedRegisterMap<>(BLOCK_BEHAVIOR);
    public static final NameSpacedConstructingMap<Entity> ENTITY = new NameSpacedConstructingMap<>(IWorld.class);
    public static final NameSpacedRegisterMap<ChunkGeneratorPipeline, ?> WORLD_GENERATOR = new NameSpacedRegisterMap<>(null);
    public static final BiomeMap BIOME = new BiomeMap();
    public static final NameSpacedRegisterMap<Item, ?> ITEM = new NameSpacedRegisterMap<>(null);
    public static final NameSpacedRegisterMap<IWorldProvider,?> WORLD_PROVIDER =new NameSpacedRegisterMap<>(null);
    public static final NameSpacedConstructingMap<Inventory> INVENTORY=new NameSpacedConstructingMap<>();


    public static Cubecraft CLIENT;

    private Registries() {
        throw new RuntimeException("you should not create instance of this!");
    }

    public static Gson createJsonReader(){
        return GSON_BUILDER.create();
    }
}