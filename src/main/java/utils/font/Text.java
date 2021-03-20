package utils.font;

import model.TextGUI;
import render_engine.FontRenderer;
import render_engine.ModelLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Text {

    private static ModelLoader loader;
    private static final Map<FontType, List<TextGUI>> texts = new HashMap<>();
    private static FontRenderer renderer;

    public static void init(ModelLoader modelLoader) {
        loader = modelLoader;
        renderer = new FontRenderer();
    }

    public static void render() {
        renderer.render(texts);
    }

    public static void loadText(TextGUI text) {
        FontType font = text.getFont();
        TextMeshData data = font.loadText(text);
        int vaoId = loader.loadToVao(data.getVertexPositions(), data.getTextureCoords());
        text.setMeshInfo(vaoId, data.getVertexCount());
        List<TextGUI> textBatch = texts.get(font);
        if (textBatch == null) {
            textBatch = new ArrayList<>();
            texts.put(font, textBatch);
        }
        textBatch.add(text);
    }

    public static void removeText(TextGUI text) {
        List<TextGUI> textBatch = texts.get(text.getFont());
        textBatch.remove(text);
        if (textBatch.isEmpty())
            texts.remove(text.getFont());
    }

    public static void clean() {
        renderer.clean();
    }

}
