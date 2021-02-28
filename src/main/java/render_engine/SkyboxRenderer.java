package render_engine;

import model.RawModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import shader.SkyboxShader;

import static utils.Constants.SKY_COLOR;

public class SkyboxRenderer {

    private final RawModel model;
    private final int dayTextureId;
    private final int nightTextureId;
    private final SkyboxShader shader;
    private float time = 0;

    public SkyboxRenderer(SkyboxShader shader, ModelLoader loader) {
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
        dayTextureId = loader.loadCubeMap(new String[] {"right", "left", "top", "bottom", "back", "front"});
        nightTextureId = loader.loadCubeMap(new String[] {"nightRight", "nightLeft", "nightTop", "nightBottom",
                "nightBack", "nightFront"});
        this.shader = shader;
        shader.start();
        shader.loadFogColor(SKY_COLOR);
        shader.loadBlendFactor(0.5f);
        shader.connectTextureUnits();
        shader.stop();
    }

    public void render() {
        GL30.glBindVertexArray(model.getVaoId());
        GL20.glEnableVertexAttribArray(0);
        bindTexturesAccordingToDayNightCycle();
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, model.getVertexCount());
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
    }

    private void bindTexturesAccordingToDayNightCycle() {
        time += DisplayManager.getDelta() * 1000;
        time %= 24000;

        int texture1, texture2;
        float blendFactor;

        if (time >= 0 && time < 5000) {
            texture1 = nightTextureId;
            texture2 = nightTextureId;
            blendFactor = (time - 0) / 5000;
        } else if (time >= 5000 && time < 8000) {
            texture1 = nightTextureId;
            texture2 = dayTextureId;
            blendFactor = (time - 5000) / (8000 - 5000);
        } else if (time >= 8000 && time < 21000) {
            texture1 = dayTextureId;
            texture2 = dayTextureId;
            blendFactor = (time - 8000) / (21000 - 8000);
        } else {
            texture1 = dayTextureId;
            texture2 = nightTextureId;
            blendFactor = (time - 21000) / (24000 - 21000);
        }

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture1);
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture2);
        shader.loadBlendFactor(blendFactor);
    }

}
