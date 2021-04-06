package render_engine;

import model.*;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import shader.EntityShader;
import utils.math.GameMath;
import utils.math.Matrix4f;
import java.util.List;
import java.util.Map;

import static utils.Constants.*;

public class EntityRenderer {

    private final EntityShader shader;

    public EntityRenderer(EntityShader shader) {
        this.shader = shader;
        shader.start();
        shader.loadFogValues(FOG_DENSITY, FOG_GRADIENT);
        shader.loadSkyColor(SKY_COLOR);
        shader.loadCelLevel();
        shader.stop();
    }

    public void render(Map<TexturedModel, List<Entity>> entities) {
        for (TexturedModel texturedModel : entities.keySet()) {
            prepareTexturedModel(texturedModel);
            List<Entity> batch = entities.get(texturedModel);
            for (Entity entity : batch) {
                prepareInstance(entity);
                GL11.glDrawElements(GL11.GL_TRIANGLES, texturedModel.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
            }
            unbindTextureModel();
        }
    }

    private void prepareTexturedModel(TexturedModel texturedModel) {
        RawModel rawModel = texturedModel.getRawModel();
        GL30.glBindVertexArray(rawModel.getVaoId());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        if (texturedModel.getTexture().isTransparent())
            Renderer.disableCulling();
        shader.loadTextureRows(texturedModel.getTexture().getRows());
        shader.loadFakeLightValue(texturedModel.getTexture().isFakeLight());
        shader.loadShineValues(texturedModel.getTexture().getShineDamper(), texturedModel.getTexture().getReflectivity());
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturedModel.getTexture().getTextureID());
        shader.loadUseSpecularMap(texturedModel.getTexture().hasSpecularMap());
        if (texturedModel.getTexture().hasSpecularMap()) {
            GL13.glActiveTexture(GL13.GL_TEXTURE1);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturedModel.getTexture().getSpecularMap());
        }
    }

    private void prepareInstance(Entity entity) {
        Matrix4f transformationMatrix = GameMath.createTransformationMatrix(entity.getPosition(), entity.getRotX(),
                entity.getRotY(), entity.getRotZ(), entity.getScale());
        shader.loadTransformationMatrix(transformationMatrix);
        shader.loadTextureOffset(entity.getTextureOffset());
    }

    private void unbindTextureModel() {
        Renderer.enableCulling();
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

}
