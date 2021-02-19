package main;

import model.*;
import render_engine.DisplayManager;
import render_engine.ModelLoader;
import render_engine.Renderer;
import shader.StaticShader;
import utils.OBJLoader;
import utils.math.Vector3f;

public class GameLoop {

    public static void main(String[] args) {
        DisplayManager.createDisplay();

        ModelLoader loader = new ModelLoader();
        StaticShader shader = new StaticShader("vertexShader", "fragmentShader");
        Renderer renderer = new Renderer(shader);

        RawModel rawModel = OBJLoader.loadOBJModel("dragon", loader);
        Texture texture = new Texture(loader.loadTexture("stallTexture"));
        texture.setShineDamper(10);
        texture.setReflectivity(1);
        TexturedModel texturedModel = new TexturedModel(rawModel, texture);
        Entity entity = new Entity(texturedModel, new Vector3f(0, 0, -30), 0, 0, 0, 1);
        renderer.processEntity(entity);

        Camera camera = new Camera();
        Light light = new Light(new Vector3f(0, 0, -20), new Vector3f(1, 1, 1));

        while (!DisplayManager.windowShouldClose()) {
            entity.increaseRotation(0, 1, 0);
            camera.move();
            renderer.render(camera, light);
            DisplayManager.updateDisplay();
        }

        shader.clean();
        loader.clean();
        renderer.clean();
        DisplayManager.closeDisplay();
    }

}
