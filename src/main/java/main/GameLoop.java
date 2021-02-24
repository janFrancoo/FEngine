package main;

import model.*;
import render_engine.*;
import utils.OBJLoader;
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
        Renderer renderer = new Renderer();

        RawModel rawDragon = OBJLoader.loadOBJModel("dragon", loader);
        Texture textureDragon = new Texture(loader.loadTexture("stallTexture"));
        textureDragon.setShineDamper(10);
        textureDragon.setReflectivity(1);
        TexturedModel texturedDragon = new TexturedModel(rawDragon, textureDragon);
        Player dragon = new Player(texturedDragon, new Vector3f(0, 0, -30), 0, 0, 0, 1);

        RawModel rawFern = OBJLoader.loadOBJModel("fern", loader);
        Texture textureFern = new Texture(loader.loadTexture("fern"));
        textureFern.setTransparent(true);
        textureFern.setFakeLight(true);
        TexturedModel texturedFern = new TexturedModel(rawFern, textureFern);
        List<Entity> ferns = new ArrayList<>();
        for (int i=0; i<250; i++)
            ferns.add(new Entity(
                    texturedFern,
                    new Vector3f(random.nextInt((int) TERRAIN_SIZE), 0, -random.nextInt((int) TERRAIN_SIZE)),
                    0, 0, 0, 1));

        RawModel rawGrass = OBJLoader.loadOBJModel("grassModel", loader);
        Texture textureGrass = new Texture(loader.loadTexture("grassTexture"));
        textureGrass.setTransparent(true);
        textureGrass.setFakeLight(true);
        TexturedModel texturedGrass = new TexturedModel(rawGrass, textureGrass);
        List<Entity> grasses = new ArrayList<>();
        for (int i=0; i<250; i++)
            grasses.add(new Entity(
                    texturedGrass,
                    new Vector3f(random.nextInt((int) TERRAIN_SIZE), 0, -random.nextInt((int) TERRAIN_SIZE)),
                    0, 0, 0, 5));

        Texture blendMap = new Texture(loader.loadTexture("blendMap"));
        Texture backgroundTexture = new Texture(loader.loadTexture("grass"));
        Texture rTexture = new Texture(loader.loadTexture("dirt"));
        Texture gTexture = new Texture(loader.loadTexture("pinkFlowers"));
        Texture bTexture = new Texture(loader.loadTexture("path"));
        TerrainTexturePack terrainTexturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
        Terrain terrain = new Terrain(0, -1, loader, terrainTexturePack, blendMap);

        Camera camera = new Camera(dragon);
        camera.setPitch(3);
        Light light = new Light(new Vector3f(0, 500, 0), new Vector3f(1, 1, 1));

        while (!DisplayManager.windowShouldClose()) {
            camera.move();
            dragon.move();

            renderer.processTerrain(terrain);
            renderer.processEntity(dragon);
            for (Entity fern : ferns)
                renderer.processEntity(fern);
            for (Entity grass : grasses)
                renderer.processEntity(grass);

            renderer.render(camera, light);
            DisplayManager.updateDisplay();
        }

        loader.clean();
        renderer.clean();
        DisplayManager.closeDisplay();
    }

}
