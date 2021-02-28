package render_engine;

import model.RawModel;
import utils.TextureData;
import org.lwjgl.opengl.*;
import utils.BufferHelper;
import utils.PNGLoader;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class ModelLoader {

    private final List<Integer> vaoList = new ArrayList<>();
    private final List<Integer> vboList = new ArrayList<>();
    private final List<Integer> textureIDs = new ArrayList<>();

    private final BufferHelper bufferHelper = new BufferHelper();

    public int loadTexture(String fileName) {
        TextureData textureData = PNGLoader.decodePNG(fileName);
        int result = GL11.glGenTextures();
        textureIDs.add(result);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, result);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, textureData.getWidth(), textureData.getHeight(),
                0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, bufferHelper.storeDataInIntBuffer(textureData.getBuffer()));
        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -0.4f);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        return result;
    }

    public int loadCubeMap(String[] textureFiles) {
        int textureId = GL11.glGenTextures();
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, textureId);

        for (int i=0; i<textureFiles.length; i++) {
            TextureData textureData = PNGLoader.decodePNG(textureFiles[i]);
            GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL11.GL_RGBA,
                    textureData.getWidth(), textureData.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE,
                    bufferHelper.storeDataInIntBuffer(textureData.getBuffer()));
        }

        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        textureIDs.add(textureId);
        return textureId;
    }

    public RawModel loadToVao(float[] positions, int[] indices, float[] textureCoords, float[] normals) {
        int vaoId = GL30.glGenVertexArrays();
        vaoList.add(vaoId);
        GL30.glBindVertexArray(vaoId);
        bindIndicesBuffer(indices);
        storeDataInAttrList(0, 3, positions);
        storeDataInAttrList(1, 2, textureCoords);
        storeDataInAttrList(2, 3, normals);
        GL30.glBindVertexArray(0);
        return new RawModel(vaoId, indices.length);
    }

    public RawModel loadToVao(float[] positions, int dimension) {
        int vaoId = GL30.glGenVertexArrays();
        vaoList.add(vaoId);
        GL30.glBindVertexArray(vaoId);
        storeDataInAttrList(0, dimension, positions);
        GL30.glBindVertexArray(0);
        return new RawModel(vaoId, positions.length / dimension);
    }

    // Every vao has a slot for index buffers, no need to specify attr idx and unbind
    private void bindIndicesBuffer(int[] indices) {
        int vboId = GL15.glGenBuffers();
        vboList.add(vboId);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboId);
        IntBuffer buffer = bufferHelper.storeDataInIntBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }

    private void storeDataInAttrList(int attrNumber, int coordinateSize, float[] data) {
        int vboId = GL15.glGenBuffers();
        vboList.add(vboId);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
        FloatBuffer buffer = bufferHelper.storeDataInFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attrNumber, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
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
