package com.sunrisestudio.cubecraft.client.render.model.object;

import com.sunrisestudio.grass3d.render.draw.IVertexArrayBuilder;

import org.joml.Matrix4f;
import org.joml.Vector3d;

public record Cube(
        Vector3d v0,Vector3d v1,
        Face top, Face bottom,
        Face left, Face right,
        Face front ,Face back
){}