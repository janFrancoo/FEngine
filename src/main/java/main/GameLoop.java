package main;

import model.Camera;
import model.Entity;
import model.TexturedModel;
import render_engine.DisplayManager;
import render_engine.ModelLoader;
import model.RawModel;
import render_engine.Renderer;
import shader.StaticShader;
import model.Texture;
import utils.OBJLoader;
import utils.math.Matrix4f;
import utils.math.Vector3f;

public class GameLoop {

    public static void main(String[] args) {
        DisplayManager.createDisplay();

        ModelLoader loader = new ModelLoader();
        StaticShader shader = new StaticShader("vertexShader", "fragmentShader");
        Renderer renderer = new Renderer(shader);

        RawModel rawModel = OBJLoader.loadOBJModel("stall", loader);
        Texture texture = new Texture(loader.loadTexture("stallTexture"));
        TexturedModel texturedModel = new TexturedModel(rawModel, texture);
        Entity entity = new Entity(texturedModel, new Vector3f(0, 0, -30), 0, 0, 0, 1);

        Camera camera = new Camera();

        while (!DisplayManager.windowShouldClose()) {
            entity.increaseRotation(0, 1, 0);
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
