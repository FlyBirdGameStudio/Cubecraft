package io.flybird.cubecraft.client.render.model.block;

import io.flybird.cubecraft.client.resources.ResourceLocation;

public record BlockModelFace(String tex, float u0, float u1, float v0, float v1, String color, CullingMethod culling) {
    public String getTexture(){
        return ResourceLocation.blockTexture(tex).format();
    }
}
