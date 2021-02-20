package render_engine;

import model.*;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import shader.TerrainShader;
import utils.math.Matrix4f;
import utils.math.Vector3f;

import java.util.List;

public class TerrainRenderer {

    private final TerrainShader shader;

    public TerrainRenderer(TerrainShader terrainShader) {
        this.shader = terrainShader;
    }

    public void render(List<Terrain> terrains) {
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
        shader.loadShineValues(terrain.getTexture().getShineDamper(), terrain.getTexture().getReflectivity());
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrain.getTexture().getTextureID());
    }

    private void prepareInstance(Terrain terrain) {
        Matrix4f transformationMatrix = Matrix4f.createTransformationMatrix(
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
