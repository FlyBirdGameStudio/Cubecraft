package com.flybirdstudio.cubecraft.client.render.model.object;


public class BlockModel implements Model {
    private ModelObject[] obj;

    public BlockModel(ModelObject[] obj) {
        this.obj=obj;
    }

    @Override
    public String getID() {
        return null;
    }

    @Override
    public String getNameSpace() {
        return null;
    }
}
