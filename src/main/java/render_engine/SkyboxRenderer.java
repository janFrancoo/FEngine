package render_engine;

import model.RawModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class SkyboxRenderer {

    private final RawModel model;
    private final int textureId;

    public SkyboxRenderer(ModelLoader loader) {
        float SIZE = 500f;
        float[] VERTICES = {
                -SIZE, SIZE, -SIZE,
                -SIZE, -SIZE, -SIZE,
                SIZE, -SIZE, -SIZE,
                SIZE, -SIZE, -SIZE,
                SIZE, SIZE, -SIZE,
                -SIZE, SIZE, -SIZE,

                -SIZE, -SIZE, SIZE,
                -SIZE, -SIZE, -SIZE,
                -SIZE, SIZE, -SIZE,
                -SIZE, SIZE, -SIZE,
                -SIZE, SIZE, SIZE,
                -SIZE, -SIZE, SIZE,

                SIZE, -SIZE, -SIZE,
                SIZE, -SIZE, SIZE,
                SIZE, SIZE, SIZE,
                SIZE, SIZE, SIZE,
                SIZE, SIZE, -SIZE,
                SIZE, -SIZE, -SIZE,

                -SIZE, -SIZE, SIZE,
                -SIZE, SIZE, SIZE,
                SIZE, SIZE, SIZE,
                SIZE, SIZE, SIZE,
                SIZE, -SIZE, SIZE,
                -SIZE, -SIZE, SIZE,

                -SIZE, SIZE, -SIZE,
                SIZE, SIZE, -SIZE,
                SIZE, SIZE, SIZE,
                SIZE, SIZE, SIZE,
                -SIZE, SIZE, SIZE,
                -SIZE, SIZE, -SIZE,

                -SIZE, -SIZE, -SIZE,
                -SIZE, -SIZE, SIZE,
                SIZE, -SIZE, -SIZE,
                SIZE, -SIZE, -SIZE,
                -SIZE, -SIZE, SIZE,
                SIZE, -SIZE, SIZE
        };

        model = loader.loadToVao(VERTICES, 3);
        textureId = loader.loadCubeMap(new String[] {"right", "left", "top", "bottom", "back", "front"});
    }

    public void render() {
        GL30.glBindVertexArray(model.getVaoId());
        GL20.glEnableVertexAttribArray(0);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, textureId);
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, model.getVertexCount());
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
    }

}
