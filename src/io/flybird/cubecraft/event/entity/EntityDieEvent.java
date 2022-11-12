package io.flybird.cubecraft.event.entity;

import io.flybird.cubecraft.world.entity.Entity;
import org.joml.Vector3d;

public record EntityDieEvent(Entity e, Vector3d diePos){
}