package utils.post_processing;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import render_engine.ImageRenderer;
import shader.ContrastShader;

public class ContrastChanger {

    private final ImageRenderer renderer;
    private final ContrastShader shader;

    public ContrastChanger() {
        this.renderer = new ImageRenderer();
        this.shader = new ContrastShader();
    }

    public void render(int texture) {
        shader.start();
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
        renderer.renderQuad();
        shader.stop();
    }

    public void clean() {
        renderer.clean();
        shader.clean();
    }

}
