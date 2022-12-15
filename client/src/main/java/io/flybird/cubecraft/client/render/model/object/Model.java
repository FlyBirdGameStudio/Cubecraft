package io.flybird.cubecraft.client.render.model.object;

import io.flybird.cubecraft.client.resources.ResourceLocation;

import java.util.List;

public interface Model {
    void initializeModel(List<ResourceLocation> textureList);
}
