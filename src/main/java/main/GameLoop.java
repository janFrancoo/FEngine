package main;

import model.*;
import render_engine.*;
import utils.OBJLoader;
import utils.math.Vector3f;

public class GameLoop {

    public static void main(String[] args) {
        DisplayManager.createDisplay();

        ModelLoader loader = new ModelLoader();
        Renderer renderer = new Renderer();

        RawModel rawModel = OBJLoader.loadOBJModel("dragon", loader);
        Texture texture = new Texture(loader.loadTexture("stallTexture"));
        texture.setShineDamper(10);
        texture.setReflectivity(1);
        TexturedModel texturedModel = new TexturedModel(rawModel, texture);
        Entity entity = new Entity(texturedModel, new Vector3f(0, 0, -30), 0, 0, 0, 1);

        Terrain terrain = new Terrain(0, -1, loader, new Texture(loader.loadTexture("grass")));

        Camera camera = new Camera(new Vector3f(0, 20, 0));
        camera.setPitch(20);
        Light light = new Light(new Vector3f(0, 100, 0), new Vector3f(1, 1, 1));

        while (!DisplayManager.windowShouldClose()) {
            camera.move();

            renderer.processTerrain(terrain);
            renderer.processEntity(entity);

            renderer.render(camera, light);
            DisplayManager.updateDisplay();
        }

        loader.clean();
        renderer.clean();
        DisplayManager.closeDisplay();
    }

}
