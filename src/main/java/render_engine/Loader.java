package render_engine;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class Loader {

    private final List<Integer> vaoList = new ArrayList<>();
    private final List<Integer> vboList = new ArrayList<>();

    public RawModel loadToVao(float[] positions, int[] indices) {
        int vaoId = GL30.glGenVertexArrays();
        vaoList.add(vaoId);
        GL30.glBindVertexArray(vaoId);
        bindIndicesBuffer(indices);
        storeDataInAttrList(0, positions);
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

    private void storeDataInAttrList(int attrNumber, float[] data) {
        int vboId = GL15.glGenBuffers();
        vboList.add(vboId);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
        FloatBuffer buffer = storeDataInFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attrNumber, 3, GL11.GL_FLOAT, false, 0, 0);
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
        for (int vboId: vboList)
            GL15.glDeleteBuffers(vboId);
    }

}
