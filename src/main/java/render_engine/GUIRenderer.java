package render_engine;

import model.RawModel;
import model.TextureGUI;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;
import shader.GUIShader;
import utils.math.GameMath;
import utils.math.Matrix4f;

import java.util.List;

public class GUIRenderer {

    private final RawModel quad;
    private final GUIShader guiShader;

    public GUIRenderer(GUIShader guiShader, ModelLoader loader) {
        float[] positions = {
                -1, 1,
                -1, -1,
                1, 1,
                1, -1
        };
        quad = loader.loadToVao(positions, 2);
        this.guiShader = guiShader;
    }

    public void render(List<TextureGUI> guis) {
        GL30.glBindVertexArray(quad.getVaoId());
        GL30.glEnableVertexAttribArray(0);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        for (TextureGUI gui : guis) {
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, gui.getTextureId());
            Matrix4f transformationMatrix = GameMath.createTransformationMatrix(gui.getPosition(), gui.getScale());
            guiShader.loadTransformationMatrix(transformationMatrix);
            GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
        }

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_BLEND);
        GL30.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
    }

}
