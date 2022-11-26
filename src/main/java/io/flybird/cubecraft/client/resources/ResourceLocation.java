package io.flybird.cubecraft.client.resources;


public record ResourceLocation(ResourceType type,String namespace,ResourceLocType folder,String relativePos) {


    public String format(){
        return new StringBuilder().append(type.getName()).append(namespace).append(folder.getName()).append(relativePos).toString();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ResourceLocation r){
            return r.format().equals(this.format());
        }else{
            return false;
        }
    }

    enum ResourceLocType{
        BLOCK_TEXTURE("/texture/block/"),
        BLOCK_MODEL("/model/block/"),
        ENTITY_TEXTURE("/texture/entity/"),
        ENTITY_MODEL("/model/entity/"),
        UI_RENDER_CONTROLLER("/model/ui/"),
        UI_TEXTURES("/texture/ui/"),
        UI_SCREEN("/ui/"),
        BLOCK_COLOR_MAP("/misc/colormap/"),

        BLOCK_DATA("/block/"),
        ENTITY_DATA("/entity/"),
        EMPTY("/");

        final String name;

        ResourceLocType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    enum ResourceType{
        RESOURCE("/resource/"),
        DATA("/data/");

        final String name;

        ResourceType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    //block
    public static ResourceLocation blockTexture(String namespace,String relativePos){
        return new ResourceLocation(ResourceType.RESOURCE,namespace,ResourceLocType.BLOCK_TEXTURE,relativePos);
    }

    public static ResourceLocation blockColorMap(String n, String s) {
        return new ResourceLocation(ResourceType.RESOURCE,n,ResourceLocType.BLOCK_COLOR_MAP,s);
    }

    public static ResourceLocation blockColorMap(String all) {
        return blockColorMap(all.split(":")[0],all.split(":")[1]);
    }

    public static ResourceLocation blockTexture(String all){
        return blockTexture(all.split(":")[0],all.split(":")[1]);
    }

    public static ResourceLocation blockModel(String namespace,String relativePos){
        return new ResourceLocation(ResourceType.RESOURCE,namespace,ResourceLocType.BLOCK_MODEL,relativePos);
    }

    public static ResourceLocation blockModel(String all){
        return blockModel(all.split(":")[0],all.split(":")[1]);
    }


    //entity
    public static ResourceLocation entityTexture(String namespace,String relativePos){
        return new ResourceLocation(ResourceType.RESOURCE,namespace,ResourceLocType.ENTITY_TEXTURE,relativePos);
    }

    public static ResourceLocation entityModel(String namespace,String relativePos){
        return new ResourceLocation(ResourceType.RESOURCE,namespace,ResourceLocType.ENTITY_MODEL,relativePos);
    }


    //ui
    public static ResourceLocation uiScreen(String namespace,String relativePos){
        return new ResourceLocation(ResourceType.RESOURCE,namespace,ResourceLocType.UI_SCREEN,relativePos);
    }

    public static ResourceLocation uiRenderController(String namespace,String relativePos){
        return new ResourceLocation(ResourceType.RESOURCE,namespace,ResourceLocType.UI_RENDER_CONTROLLER,relativePos);
    }

    public static ResourceLocation uiTexture(String namespace,String relativePos){
        return new ResourceLocation(ResourceType.RESOURCE,namespace,ResourceLocType.UI_TEXTURES,relativePos);
    }
}

