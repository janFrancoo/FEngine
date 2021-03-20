package render_engine;

import model.TextGUI;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import shader.FontShader;
import utils.font.FontType;

import java.util.List;
import java.util.Map;

public class FontRenderer {

    private final FontShader shader;

    public FontRenderer(FontShader shader) {
        this.shader = shader;
    }

    public void render(Map<FontType, List<TextGUI>> texts) {
        prepare();
        for (FontType fontType : texts.keySet()) {
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL13.glBindTexture(GL11.GL_TEXTURE_2D, fontType.getTextureAtlas());
            for (TextGUI textGUI : texts.get(fontType))
                renderText(textGUI);
        }
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
    }

    private void prepare() {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
    }

    private void renderText(TextGUI textGUI) {
        GL30.glBindVertexArray(textGUI.getMesh());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        shader.loadColor(textGUI.getColor());
        shader.loadTranslation(textGUI.getPosition());
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, textGUI.getVertexCount());
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL30.glBindVertexArray(0);
    }

}
