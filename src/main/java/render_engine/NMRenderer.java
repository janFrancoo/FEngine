package render_engine;

import model.*;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import shader.NMShader;
import utils.math.GameMath;
import utils.math.Matrix4f;

import java.util.List;
import java.util.Map;

import static utils.Constants.*;

public class NMRenderer {

    private final NMShader shader;

    public NMRenderer(NMShader shader) {
        this.shader = shader;
        this.shader.start();
        this.shader.connectTextureUnits();
        this.shader.loadFogValues(FOG_DENSITY, FOG_GRADIENT);
        this.shader.loadSkyColor(SKY_COLOR);
        this.shader.stop();
    }

    public void render(Map<TexturedModel, List<Entity>> entities) {
        shader.start();
        for (TexturedModel texturedModel : entities.keySet()) {
            prepareTexturedModel(texturedModel);
            List<Entity> batch = entities.get(texturedModel);
            for (Entity entity : batch) {
                prepareInstance(entity);
                GL11.glDrawElements(GL11.GL_TRIANGLES, texturedModel.getRawModel().getVertexCount(),
                        GL11.GL_UNSIGNED_INT, 0);
            }
            unbindTextureModel();
        }
        shader.stop();
    }

    private void prepareTexturedModel(TexturedModel texturedModel) {
        RawModel rawModel = texturedModel.getRawModel();
        GL30.glBindVertexArray(rawModel.getVaoId());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        GL20.glEnableVertexAttribArray(3);
        if (texturedModel.getTexture().isTransparent())
            Renderer.disableCulling();
        shader.loadTextureRows(texturedModel.getTexture().getRows());
        shader.loadShineValues(texturedModel.getTexture().getShineDamper(), texturedModel.getTexture().getReflectivity());
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturedModel.getTexture().getTextureID());
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturedModel.getTexture().getNormalMapID());
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
        GL20.glDisableVertexAttribArray(3);
        GL30.glBindVertexArray(0);
    }

}
