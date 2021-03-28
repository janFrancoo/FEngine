package render_engine;

import model.*;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import shader.ShadowShader;
import utils.math.*;
import utils.shadow.ShadowBox;
import utils.shadow.ShadowFrameBuffer;

import java.util.List;
import java.util.Map;

public class ShadowRenderer {

    private static final int SHADOW_MAP_SIZE = 2048;

    private final ShadowShader shader;
    private final ShadowBox shadowBox;
    private final ShadowFrameBuffer shadowFBO;
    private final Matrix4f projectionMatrix = new Matrix4f();
    private final Matrix4f lightViewMatrix = new Matrix4f();
    private final Matrix4f projectionViewMatrix = new Matrix4f();
    private final Matrix4f offset = createOffset();

    public ShadowRenderer(ShadowShader shader, Camera camera) {
        this.shader = shader;
        this.shadowBox = new ShadowBox(lightViewMatrix, camera);
        this.shadowFBO = new ShadowFrameBuffer(SHADOW_MAP_SIZE, SHADOW_MAP_SIZE);
    }

    public void render(Map<TexturedModel, List<Entity>> entities, Light sun) {
        shadowBox.update();
        Vector3f sunPosition = sun.getPosition();
        Vector3f lightDirection = new Vector3f(-sunPosition.x, -sunPosition.y, -sunPosition.z);
        prepare(lightDirection, shadowBox);

        for (TexturedModel model : entities.keySet()) {
            GL30.glBindVertexArray(model.getRawModel().getVaoId());
            GL20.glEnableVertexAttribArray(0);
            GL20.glEnableVertexAttribArray(1);
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getTextureID());
            if (model.getTexture().isTransparent())
                Renderer.disableCulling();
            for (Entity entity : entities.get(model)) {
                prepareInstance(entity);
                GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(),
                        GL11.GL_UNSIGNED_INT, 0);
            }
            if (model.getTexture().isTransparent())
                Renderer.enableCulling();
        }
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL30.glBindVertexArray(0);

        shadowFBO.unbindFrameBuffer();
    }

    private void prepare(Vector3f lightDirection, ShadowBox box) {
        updateOrthoProjectionMatrix(box.getWidth(), box.getHeight(), box.getLength());
        updateLightViewMatrix(lightDirection, box.getCenter());
        Matrix4f.mul(projectionMatrix, lightViewMatrix, projectionViewMatrix);
        shadowFBO.bindFrameBuffer();
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
    }

    private void updateOrthoProjectionMatrix(float width, float height, float length) {
        projectionMatrix.setIdentity();
        projectionMatrix.m00 = 2f / width;
        projectionMatrix.m11 = 2f / height;
        projectionMatrix.m22 = -2f / length;
        projectionMatrix.m33 = 1;
    }

    private void updateLightViewMatrix(Vector3f direction, Vector3f center) {
        direction.normalise();
        center.negate();
        lightViewMatrix.setIdentity();
        float pitch = (float) Math.acos(new Vector2f(direction.x, direction.z).length());
        Matrix4f.rotate(pitch, new Vector3f(1, 0, 0), lightViewMatrix, lightViewMatrix);
        float yaw = (float) Math.toDegrees(((float) Math.atan(direction.x / direction.z)));
        yaw = direction.z > 0 ? yaw - 180 : yaw;
        Matrix4f.rotate((float) -Math.toRadians(yaw), new Vector3f(0, 1, 0), lightViewMatrix, lightViewMatrix);
        Matrix4f.translate(center, lightViewMatrix, lightViewMatrix);
    }

    private void prepareInstance(Entity entity) {
        Matrix4f modelMatrix = GameMath.createTransformationMatrix(entity.getPosition(),
                entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
        Matrix4f mvpMatrix = Matrix4f.mul(projectionViewMatrix, modelMatrix, null);
        shader.loadMVPMatrix(mvpMatrix);
    }

    private static Matrix4f createOffset() {
        Matrix4f offset = new Matrix4f();
        offset.translate(new Vector3f(0.5f, 0.5f, 0.5f));
        offset.scale(new Vector3f(0.5f, 0.5f, 0.5f));
        return offset;
    }

    public Matrix4f getToShadowMapSpaceMatrix() {
        return Matrix4f.mul(offset, projectionViewMatrix, null);
    }

    public void clean() {
        shader.clean();
        shadowFBO.cleanUp();
    }

    public int getShadowMap() {
        return shadowFBO.getShadowMap();
    }

    public Matrix4f getLightSpaceTransform() {
        return lightViewMatrix;
    }

}
