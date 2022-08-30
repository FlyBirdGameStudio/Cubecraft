package com.flybirdstudio.cubecraft.event.entity;

import com.flybirdstudio.cubecraft.world.entity.Entity;
import org.joml.Vector3d;

public record EntityDieEvent(Entity e, Vector3d diePos){
}
