package main;

import model.*;
import render_engine.*;
import utils.OBJLoader;
import utils.math.Vector2f;
import utils.math.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static utils.Constants.TERRAIN_SIZE;

public class GameLoop {

    public static void main(String[] args) {
        Random random = new Random();

        DisplayManager.createDisplay();

        ModelLoader loader = new ModelLoader();
        Renderer renderer = new Renderer(loader);

        Texture blendMap = new Texture(loader.loadTexture("blendMap"));
        Texture backgroundTexture = new Texture(loader.loadTexture("grass"));
        Texture rTexture = new Texture(loader.loadTexture("dirt"));
        Texture gTexture = new Texture(loader.loadTexture("pinkFlowers"));
        Texture bTexture = new Texture(loader.loadTexture("path"));
        TerrainTexturePack terrainTexturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
        Terrain terrain = new Terrain(0, -1, loader, terrainTexturePack, blendMap, "heightMap");

        RawModel rawDragon = OBJLoader.loadOBJModel("dragon", loader);
        Texture textureDragon = new Texture(loader.loadTexture("stallTexture"));
        textureDragon.setShineDamper(10);
        textureDragon.setReflectivity(1);
        TexturedModel texturedDragon = new TexturedModel(rawDragon, textureDragon);
        Player dragon = new Player(texturedDragon, new Vector3f(0, 0, -30), 0, 180, 0, 1);

        RawModel rawFern = OBJLoader.loadOBJModel("fern", loader);
        Texture fernTextureAtlas = new Texture(loader.loadTexture("fern"));
        fernTextureAtlas.setTransparent(true);
        fernTextureAtlas.setFakeLight(true);
        fernTextureAtlas.setRows(2);
        TexturedModel texturedFern = new TexturedModel(rawFern, fernTextureAtlas);
        List<Entity> ferns = new ArrayList<>();
        for (int i=0; i<250; i++) {
            float x = random.nextInt((int) TERRAIN_SIZE);
            float z = random.nextInt((int) TERRAIN_SIZE) * -1;
            ferns.add(new Entity(texturedFern, random.nextInt(4),
                    new Vector3f(x, terrain.getHeight(x, z), z), 0, 0, 0, 1));
        }

        Camera camera = new Camera(dragon);
        camera.setPitch(3);
        Light light = new Light(new Vector3f(100, 100, 100), new Vector3f(1, 1, 1));

        TextureGUI healthGUI = new TextureGUI(loader.loadTexture("health"), new Vector2f(-0.75f, 0.9f),
                new Vector2f(0.2f, 0.2f));

        while (!DisplayManager.windowShouldClose()) {
            camera.move();
            dragon.move(terrain);

            renderer.processTerrain(terrain);
            renderer.processEntity(dragon);
            for (Entity fern : ferns)
                renderer.processEntity(fern);
            renderer.processGUI(healthGUI);

            renderer.render(camera, light);
            DisplayManager.updateDisplay();
        }

        loader.clean();
        renderer.clean();
        DisplayManager.closeDisplay();
    }

}
