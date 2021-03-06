package render_engine;

import model.*;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import shader.*;
import utils.font.FontType;
import utils.math.GameMath;
import utils.math.Matrix4f;
import utils.math.Vector3f;
import utils.math.Vector4f;
import utils.particle.ParticleManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static utils.Constants.*;

public class Renderer {

    private final ModelLoader loader;

    private final EntityRenderer entityRenderer;
    private final NMRenderer nmRenderer;
    private final TerrainRenderer terrainRenderer;
    private final GUIRenderer guiRenderer;
    private final FontRenderer fontRenderer;
    private final SkyboxRenderer skyboxRenderer;
    private final ParticleShader particleShader;
    private final ShadowShader shadowShader;

    private final EntityShader entityShader;
    private final NMShader nmShader;
    private final TerrainShader terrainShader;
    private final GUIShader guiShader;
    private final FontShader fontShader;
    private final SkyboxShader skyboxShader;
    private final WaterShader waterShader;
    private final WaterRenderer waterRenderer;
    private final ParticleRenderer particleRenderer;
    private final ShadowRenderer shadowRenderer;

    private final Map<TexturedModel, List<Entity>> entities = new HashMap<>();
    private final Map<TexturedModel, List<Entity>> nmEntities = new HashMap<>();
    private final List<Terrain> terrains = new ArrayList<>();
    private final List<TextureGUI> guis = new ArrayList<>();
    private final Map<FontType, List<TextGUI>> texts = new HashMap<>();
    private final List<WaterTile> waterTiles = new ArrayList<>();

    private final Matrix4f projectionMatrix;

    private float skyBoxRotation = 0;

    public Renderer(ModelLoader loader, WaterFrameBuffers waterFrameBuffers, Camera camera) {
        this.loader = loader;

        entityShader = new EntityShader();
        nmShader = new NMShader();
        terrainShader = new TerrainShader();
        guiShader = new GUIShader();
        fontShader = new FontShader();
        skyboxShader = new SkyboxShader();
        waterShader = new WaterShader();
        particleShader = new ParticleShader();
        shadowShader = new ShadowShader();

        entityRenderer = new EntityRenderer(entityShader);
        nmRenderer = new NMRenderer(nmShader);
        terrainRenderer = new TerrainRenderer(terrainShader);
        guiRenderer = new GUIRenderer(guiShader, loader);
        fontRenderer = new FontRenderer(fontShader);
        skyboxRenderer = new SkyboxRenderer(skyboxShader, loader);
        waterRenderer = new WaterRenderer(waterShader, waterFrameBuffers, loader);
        particleRenderer = new ParticleRenderer(loader, particleShader);
        shadowRenderer = new ShadowRenderer(shadowShader, camera);

        enableCulling();

        projectionMatrix = GameMath.createProjectionMatrix();

        entityShader.start();
        entityShader.loadProjectionMatrix(projectionMatrix);
        entityShader.stop();

        nmShader.start();
        nmShader.loadProjectionMatrix(projectionMatrix);
        nmShader.stop();

        terrainShader.start();
        terrainShader.loadProjectionMatrix(projectionMatrix);
        terrainShader.stop();

        skyboxShader.start();
        skyboxShader.loadProjectionMatrix(projectionMatrix);
        skyboxShader.stop();

        waterShader.start();
        waterShader.loadProjectionMatrix(projectionMatrix);
        waterShader.stop();

        particleShader.start();
        particleShader.loadProjectionMatrix(projectionMatrix);
        particleShader.stop();
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

    public void processNMEntity(Entity entity) {
        TexturedModel texturedModel = entity.getTexturedModel();
        List<Entity> batch = nmEntities.get(texturedModel);
        if (batch != null)
            batch.add(entity);
        else {
            List<Entity> newBatch = new ArrayList<>();
            newBatch.add(entity);
            nmEntities.put(texturedModel, newBatch);
        }
    }

    public void processTerrain(Terrain terrain) {
        terrains.add(terrain);
    }

    public void processGUI(TextureGUI gui) {
        guis.add(gui);
    }

    public void processText(TextGUI text) {
        FontType font = text.getFont();
        List<TextGUI> textBatch = texts.computeIfAbsent(font, k -> new ArrayList<>());
        textBatch.add(text);
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

    public void renderScene(List<Entity> nmEntities, List<Terrain> terrains, List<WaterTile> waterTiles, Camera camera,
                            List<Light> lights, Vector4f clippingPlane) {
        for (Entity nmEntity : nmEntities)
            processNMEntity(nmEntity);

        for (Terrain terrain : terrains)
            processTerrain(terrain);

        for (WaterTile waterTile : waterTiles)
            processWaterTile(waterTile);

        render(this.entities, this.nmEntities, this.terrains, this.waterTiles, camera, lights, clippingPlane);

        this.entities.clear();
        this.nmEntities.clear();
        this.terrains.clear();
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
        GL13.glActiveTexture(GL13.GL_TEXTURE5);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, getShadowMapTexture());
        terrainRenderer.render(terrains, shadowRenderer.getToShadowMapSpaceMatrix());
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

    public void render(Map<TexturedModel, List<Entity>> entities, Map<TexturedModel, List<Entity>> nmEntities,
                       List<Terrain> terrains, List<WaterTile> waterTiles, Camera camera, List<Light> lights,
                       Vector4f clippingPlane) {
        prepare();
        Matrix4f viewMatrix = GameMath.createViewMatrix(camera);

        entityShader.start();
        entityShader.loadClippingPlane(clippingPlane);
        entityShader.loadLights(lights);
        entityShader.loadViewMatrix(viewMatrix);
        entityRenderer.render(entities);
        entityShader.stop();

        nmShader.start();
        nmShader.loadClippingPlane(clippingPlane);
        nmShader.loadLights(lights, viewMatrix);
        nmShader.loadViewMatrix(viewMatrix);
        nmRenderer.render(nmEntities);
        nmShader.stop();

        terrainShader.start();
        terrainShader.loadClippingPlane(clippingPlane);
        terrainShader.loadLights(lights);
        terrainShader.loadViewMatrix(viewMatrix);
        GL13.glActiveTexture(GL13.GL_TEXTURE5);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, getShadowMapTexture());
        terrainRenderer.render(terrains, shadowRenderer.getToShadowMapSpaceMatrix());
        terrainShader.stop();

        waterShader.start();
        waterShader.loadFogSettings(FOG_DENSITY, FOG_GRADIENT, SKY_COLOR);
        waterShader.loadViewMatrix(camera, viewMatrix);
        waterShader.connectTextures();
        waterShader.loadLight(lights.get(0));
        waterRenderer.render(waterTiles);
        waterShader.stop();

        skyboxShader.start();
        // Disabling translation
        Matrix4f viewMatrixForSkyboxShader = (Matrix4f) viewMatrix.clone();
        viewMatrixForSkyboxShader.m30 = 0;
        viewMatrixForSkyboxShader.m31 = 0;
        viewMatrixForSkyboxShader.m32 = 0;
        skyBoxRotation += SKYBOX_ROTATE_SPEED * DisplayManager.getDelta();
        Matrix4f.rotate((float) Math.toRadians(skyBoxRotation), new Vector3f(0, 1, 0),
                viewMatrixForSkyboxShader, viewMatrixForSkyboxShader);
        skyboxShader.loadViewMatrix(viewMatrixForSkyboxShader);
        skyboxRenderer.render();
        skyboxShader.stop();

        ParticleManager.update(camera);
        particleRenderer.render(ParticleManager.particles, viewMatrix);
    }

    public void renderShadows(List<Entity> entities, List<Light> lights) {
        for (Entity entity : entities)
            processEntity(entity);

        shadowShader.start();
        shadowRenderer.render(this.entities, lights.get(0));
        shadowShader.stop();
    }

    public void renderGuisAndTexts(List<TextureGUI> guis, List<TextGUI> texts) {
        for (TextureGUI gui : guis)
            processGUI(gui);

        for (TextGUI text : texts)
            processText(text);

        guiShader.start();
        guiRenderer.render(guis);
        guiShader.stop();

        fontShader.start();
        fontRenderer.render(this.texts);
        fontShader.stop();

        this.guis.clear();
        this.texts.clear();
    }

    private void prepare() {
        GL11.glClearColor(SKY_COLOR.x, SKY_COLOR.y, SKY_COLOR.z, 1);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    public void clean() {
        loader.clean();
        entityShader.clean();
        nmShader.clean();
        terrainShader.clean();
        guiShader.clean();
        fontShader.clean();
        skyboxShader.clean();
        waterShader.clean();
        particleShader.clean();
        ParticleManager.clean();
        shadowRenderer.clean();
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public int getShadowMapTexture() {
        return shadowRenderer.getShadowMap();
    }

}
