package io.flybird.cubecraft.server;

import io.flybird.cubecraft.world.entity.humanoid.Player;
import io.flybird.util.container.MultiMap;

import java.net.InetSocketAddress;

public class PlayerTable {
    public final MultiMap<String, Player> uuid2player=new MultiMap<>();
    public final MultiMap<String, InetSocketAddress> uuid2address=new MultiMap<>();
    public final MultiMap<Player,InetSocketAddress> player2address=new MultiMap<>();

    public String getPlayerUUID(Player player){
        return uuid2player.of(player);
    }

    public Player getPlayer(String uuid){
        return uuid2player.get(uuid);
    }

    public InetSocketAddress getAddr(String uuid){
        return this.uuid2address.get(uuid);
    }

    public String getUuid(InetSocketAddress addr){
        return this.uuid2address.of(addr);
    }

    public InetSocketAddress getAddr(Player player){
        return this.player2address.get(player);
    }

    public Player getPlayer(InetSocketAddress addr){
        return this.player2address.of(addr);
    }

    public void add(Player player,String uuid,InetSocketAddress addr){
        this.uuid2player.put(uuid,player);
        this.uuid2address.put(uuid,addr);
        this.player2address.put(player,addr);
    }

    public void remove(String uuid){
        this.player2address.remove(this.uuid2player.get(uuid));
        this.uuid2player.remove(uuid);
        this.uuid2address.remove(uuid);
    }

    public void remove(Player player){
        this.uuid2address.remove(this.uuid2player.of(player));
        this.uuid2player.remove(this.uuid2player.of(player));
        this.player2address.remove(player);
    }

    public void remove(InetSocketAddress addr){
        this.uuid2player.remove(this.uuid2address.of(addr));
        this.uuid2address.remove(this.uuid2address.of(addr));
        this.player2address.remove(this.player2address.of(addr));
    }

    public MultiMap<Player, InetSocketAddress> getPlayer2address() {
        return player2address;
    }

    public MultiMap<String, InetSocketAddress> getUuid2address() {
        return uuid2address;
    }

    public MultiMap<String, Player> getUuid2player() {
        return uuid2player;
    }
}
