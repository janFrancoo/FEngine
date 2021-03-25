package main;

import model.*;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import render_engine.*;
import utils.font.FontType;
import utils.input.KeyInput;
import utils.loader.OBJLoader;
import utils.math.Vector2f;
import utils.math.Vector3f;
import utils.math.Vector4f;
import utils.particle.ParticleSystem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GameLoop {

    public static void main(String[] args) {
        DisplayManager.createDisplay();

        ModelLoader loader = new ModelLoader();
        WaterFrameBuffers frameBufferObjects = new WaterFrameBuffers();
        Renderer renderer = new Renderer(loader, frameBufferObjects);

        List<Entity> entities = new ArrayList<>();
        List<Entity> nmEntities = new ArrayList<>();
        List<Terrain> terrains = new ArrayList<>();
        List<TextureGUI> guis = new ArrayList<>();
        List<TextGUI> texts = new ArrayList<>();
        List<WaterTile> waterTiles = new ArrayList<>();

        Texture blendMap = new Texture(loader.loadTexture("blendMap"));
        Texture backgroundTexture = new Texture(loader.loadTexture("grass"));
        Texture rTexture = new Texture(loader.loadTexture("dirt"));
        Texture gTexture = new Texture(loader.loadTexture("pinkFlowers"));
        Texture bTexture = new Texture(loader.loadTexture("path"));
        TerrainTexturePack terrainTexturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
        Terrain terrain = new Terrain(0, -1, loader, terrainTexturePack, blendMap, "heightMap");
        terrains.add(terrain);

        RawModel rawDragon = OBJLoader.loadOBJModel("dragon", loader);
        Texture textureDragon = new Texture(loader.loadTexture("stallTexture"));
        textureDragon.setShineDamper(10);
        textureDragon.setReflectivity(1);
        TexturedModel texturedDragon = new TexturedModel(rawDragon, textureDragon);
        Player dragon = new Player(texturedDragon, new Vector3f(0, 0, -30), 0, 180, 0, 1);
        entities.add(dragon);

        Camera camera = new Camera(dragon);
        camera.setPitch(3);
        List<Light> lights = new ArrayList<>();
        lights.add(new Light(new Vector3f(0, 10000, -7000), new Vector3f(1, 1, 1))); // Sun
        lights.add(new Light(new Vector3f(200, 10, -300), new Vector3f(2, 0, 0),
                new Vector3f(1, 0.01f, 0.002f)));
        lights.add(new Light(new Vector3f(350, 20, -300), new Vector3f(0, 2, 2),
                new Vector3f(1, 0.01f, 0.002f)));
        lights.add(new Light(new Vector3f(300, 5, -300), new Vector3f(2, 2, 0),
                new Vector3f(1, 0.01f, 0.002f)));

        TextureGUI healthGUI = new TextureGUI(loader.loadTexture("health"), new Vector2f(-0.75f, 0.9f),
                new Vector2f(0.2f, 0.2f));
        guis.add(healthGUI);

        WaterTile waterTile = new WaterTile(150, -300, 0);
        waterTiles.add(waterTile);

        FontType font = new FontType(loader.loadTexture("comic_sans_ms"),
                new File("res/comic_sans_ms.fnt"));
        TextGUI text = new TextGUI("TEST", 5, font, new Vector2f(0, 0.5f),
                1f, true, loader);
        text.setColor(1, 0 ,0);
        texts.add(text);

        TextureParticle particleTexture = new TextureParticle(loader.loadTexture("cosmic"), 4);
        ParticleSystem particleSystem = new ParticleSystem(particleTexture, 50, 25, 0.3f, 4);

        // MousePicker mousePicker = new MousePicker(camera, renderer.getProjectionMatrix());

        while (!DisplayManager.windowShouldClose()) {
            dragon.move(terrain);
            camera.move();

            // mousePicker.update();
            // System.out.println(mousePicker.getCurrentRay());

            if (KeyInput.isKeyDown(GLFW.GLFW_KEY_P))
                particleSystem.generateParticles(dragon.getPosition());

            GL11.glEnable(GL30.GL_CLIP_DISTANCE0);

            frameBufferObjects.bindReflectionFrameBuffer();
            float camDistance = 2 * (camera.getPosition().y - waterTile.getHeight());
            camera.getPosition().y -= camDistance;
            camera.invertPitch();
            renderer.renderScene(entities, terrains, camera, lights, new Vector4f(0, 1, 0,
                    -waterTile.getHeight() + 1.0f));
            camera.getPosition().y += camDistance;
            camera.invertPitch();

            frameBufferObjects.bindRefractionFrameBuffer();
            renderer.renderScene(entities, terrains, camera, lights, new Vector4f(0, -1, 0,
                    waterTile.getHeight() + 1.0f));

            GL11.glDisable(GL30.GL_CLIP_DISTANCE0);

            frameBufferObjects.unbindCurrentFrameBuffer();
            renderer.renderScene(entities, nmEntities, terrains, guis, texts, waterTiles, camera, lights,
                    new Vector4f(0, -1, 0, 100000));

            DisplayManager.updateDisplay();
        }

        frameBufferObjects.clean();
        renderer.clean();
        DisplayManager.closeDisplay();
    }

}
