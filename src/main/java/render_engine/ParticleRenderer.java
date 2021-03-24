package render_engine;

import model.Particle;
import model.RawModel;
import model.TextureParticle;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import shader.ParticleShader;
import utils.math.Matrix4f;
import utils.math.Vector3f;

import java.nio.FloatBuffer;
import java.util.List;
import java.util.Map;

import static utils.Constants.MAX_PARTICLE_INSTANCE;

public class ParticleRenderer {

    private static final float[] VERTICES = {-0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, -0.5f};
    private static final int INSTANCE_DATA_LEN = 21;
    private static final FloatBuffer buffer = BufferUtils.createFloatBuffer(MAX_PARTICLE_INSTANCE * INSTANCE_DATA_LEN);

    private final RawModel quad;
    private final ParticleShader shader;
    private final ModelLoader loader;
    private int vbo;
    private int pointer = 0;

    public ParticleRenderer(ModelLoader loader, ParticleShader shader) {
        this.loader = loader;
        this.vbo = loader.createEmptyVBO(INSTANCE_DATA_LEN * MAX_PARTICLE_INSTANCE);
        quad = loader.loadToVao(VERTICES, 2);
        loader.addInstancedAttribute(quad.getVaoId(), vbo, 1, 4, INSTANCE_DATA_LEN, 0);
        loader.addInstancedAttribute(quad.getVaoId(), vbo, 2, 4, INSTANCE_DATA_LEN, 4);
        loader.addInstancedAttribute(quad.getVaoId(), vbo, 3, 4, INSTANCE_DATA_LEN, 8);
        loader.addInstancedAttribute(quad.getVaoId(), vbo, 4, 4, INSTANCE_DATA_LEN, 12);
        loader.addInstancedAttribute(quad.getVaoId(), vbo, 5, 4, INSTANCE_DATA_LEN, 16);
        loader.addInstancedAttribute(quad.getVaoId(), vbo, 6, 1, INSTANCE_DATA_LEN, 20);
        this.shader = shader;
    }

    public void render(Map<TextureParticle, List<Particle>> particles, Matrix4f viewMatrix) {
        shader.start();
        prepare();
        for (TextureParticle texture : particles.keySet()) {
            bindTexture(texture);
            List<Particle> particleList = particles.get(texture);
            pointer = 0;
            float[] vboData = new float[particleList.size() * INSTANCE_DATA_LEN];
            for (Particle particle : particleList) {
                updateModelViewMatrix(viewMatrix, particle.getPosition(), particle.getRotation(), particle.getScale(),
                        vboData);
                updateTexCoordInfo(particle, vboData);
            }
            loader.updateVBO(vbo, vboData, buffer);
            GL31.glDrawArraysInstanced(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount(), particleList.size());
        }
        unbind();
        shader.stop();
    }

    private void prepare() {
        GL30.glBindVertexArray(quad.getVaoId());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        GL20.glEnableVertexAttribArray(3);
        GL20.glEnableVertexAttribArray(4);
        GL20.glEnableVertexAttribArray(5);
        GL20.glEnableVertexAttribArray(6);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDepthMask(false);
    }

    private void updateTexCoordInfo(Particle particle, float[] data) {
        data[pointer++] = particle.getTextureOffset().x;
        data[pointer++] = particle.getTextureOffset().y;
        data[pointer++] = particle.getTextureOffset2().x;
        data[pointer++] = particle.getTextureOffset2().y;
        data[pointer++] = particle.getBlend();
    }

    private void bindTexture(TextureParticle texture) {
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureId());
        shader.loadRows(texture.getRows());
    }

    private void updateModelViewMatrix(Matrix4f viewMatrix, Vector3f position, float rotation, float scale,
                                       float[] vboData) {
        Matrix4f modelMatrix = new Matrix4f();
        Matrix4f.translate(position, modelMatrix, modelMatrix);
        modelMatrix.m00 = viewMatrix.m00;
        modelMatrix.m01 = viewMatrix.m10;
        modelMatrix.m02 = viewMatrix.m20;
        modelMatrix.m10 = viewMatrix.m01;
        modelMatrix.m11 = viewMatrix.m11;
        modelMatrix.m12 = viewMatrix.m21;
        modelMatrix.m20 = viewMatrix.m02;
        modelMatrix.m21 = viewMatrix.m12;
        modelMatrix.m22 = viewMatrix.m22;
        Matrix4f.rotate((float) Math.toRadians(rotation), new Vector3f(0, 0, 1), modelMatrix, modelMatrix);
        Matrix4f.scale(new Vector3f(scale, scale, scale), modelMatrix, modelMatrix);
        Matrix4f modelViewMatrix = Matrix4f.mul(viewMatrix, modelMatrix, null);
        storeMatrixData(modelViewMatrix, vboData);
    }

    private void storeMatrixData(Matrix4f matrix4f, float[] vboData) {
        vboData[pointer++] = matrix4f.m00;
        vboData[pointer++] = matrix4f.m01;
        vboData[pointer++] = matrix4f.m02;
        vboData[pointer++] = matrix4f.m03;
        vboData[pointer++] = matrix4f.m10;
        vboData[pointer++] = matrix4f.m11;
        vboData[pointer++] = matrix4f.m12;
        vboData[pointer++] = matrix4f.m13;
        vboData[pointer++] = matrix4f.m20;
        vboData[pointer++] = matrix4f.m21;
        vboData[pointer++] = matrix4f.m22;
        vboData[pointer++] = matrix4f.m23;
        vboData[pointer++] = matrix4f.m30;
        vboData[pointer++] = matrix4f.m31;
        vboData[pointer++] = matrix4f.m32;
        vboData[pointer++] = matrix4f.m33;
    }

    private void unbind() {
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL20.glDisableVertexAttribArray(3);
        GL20.glDisableVertexAttribArray(4);
        GL20.glDisableVertexAttribArray(5);
        GL20.glDisableVertexAttribArray(6);
        GL30.glBindVertexArray(0);
    }

}
