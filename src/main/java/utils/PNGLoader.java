package utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class PNGLoader {

    public static TextureData decodePNG(String fileName) {
        int[] pixels = null;
        int width = 0, height = 0;

        try {
            InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName + ".png");
            assert stream != null;
            BufferedImage image = ImageIO.read(stream);
            width = image.getWidth();
            height = image.getHeight();
            pixels = new int[width * height];
            image.getRGB(0, 0, width, height, pixels, 0, width);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int[] data = new int[width * height];
        for (int i = 0; i < width * height; i++) {
            int a = (pixels[i] & 0xff000000) >> 24;
            int r = (pixels[i] & 0xff0000) >> 16;
            int g = (pixels[i] & 0xff00) >> 8;
            int b = (pixels[i] & 0xff);

            data[i] = a << 24 | b << 16 | g << 8 | r;
        }

        return new TextureData(data, width, height);
    }

}
