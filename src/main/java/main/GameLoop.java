package main;

import model.Camera;
import model.Entity;
import model.TexturedModel;
import render_engine.DisplayManager;
import render_engine.Loader;
import model.RawModel;
import render_engine.Renderer;
import shader.StaticShader;
import texture.Texture;
import utils.math.Matrix4f;
import utils.math.Vector3f;

public class GameLoop {

    public static void main(String[] args) {
        DisplayManager.createDisplay();

        Loader loader = new Loader();
        StaticShader shader = new StaticShader("vertexShader", "fragmentShader");
        Renderer renderer = new Renderer(shader);

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
        Entity entity = new Entity(texturedModel, new Vector3f(0, 0, -5), 0, 0, 0, 1);

        Camera camera = new Camera();

        while (!DisplayManager.windowShouldClose()) {
            camera.move();
            renderer.prepare();
            shader.start();
            Matrix4f viewMatrix = Matrix4f.createViewMatrix(camera);
            shader.loadViewMatrix(viewMatrix);
            renderer.render(entity);
            shader.stop();
            DisplayManager.updateDisplay();
        }

        shader.clean();
        loader.clean();
        DisplayManager.closeDisplay();
    }

}
