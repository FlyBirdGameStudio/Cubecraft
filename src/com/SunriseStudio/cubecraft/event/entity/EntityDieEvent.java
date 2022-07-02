package com.SunriseStudio.cubecraft.event.entity;

import com.SunriseStudio.cubecraft.event.Event;
import com.SunriseStudio.cubecraft.world.entity.Entity;
import org.joml.Vector3d;

public record EntityDieEvent(Entity e, Vector3d diePos){
}
