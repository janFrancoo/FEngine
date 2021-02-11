package main;

import model.TexturedModel;
import render_engine.DisplayManager;
import render_engine.Loader;
import model.RawModel;
import render_engine.Renderer;
import shader.StaticShader;
import texture.Texture;

public class GameLoop {

    public static void main(String[] args) {
        DisplayManager.createDisplay();

        Loader loader = new Loader();
        Renderer renderer = new Renderer();
        StaticShader shader = new StaticShader("texturedVertexShader", "texturedFragmentShader");

        float[] vertices = {
                -0.5f,  0.5f, 0f,
                -0.5f, -0.5f, 0f,
                 0.5f, -0.5f, 0f,
                 0.5f,  0.5f, 0f,
        };

        int[] indices = {
                0, 1, 3,
                3, 1, 2
        };

        float[] textureCoords = {
                0, 0,
                0, 1,
                1, 1,
                1, 0
        };

        RawModel rawModel = loader.loadToVao(vertices, indices, textureCoords);
        Texture texture = new Texture(loader.loadTexture("image"));
        TexturedModel texturedModel = new TexturedModel(rawModel, texture);

        while (!DisplayManager.windowShouldClose()) {
            renderer.prepare();
            shader.start();
            renderer.render(texturedModel);
            shader.stop();
            DisplayManager.updateDisplay();
        }

        shader.clean();
        loader.clean();
        DisplayManager.closeDisplay();
    }

}
