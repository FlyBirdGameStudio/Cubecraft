package io.flybird.starfish3d.render.textures;

import io.flybird.util.ImageUtil;
import io.flybird.util.container.BufferUtil;
import io.flybird.util.math.AABB2D;
import io.flybird.util.math.MathHelper;
import io.flybird.util.task.TaskProgressUpdateListener;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Texture2DTileMap extends Texture2D {
    private BufferedImage img;
    private final HashMap<String, Section> map = new HashMap<>();
    private final ArrayList<ITextureImage> plannedLoad = new ArrayList<>();
    public final ArrayList<Section> sections = new ArrayList<>();
    private final int sectionSizeH;
    private final int sectionSizeV;
    private int counter=0;

    public Texture2DTileMap(boolean mipMap, int sectionSizeH, int sectionSizeV) {
        super(false, mipMap);
        this.sectionSizeH = sectionSizeH;
        this.sectionSizeV = sectionSizeV;
    }

    @Override
    public void load(ITextureImage image) {
        Image img = new Image(image.getName(), image.getAsImage());
        if (img.image != null) {
            this.tryPutImage(img);
        }
    }

    public void tryPutImage(Image image) {
        for (Section section : sections) {
            if (section.getAvailableSpace() > image.getPixels() / (float) this.sectionSizeH / (float) this.sectionSizeV) {
                if (section.tryPutImage(image)) {
                    this.map.put(image.s, section);
                    return;
                }
            }
        }
        Section section = new Section(sectionSizeH, sectionSizeV, this, counter);
        this.sections.add(section);
        section.tryPutImage(image);
        this.map.put(image.s, section);
        counter++;
    }

    public void drawSection() {
        int columnCount = (16384 / sectionSizeH);
        int w = this.sections.size() % columnCount;
        int h = this.sections.size() / columnCount;
        this.width = this.sections.size() > columnCount ? columnCount * sectionSizeH : w*sectionSizeH;
        this.height = (h == 0 ? sectionSizeV : sectionSizeV * h);
        this.img = new BufferedImage(width, height, 2);
        int x = 0, y = 0;
        for (Section s : this.sections) {
            this.img.getGraphics().drawImage(s.image, x * sectionSizeH, y * sectionSizeV, null);
            x++;
            if (x > columnCount) {
                x = 0;
                y++;
            }
        }
    }

    public float exactTextureU(String tex, double u) {
        return (float) this.map.get(tex).exactTextureU(tex, u);
    }

    public float exactTextureV(String tex, double v) {
        return (float) this.map.get(tex).exactTextureV(tex, v);
    }

    public void register(ITextureImage file) {
        for (ITextureImage img:this.plannedLoad){
            if(Objects.equals(img.getName(), file.getName()))return;
        }
        plannedLoad.add(file);
    }

    public void completePlannedLoad(TaskProgressUpdateListener l, int start, int end) {
        int i = 0;
        for (ITextureImage s : plannedLoad) {
            this.load(s);
            i++;
            l.onProgressChange((int) MathHelper.scale(i, start, end, 0, plannedLoad.size()));
        }
        plannedLoad.clear();
    }

    public void export(File f) {
        try {
            ImageIO.write(this.img, "png", f);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static class Section {
        private final BufferedImage image;
        private final int sizeH;
        private final int sizeV;
        private final HashMap<String, AABB2D> tex = new HashMap<>();
        private final Texture2DTileMap parent;
        boolean[][] usage;
        private final int id;

        public Section(int sizeH, int sizeV, Texture2DTileMap parent, int id) {
            this.sizeH = sizeH;
            this.sizeV = sizeV;
            this.id = id;
            this.image = new BufferedImage(sizeH, sizeV, BufferedImage.TYPE_INT_ARGB);
            this.parent = parent;
            this.usage = new boolean[sizeH][sizeV];
        }

        public boolean tryPutImage(Image image) {

            AABB2D aabb = image.generateBounding();
            for (int i = 0; i < sizeH - image.image.getWidth() + 1; i++) {
                for (int j = 0; j < sizeV - image.image.getHeight() + 1; j++) {
                    AABB2D movedPos = new AABB2D(aabb).move(i, j);
                    boolean anyIntersect = false;
                    for (AABB2D test : tex.values()) {
                        if (test.intersect(movedPos)) {
                            anyIntersect = true;
                        }
                    }
                    if (!anyIntersect) {
                        this.image.getGraphics().drawImage(image.image, i, j, null);
                        this.tex.put(image.s, movedPos);
                        for (int x = i; x < i + image.image.getWidth(); x++) {
                            for (int y = j; y < j + image.image.getHeight(); y++) {
                                this.usage[x][y] = true;
                            }
                        }
                        return true;
                    }
                }
            }
            return false;
        }

        public float getAvailableSpace() {
            int all = this.usage.length * this.usage.length;
            int f = 0;
            for (boolean[] booleans : this.usage) {
                for (int y = 0; y < this.usage.length; y++) {
                    f += booleans[y] ? 1 : 0;
                }
            }
            return 1 - f / (float) all;
        }

        public double exactTextureU(String tex, double u) {
            AABB2D aabb = this.tex.get(tex);
            int columnCount = (16384 / sizeH);
            int off=this.id%columnCount;
            return ((aabb.x1 - aabb.x0) * u+this.sizeH*off) / this.parent.getWidth();
        }

        public double exactTextureV(String tex, double v) {
            AABB2D aabb = this.tex.get(tex);
            int columnCount = (16384 / sizeH);
            int off=this.id/columnCount;
            return ((aabb.y1 - aabb.y0) * v+this.sizeV*off) / this.parent.getHeight();
        }
    }

    public record Image(String s, BufferedImage image) {
        public AABB2D generateBounding() {
            return new AABB2D(0, 0, image.getWidth(), image.getHeight());
        }

        public int getPixels() {
            return image.getWidth() * image.getHeight();
        }
    }


    public static Texture2DTileMap autoGenerate(ITextureImage[] file, boolean mipMap) {
        int maxSizeH = 1;
        int maxSizeV=1;
        for (ITextureImage f : file) {
            BufferedImage testImg = f.getAsImage();
            if (testImg != null) {
                int i = testImg.getWidth();
                int i2 = testImg.getHeight();
                if (i > maxSizeH) maxSizeH = i;
                if (i2 > maxSizeV) maxSizeV = i2;
            }
        }
        return new Texture2DTileMap(mipMap, maxSizeH,maxSizeV);
    }

    public void upload() {
        this.drawSection();
        ByteBuffer buffer = ImageUtil.getByteFromBufferedImage_RGBA(img);
        GL11.glTexImage2D(this.getBindingType(), 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
        BufferUtil.free(buffer);
    }
}
