package render_engine;

import model.*;
import org.lwjgl.opengl.GL11;
import shader.*;
import utils.math.GameMath;
import utils.math.Matrix4f;
import utils.math.Vector3f;
import utils.math.Vector4f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static utils.Constants.*;

public class Renderer {

    private final ModelLoader loader;

    private final EntityRenderer entityRenderer;
    private final TerrainRenderer terrainRenderer;
    private final GUIRenderer guiRenderer;
    private final SkyboxRenderer skyboxRenderer;
    private final EntityShader entityShader;
    private final TerrainShader terrainShader;
    private final GUIShader guiShader;
    private final SkyboxShader skyboxShader;
    private final WaterShader waterShader;
    private final WaterRenderer waterRenderer;

    private final Map<TexturedModel, List<Entity>> entities = new HashMap<>();
    private final List<Terrain> terrains = new ArrayList<>();
    private final List<TextureGUI> guis = new ArrayList<>();
    private final List<WaterTile> waterTiles = new ArrayList<>();

    private final Matrix4f projectionMatrix;

    private float skyBoxRotation = 0;

    public Renderer(ModelLoader loader, WaterFrameBuffers waterFrameBuffers) {
        this.loader = loader;

        entityShader = new EntityShader();
        terrainShader = new TerrainShader();
        guiShader = new GUIShader();
        skyboxShader = new SkyboxShader();
        waterShader = new WaterShader();

        entityRenderer = new EntityRenderer(entityShader);
        terrainRenderer = new TerrainRenderer(terrainShader);
        guiRenderer = new GUIRenderer(guiShader, loader);
        skyboxRenderer = new SkyboxRenderer(skyboxShader, loader);
        waterRenderer = new WaterRenderer(waterShader, waterFrameBuffers, loader);

        enableCulling();

        projectionMatrix = GameMath.createProjectionMatrix();

        entityShader.start();
        entityShader.loadProjectionMatrix(projectionMatrix);
        entityShader.stop();

        terrainShader.start();
        terrainShader.loadProjectionMatrix(projectionMatrix);
        terrainShader.stop();

        skyboxShader.start();
        skyboxShader.loadProjectionMatrix(projectionMatrix);
        skyboxShader.stop();

        waterShader.start();
        waterShader.loadProjectionMatrix(projectionMatrix);
        waterShader.stop();
    }

    public static void enableCulling() {
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
    }

    public static void disableCulling() {
        GL11.glDisable(GL11.GL_CULL_FACE);
    }

    public void processEntity(Entity entity) {
        TexturedModel texturedModel = entity.getTexturedModel();
        List<Entity> batch = entities.get(texturedModel);
        if (batch != null)
            batch.add(entity);
        else {
            List<Entity> newBatch = new ArrayList<>();
            newBatch.add(entity);
            entities.put(texturedModel, newBatch);
        }
    }

    public void processTerrain(Terrain terrain) {
        terrains.add(terrain);
    }

    public void processGUI(TextureGUI gui) {
        guis.add(gui);
    }

    public void processWaterTile(WaterTile waterTile) {
        waterTiles.add(waterTile);
    }

    public void renderScene(List<Entity> entities, List<Terrain> terrains, Camera camera, List<Light> lights,
                            Vector4f clippingPlane) {
        for (Entity entity : entities)
            processEntity(entity);

        for (Terrain terrain : terrains)
            processTerrain(terrain);

        render(this.entities, terrains, camera, lights, clippingPlane);

        this.entities.clear();
        this.terrains.clear();
        this.guis.clear();
        this.waterTiles.clear();
    }

    public void renderScene(List<Entity> entities, List<Terrain> terrains, List<TextureGUI> guis,
                            List<WaterTile> waterTiles, WaterFrameBuffers waterFrameBuffers, Camera camera,
                            List<Light> lights, Vector4f clippingPlane) {
        for (Entity entity : entities)
            processEntity(entity);

        for (Terrain terrain : terrains)
            processTerrain(terrain);

        for (TextureGUI gui : guis)
            processGUI(gui);

        for (WaterTile waterTile : waterTiles)
            processWaterTile(waterTile);

        render(this.entities, terrains, guis, waterTiles, camera, lights, clippingPlane);

        this.entities.clear();
        this.terrains.clear();
        this.guis.clear();
        this.waterTiles.clear();
    }

    public void render(Map<TexturedModel, List<Entity>> entities, List<Terrain> terrains, Camera camera,
                       List<Light> lights, Vector4f clippingPlane) {
        prepare();
        Matrix4f viewMatrix = GameMath.createViewMatrix(camera);

        entityShader.start();
        entityShader.loadClippingPlane(clippingPlane);
        entityShader.loadLights(lights);
        entityShader.loadViewMatrix(viewMatrix);
        entityRenderer.render(entities);
        entityShader.stop();

        terrainShader.start();
        terrainShader.loadClippingPlane(clippingPlane);
        terrainShader.loadLights(lights);
        terrainShader.loadViewMatrix(viewMatrix);
        terrainRenderer.render(terrains);
        terrainShader.stop();

        skyboxShader.start();
        // Disabling translation
        viewMatrix.m30 = 0;
        viewMatrix.m31 = 0;
        viewMatrix.m32 = 0;
        skyBoxRotation += SKYBOX_ROTATE_SPEED * DisplayManager.getDelta();
        Matrix4f.rotate((float) Math.toRadians(skyBoxRotation), new Vector3f(0, 1, 0), viewMatrix, viewMatrix);
        skyboxShader.loadViewMatrix(viewMatrix);
        skyboxRenderer.render();
        skyboxShader.stop();
    }

    public void render(Map<TexturedModel, List<Entity>> entities, List<Terrain> terrains, List<TextureGUI> guis,
                       List<WaterTile> waterTiles, Camera camera,
                       List<Light> lights, Vector4f clippingPlane) {
        prepare();
        Matrix4f viewMatrix = GameMath.createViewMatrix(camera);

        entityShader.start();
        entityShader.loadClippingPlane(clippingPlane);
        entityShader.loadLights(lights);
        entityShader.loadViewMatrix(viewMatrix);
        entityRenderer.render(entities);
        entityShader.stop();

        terrainShader.start();
        terrainShader.loadClippingPlane(clippingPlane);
        terrainShader.loadLights(lights);
        terrainShader.loadViewMatrix(viewMatrix);
        terrainRenderer.render(terrains);
        terrainShader.stop();

        waterShader.start();
        waterShader.loadViewMatrix(camera, viewMatrix);
        waterShader.connectTextures();
        waterRenderer.render(waterTiles);
        waterShader.stop();

        skyboxShader.start();
        // Disabling translation
        viewMatrix.m30 = 0;
        viewMatrix.m31 = 0;
        viewMatrix.m32 = 0;
        skyBoxRotation += SKYBOX_ROTATE_SPEED * DisplayManager.getDelta();
        Matrix4f.rotate((float) Math.toRadians(skyBoxRotation), new Vector3f(0, 1, 0), viewMatrix, viewMatrix);
        skyboxShader.loadViewMatrix(viewMatrix);
        skyboxRenderer.render();
        skyboxShader.stop();

        guiShader.start();
        guiRenderer.render(guis);
        guiShader.stop();
    }

    private void prepare() {
        GL11.glClearColor(SKY_COLOR.x, SKY_COLOR.y, SKY_COLOR.z, 1);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    public void clean() {
        loader.clean();
        entityShader.clean();
        terrainShader.clean();
        guiShader.clean();
        skyboxShader.clean();
        waterShader.clean();
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

}
