package render_engine;

import model.*;
import org.lwjgl.opengl.GL11;
import shader.EntityShader;
import shader.GUIShader;
import shader.TerrainShader;
import utils.math.Matrix4f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static utils.Constants.*;

public class Renderer {

    private final EntityRenderer entityRenderer;
    private final TerrainRenderer terrainRenderer;
    private final GUIRenderer guiRenderer;
    private final EntityShader entityShader;
    private final TerrainShader terrainShader;
    private final GUIShader guiShader;

    private final Map<TexturedModel, List<Entity>> entities = new HashMap<>();
    private final List<Terrain> terrains = new ArrayList<>();
    private final List<TextureGUI> guis = new ArrayList<>();

    public Renderer(ModelLoader loader) {
        entityShader = new EntityShader();
        terrainShader = new TerrainShader();
        guiRenderer = new GUIRenderer(loader);
        entityRenderer = new EntityRenderer(entityShader);
        terrainRenderer = new TerrainRenderer(terrainShader);
        guiShader = new GUIShader();

        enableCulling();

        Matrix4f projectionMatrix = Matrix4f.createProjectionMatrix();

        entityShader.start();
        entityShader.loadProjectionMatrix(projectionMatrix);
        entityShader.stop();

        terrainShader.start();
        terrainShader.loadProjectionMatrix(projectionMatrix);
        terrainShader.stop();
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

    public void render(Camera camera, Light light) {
        prepare();
        Matrix4f viewMatrix = Matrix4f.createViewMatrix(camera);

        entityShader.start();
        entityShader.loadLight(light);
        entityShader.loadViewMatrix(viewMatrix);
        entityRenderer.render(entities);
        entityShader.stop();

        terrainShader.start();
        terrainShader.loadLight(light);
        terrainShader.loadViewMatrix(viewMatrix);
        terrainRenderer.render(terrains);
        terrainShader.stop();

        guiShader.start();
        guiRenderer.render(guis);
        guiShader.stop();

        entities.clear();
        terrains.clear();
    }

    private void prepare() {
        GL11.glClearColor(SKY_COLOR.x, SKY_COLOR.y, SKY_COLOR.z, 1);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    public void clean() {
        entityShader.clean();
        terrainShader.clean();
        guiShader.clean();
    }

}
