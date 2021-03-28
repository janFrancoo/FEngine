package render_engine;

import model.*;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import shader.TerrainShader;
import utils.math.GameMath;
import utils.math.Matrix4f;
import utils.math.Vector3f;

import java.util.List;

import static utils.Constants.*;

public class TerrainRenderer {

    private final TerrainShader shader;

    public TerrainRenderer(TerrainShader shader) {
        this.shader = shader;
        shader.start();
        shader.connectTextureUnits();
        shader.loadFogValues(FOG_DENSITY, FOG_GRADIENT);
        shader.loadSkyColor(SKY_COLOR);
        shader.loadCelLevel();
        shader.stop();
    }

    public void render(List<Terrain> terrains, Matrix4f toShadowSpace) {
        shader.loadToShadowMapSpace(toShadowSpace);
        for (Terrain terrain : terrains) {
            prepareTerrain(terrain);
            prepareInstance(terrain);
            GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
            unbindTextureModel();
        }
    }

    private void prepareTerrain(Terrain terrain) {
        RawModel rawModel = terrain.getRawModel();
        GL30.glBindVertexArray(rawModel.getVaoId());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        shader.loadShineValues(1, 0);
        bindTextures(terrain);
    }

    private void bindTextures(Terrain terrain) {
        TerrainTexturePack texturePack = terrain.getTexturePack();
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getBackgroundTexture().getTextureID());
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getRTexture().getTextureID());
        GL13.glActiveTexture(GL13.GL_TEXTURE2);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getGTexture().getTextureID());
        GL13.glActiveTexture(GL13.GL_TEXTURE3);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getBTexture().getTextureID());
        GL13.glActiveTexture(GL13.GL_TEXTURE4);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrain.getBlendMap().getTextureID());
    }

    private void prepareInstance(Terrain terrain) {
        Matrix4f transformationMatrix = GameMath.createTransformationMatrix(
                new Vector3f(terrain.getX(), 0, terrain.getZ()), 0, 0, 0, 1
        );
        shader.loadTransformationMatrix(transformationMatrix);
    }

    private void unbindTextureModel() {
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

}
