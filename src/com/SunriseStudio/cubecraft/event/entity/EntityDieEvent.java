package com.sunrisestudio.cubecraft.event.entity;

import com.sunrisestudio.cubecraft.world.entity.Entity;
import org.joml.Vector3d;

public record EntityDieEvent(Entity e, Vector3d diePos){
}
