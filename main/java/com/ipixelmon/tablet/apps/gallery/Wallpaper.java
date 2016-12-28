package com.ipixelmon.tablet.apps.gallery;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.util.GuiUtil;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import net.coobird.thumbnailator.Thumbnails;


public class Wallpaper implements Comparable {

    private int texture = 0;
    private ByteBuffer buffer = null;
    private BufferedImage image;
    private File location;

    public Wallpaper(File imageLocation) {

        if (!imageLocation.exists()) {
            return;
        }

        this.location = imageLocation;

        try {

            image = ImageIO.read(imageLocation);

            int[] pixels = new int[image.getWidth() * image.getHeight()];
            // Makes an array of pixels for modification
            image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth()); // Sets
            // the RGB values to 0

            buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4); // 4
            // for RGBA, 3 for RGB

            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    int pixel = pixels[y * image.getWidth() + x];
                    buffer.put((byte) ((pixel >> 16) & 0xFF)); // Red component
                    buffer.put((byte) ((pixel >> 8) & 0xFF)); // Green component
                    buffer.put((byte) (pixel & 0xFF)); // Blue component
                    buffer.put((byte) ((pixel >> 24) & 0xFF)); // Alpha
                    // component.
                }
            }

            buffer.flip(); // FOR THE LOVE OF GOD DO NOT FORGET THIS

            // You now have a ByteBuffer filled with the color data of each
            // pixel.
            // Now just create a texture ID and bind it. Then you can load it
            // using
            // whatever OpenGL method you want

            texture = GL11.glGenTextures(); // Generate texture ID

            BufferedImage newImage = Thumbnails.of(image).size(1366, 768).asBufferedImage();

            if (imageLocation.length() / 1024 > 100) {
                File compressedImageFile = new File(imageLocation.getAbsolutePath().replaceAll("png", "jpg"));
                OutputStream os = new FileOutputStream(compressedImageFile);
                Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
                ImageWriter writer = (ImageWriter) writers.next();
                ImageOutputStream ios = ImageIO.createImageOutputStream(os);
                writer.setOutput(ios);

                ImageWriteParam param = writer.getDefaultWriteParam();

                param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                param.setCompressionQuality(0.3f);
                writer.write(null, new IIOImage(newImage, null, null), param);
                os.close();
                ios.close();
                writer.dispose();
                if (location.getName().endsWith("png")) {
                    location.delete();
                    location = compressedImageFile;
                }
            }

        } catch (IndexOutOfBoundsException e) {
            location.delete();
            Gallery.wallpapers.remove(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void drawWallpaper(int x, int y, int width, int height) {
        try {

            GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture); // Bind texture ID

            // Setup wrap mode
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

            // Setup texture scaling filtering
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

            // Send texel data to OpenGL
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, image.getWidth(), image.getHeight(), 0,
                    GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
            iPixelmon.util.gui.drawImage((float) x, (float) y, (float) width, (float) height);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int compareTo(Object o) {
        if(o instanceof Wallpaper) {
            Wallpaper wallpaper = (Wallpaper) o;
            return wallpaper.location.compareTo(location);
        }
        return -1;
    }

    public File getLocation() {
        return location;
    }

    public boolean isValid() { return location.exists(); }

    public BufferedImage getImage() {
        return image;
    }
}