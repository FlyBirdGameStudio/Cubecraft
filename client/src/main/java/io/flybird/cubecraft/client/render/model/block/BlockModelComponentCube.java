package io.flybird.cubecraft.client.render.model.block;

import io.flybird.cubecraft.client.ClientRegistries;
import io.flybird.cubecraft.client.render.BlockRenderUtil;
import io.flybird.cubecraft.client.render.model.RenderType;
import io.flybird.cubecraft.client.render.model.object.Vertex;
import io.flybird.cubecraft.world.IWorld;
import io.flybird.cubecraft.world.block.BlockState;
import io.flybird.cubecraft.world.block.EnumFacing;
import io.flybird.starfish3d.render.draw.VertexArrayBuilder;
import io.flybird.starfish3d.render.textures.Texture2DTileMap;
import io.flybird.util.ColorUtil;
import org.joml.Vector2d;
import org.joml.Vector3d;

import java.util.Objects;

public class BlockModelComponentCube extends BlockModelComponent {
    private final BlockModelFace top, bottom, left, right, front, back;

    public BlockModelComponentCube(RenderType renderType, Vector3d start, Vector3d end, BlockModelFace top1, BlockModelFace bottom1, BlockModelFace left1, BlockModelFace right1, BlockModelFace front1, BlockModelFace back1) {
        super(renderType, start, end);
        this.top = top1;
        this.bottom = bottom1;
        this.left = left1;
        this.right = right1;
        this.front = front1;
        this.back = back1;
    }

    @Override
    public void render(VertexArrayBuilder builder, RenderType currentType, IWorld world, BlockState bs, long x, long y, long z, double renderX, double renderY, double renderZ) {
        if (currentType == this.type) {
            if (this.shouldRender(0, bs, world, x, y, z)) {
                this.renderFace(top, 0, builder, world, bs, x, y, z, renderX, renderY, renderZ);
            }
            if (this.shouldRender(1, bs, world, x, y, z)) {
                this.renderFace(bottom, 1, builder, world, bs, x, y, z, renderX, renderY, renderZ);
            }
            if (this.shouldRender(2, bs, world, x, y, z)) {
                this.renderFace(left, 2, builder, world, bs, x, y, z, renderX, renderY, renderZ);
            }
            if (this.shouldRender(3, bs, world, x, y, z)) {
                this.renderFace(right, 3, builder, world, bs, x, y, z, renderX, renderY, renderZ);
            }
            if (this.shouldRender(4, bs, world, x, y, z)) {
                this.renderFace(front, 4, builder, world, bs, x, y, z, renderX, renderY, renderZ);
            }
            if (this.shouldRender(5, bs, world, x, y, z)) {
                this.renderFace(back, 5, builder, world, bs, x, y, z, renderX, renderY, renderZ);
            }
        }
    }

    public boolean shouldRender(int current, BlockState bs, IWorld world, long x, long y, long z) {
        BlockModelFace face = switch (current) {
            case 0 -> right;
            case 1 -> left;
            case 2 -> front;
            case 3 -> back;
            case 4 -> bottom;
            case 5 -> top;
            default -> throw new IllegalStateException("Unexpected value: " + current);
        };
        return switch (face.culling()) {
            case NEVER -> false;
            case SOLID -> !world.getBlockState(EnumFacing.findNear(x, y, z, 1, EnumFacing.clip(bs.getFacing().getNumID(),current))).getBlock().isSolid();
            case ALWAYS -> true;
            case EQUALS -> !(Objects.equals(world.getBlockState(EnumFacing.findNear(x, y, z, 1, EnumFacing.clip(bs.getFacing().getNumID(),current))).getId(), bs.getId()));
        };
    }

    public int getFaceColor(IWorld world, BlockState bs, long x, long y, long z, int face) {
        return 0xFFFFFF;
    }


    public void renderFace(BlockModelFace f, int face, VertexArrayBuilder builder, IWorld w, BlockState bs, long x, long y, long z, double renderX, double renderY, double renderZ) {
        Texture2DTileMap terrain = ClientRegistries.TEXTURE.getTexture2DTileMapContainer().get("cubecraft:terrain");

        float u0 = terrain.exactTextureU(f.getTexture(), f.u0());
        float u1 = terrain.exactTextureU(f.getTexture(), f.u1());
        float v0 = terrain.exactTextureV(f.getTexture(), f.v0());
        float v1 = terrain.exactTextureV(f.getTexture(), f.v1());

        Vector3d v000 = bs.getFacing().clipVec(new Vector3d(this.start.x, this.start.y, this.start.z));
        Vector3d v001 = bs.getFacing().clipVec(new Vector3d(this.start.x, this.start.y, this.end.z));
        Vector3d v010 = bs.getFacing().clipVec(new Vector3d(this.start.x, this.end.y, this.start.z));
        Vector3d v011 = bs.getFacing().clipVec(new Vector3d(this.start.x, this.end.y, this.end.z));
        Vector3d v100 = bs.getFacing().clipVec(new Vector3d(this.end.x, this.start.y, this.start.z));
        Vector3d v101 = bs.getFacing().clipVec(new Vector3d(this.end.x, this.start.y, this.end.z));
        Vector3d v110 = bs.getFacing().clipVec(new Vector3d(this.end.x, this.end.y, this.start.z));
        Vector3d v111 = bs.getFacing().clipVec(new Vector3d(this.end.x, this.end.y, this.end.z));

        Vector3d render = new Vector3d(renderX, renderY, renderZ);
        int c = ClientRegistries.COLOR_MAP.get(f.color()).sample(w,bs,x,y,z);
        if (face == 0) {
            Vector3d faceColor = new Vector3d(ColorUtil.int1ToFloat3(c));
            BlockRenderUtil.bakeVertex(Vertex.create(new Vector3d(v111).add(render), new Vector2d(u1, v1), faceColor), v111, w, x, y, z, 0).draw(builder);
            BlockRenderUtil.bakeVertex(Vertex.create(new Vector3d(v110).add(render), new Vector2d(u1, v0), faceColor), v110, w, x, y, z, 0).draw(builder);
            BlockRenderUtil.bakeVertex(Vertex.create(new Vector3d(v010).add(render), new Vector2d(u0, v0), faceColor), v010, w, x, y, z, 0).draw(builder);
            BlockRenderUtil.bakeVertex(Vertex.create(new Vector3d(v011).add(render), new Vector2d(u0, v1), faceColor), v011, w, x, y, z, 0).draw(builder);
            return;
        }

        if (face == 1) {
            Vector3d faceColor = new Vector3d(ColorUtil.int1ToFloat3(c));
            BlockRenderUtil.bakeVertex(Vertex.create(new Vector3d(v001).add(render), new Vector2d(u0, v1), faceColor), v001, w, x, y, z, 1).draw(builder);
            BlockRenderUtil.bakeVertex(Vertex.create(new Vector3d(v000).add(render), new Vector2d(u0, v0), faceColor), v000, w, x, y, z, 1).draw(builder);
            BlockRenderUtil.bakeVertex(Vertex.create(new Vector3d(v100).add(render), new Vector2d(u1, v0), faceColor), v100, w, x, y, z, 1).draw(builder);
            BlockRenderUtil.bakeVertex(Vertex.create(new Vector3d(v101).add(render), new Vector2d(u1, v1), faceColor), v101, w, x, y, z, 1).draw(builder);
            return;
        }

        if (face == 2) {
            Vector3d faceColor = new Vector3d(ColorUtil.int1ToFloat3(c));
            BlockRenderUtil.bakeVertex(Vertex.create(new Vector3d(v011).add(render), new Vector2d(u0, v0), faceColor), v011, w, x, y, z, 2).draw(builder);
            BlockRenderUtil.bakeVertex(Vertex.create(new Vector3d(v001).add(render), new Vector2d(u0, v1), faceColor), v001, w, x, y, z, 2).draw(builder);
            BlockRenderUtil.bakeVertex(Vertex.create(new Vector3d(v101).add(render), new Vector2d(u1, v1), faceColor), v101, w, x, y, z, 2).draw(builder);
            BlockRenderUtil.bakeVertex(Vertex.create(new Vector3d(v111).add(render), new Vector2d(u1, v0), faceColor), v111, w, x, y, z, 2).draw(builder);
            return;
        }


        if (face == 3) {
            Vector3d faceColor = new Vector3d(ColorUtil.int1ToFloat3(c));
            BlockRenderUtil.bakeVertex(Vertex.create(new Vector3d(v010).add(render), new Vector2d(u1, v0), faceColor), v010, w, x, y, z, 3).draw(builder);
            BlockRenderUtil.bakeVertex(Vertex.create(new Vector3d(v110).add(render), new Vector2d(u0, v0), faceColor), v110, w, x, y, z, 3).draw(builder);
            BlockRenderUtil.bakeVertex(Vertex.create(new Vector3d(v100).add(render), new Vector2d(u0, v1), faceColor), v100, w, x, y, z, 3).draw(builder);
            BlockRenderUtil.bakeVertex(Vertex.create(new Vector3d(v000).add(render), new Vector2d(u1, v1), faceColor), v000, w, x, y, z, 3).draw(builder);
            return;
        }

        if (face == 4) {
            Vector3d faceColor = new Vector3d(ColorUtil.int1ToFloat3(c));
            BlockRenderUtil.bakeVertex(Vertex.create(new Vector3d(v101).add(render), new Vector2d(u0, v1), faceColor), v100, w, x, y, z, 4).draw(builder);
            BlockRenderUtil.bakeVertex(Vertex.create(new Vector3d(v100).add(render), new Vector2d(u1, v1), faceColor), v101, w, x, y, z, 4).draw(builder);
            BlockRenderUtil.bakeVertex(Vertex.create(new Vector3d(v110).add(render), new Vector2d(u1, v0), faceColor), v111, w, x, y, z, 4).draw(builder);
            BlockRenderUtil.bakeVertex(Vertex.create(new Vector3d(v111).add(render), new Vector2d(u0, v0), faceColor), v110, w, x, y, z, 4).draw(builder);
            return;
        }

        if (face == 5) {
            Vector3d faceColor = new Vector3d(ColorUtil.int1ToFloat3(c));
            BlockRenderUtil.bakeVertex(Vertex.create(new Vector3d(v011).add(render), new Vector2d(u1, v0), faceColor), v011, w, x, y, z, 5).draw(builder);
            BlockRenderUtil.bakeVertex(Vertex.create(new Vector3d(v010).add(render), new Vector2d(u0, v0), faceColor), v010, w, x, y, z, 5).draw(builder);
            BlockRenderUtil.bakeVertex(Vertex.create(new Vector3d(v000).add(render), new Vector2d(u0, v1), faceColor), v000, w, x, y, z, 5).draw(builder);
            BlockRenderUtil.bakeVertex(Vertex.create(new Vector3d(v001).add(render), new Vector2d(u1, v1), faceColor), v001, w, x, y, z, 5).draw(builder);
        }
    }

    @Override
    public void renderAsItem(VertexArrayBuilder builder, double renderX, double renderY, double renderZ) {

    }
}
