package render_engine;

import model.RawModel;
import model.WaterTile;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import shader.WaterShader;
import utils.math.GameMath;
import utils.math.Matrix4f;
import utils.math.Vector3f;

import java.util.List;

import static utils.Constants.*;

public class WaterRenderer {

    private final RawModel quad;
    private final WaterShader shader;
    private final WaterFrameBuffers frameBuffers;
    private final int dudvTexture;
    private final int normalTexture;
    private float moveFactor = 0;

    public WaterRenderer(WaterShader shader, WaterFrameBuffers frameBuffers, ModelLoader loader) {
        this.shader = shader;
        this.frameBuffers = frameBuffers;
        this.dudvTexture = loader.loadTexture(DUDV_MAP);
        this.normalTexture = loader.loadTexture(NORMAL_MAP);

        float[] vertices = { -1, -1, -1, 1, 1, -1, 1, -1, -1, 1, 1, 1 };
        quad = loader.loadToVao(vertices, 2);
    }

    public void render(List<WaterTile> waterTiles) {
        moveFactor += WAVE_SPEED * DisplayManager.getDelta();
        moveFactor %= 1;
        shader.loadMoveFactor(moveFactor);

        GL30.glBindVertexArray(quad.getVaoId());
        GL20.glEnableVertexAttribArray(0);

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, frameBuffers.getReflectionTexture());
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, frameBuffers.getRefractionTexture());
        GL13.glActiveTexture(GL13.GL_TEXTURE2);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, dudvTexture);
        GL13.glActiveTexture(GL13.GL_TEXTURE3);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, normalTexture);

        for (WaterTile waterTile: waterTiles) {
            Matrix4f transformationMatrix = GameMath.createTransformationMatrix(new Vector3f(waterTile.getX(),
                    waterTile.getHeight(), waterTile.getZ()), 0, 0, 0, TILE_SIZE);
            shader.loadTransformationMatrix(transformationMatrix);
            GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, quad.getVertexCount());
        }

        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
    }

}
