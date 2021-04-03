package render_engine;

import org.lwjgl.opengl.GL11;
import utils.post_processing.FBO;

public class ImageRenderer {

    private FBO fbo;

    public ImageRenderer(int width, int height) {
        this.fbo = new FBO(width, height, FBO.NONE);
    }

    public ImageRenderer() {}

    public void renderQuad() {
        if (fbo != null) {
            fbo.bindFrameBuffer();
        }
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
        if (fbo != null) {
            fbo.unbindFrameBuffer();
        }
    }

    public int getOutputTexture() {
        return fbo.getColorTexture();
    }

    public void clean() {
        if (fbo != null) {
            fbo.cleanUp();
        }
    }

}
