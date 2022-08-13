package com.sunrisestudio.cubecraft.client.render.model.object;

import com.sunrisestudio.cubecraft.client.render.model.FaceCullingMethod;
import org.joml.Vector2d;

public record Face(
        Vector2d tc0, Vector2d tc1,
        String texture,
        FaceCullingMethod culling
) { }
