package render_engine;

import model.RawModel;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class Loader {

    private final List<Integer> vaoList = new ArrayList<>();
    private final List<Integer> vboList = new ArrayList<>();
    private final List<Integer> textureIDs = new ArrayList<>();

    public int loadTexture(String fileName) {
        int[] pixels = null;
        int width = 0, height = 0;

        try {
            BufferedImage image = ImageIO.read(new FileInputStream("res/" + fileName + ".png"));
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

        int result = GL11.glGenTextures();
        textureIDs.add(result);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, result);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA,
                GL11.GL_UNSIGNED_BYTE, storeDataInIntBuffer(data));
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        return result;
    }

    public RawModel loadToVao(float[] positions, int[] indices) {
        int vaoId = GL30.glGenVertexArrays();
        vaoList.add(vaoId);
        GL30.glBindVertexArray(vaoId);
        bindIndicesBuffer(indices);
        storeDataInAttrList(0, 3, positions);
        GL30.glBindVertexArray(0);
        return new RawModel(vaoId, indices.length);
    }

    public RawModel loadToVao(float[] positions, int[] indices, float[] textureCoords) {
        int vaoId = GL30.glGenVertexArrays();
        vaoList.add(vaoId);
        GL30.glBindVertexArray(vaoId);
        bindIndicesBuffer(indices);
        storeDataInAttrList(0, 3, positions);
        storeDataInAttrList(1, 2, textureCoords);
        GL30.glBindVertexArray(0);
        return new RawModel(vaoId, indices.length);
    }

    // Every vao has a slot for index buffers, no need to specify attr idx and unbind
    private void bindIndicesBuffer(int[] indices) {
        int vboId = GL15.glGenBuffers();
        vboList.add(vboId);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboId);
        IntBuffer buffer = storeDataInIntBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }

    private void storeDataInAttrList(int attrNumber, int coordinateSize, float[] data) {
        int vboId = GL15.glGenBuffers();
        vboList.add(vboId);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
        FloatBuffer buffer = storeDataInFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attrNumber, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    private FloatBuffer storeDataInFloatBuffer(float[] data) {
        FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(data.length);
        floatBuffer.put(data);
        floatBuffer.flip();
        return floatBuffer;
    }

    private IntBuffer storeDataInIntBuffer(int[] data) {
        IntBuffer intBuffer = BufferUtils.createIntBuffer(data.length);
        intBuffer.put(data);
        intBuffer.flip();
        return intBuffer;
    }

    public void clean() {
        for (int vaoId : vaoList)
            GL30.glDeleteVertexArrays(vaoId);
        for (int vboId : vboList)
            GL15.glDeleteBuffers(vboId);
        for (int textureId : textureIDs)
            GL11.glDeleteTextures(textureId);
    }

}
