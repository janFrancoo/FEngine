package main;

import render_engine.DisplayManager;
import render_engine.Loader;
import render_engine.RawModel;
import render_engine.Renderer;
import shader.StaticShader;

public class GameLoop {

    public static void main(String[] args) {
        DisplayManager.createDisplay();

        Loader loader = new Loader();
        Renderer renderer = new Renderer();
        StaticShader shader = new StaticShader();

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

        RawModel model = loader.loadToVao(vertices, indices);

        while (!DisplayManager.windowShouldClose()) {
            renderer.prepare();
            shader.start();
            renderer.render(model);
            shader.stop();
            DisplayManager.updateDisplay();
        }

        shader.clean();
        loader.clean();
        DisplayManager.closeDisplay();
    }

}
