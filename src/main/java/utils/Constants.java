package utils;

import utils.math.Vector3f;

public class Constants {
    // Folders & files
    public static final String RES_FOLDER = "res";
    private static final String SHADER_FOLDER = "C:\\Users\\PC\\Documents\\Java Projects\\FEngine\\src\\main\\java\\shader\\";
    public static final String VERTEX_SHADER_FILE = SHADER_FOLDER + "vertexShader.txt";
    public static final String FRAGMENT_SHADER_FILE = SHADER_FOLDER + "fragmentShader.txt";
    public static final String TERRAIN_VERTEX_SHADER_FILE = SHADER_FOLDER + "terrainVertexShader.txt";
    public static final String TERRAIN_FRAGMENT_SHADER_FILE = SHADER_FOLDER + "terrainFragmentShader.txt";
    public static final String GUI_VERTEX_SHADER_FILE = SHADER_FOLDER + "guiVertexShader.txt";
    public static final String GUI_FRAGMENT_SHADER_FILE = SHADER_FOLDER + "guiFragmentShader.txt";
    public static final String SKYBOX_VERTEX_SHADER_FILE = SHADER_FOLDER + "skyboxVertexShader.txt";
    public static final String SKYBOX_FRAGMENT_SHADER_FILE = SHADER_FOLDER + "skyboxFragmentShader.txt";
    public static final String WATER_VERTEX_SHADER_FILE = SHADER_FOLDER + "waterVertexShader.txt";
    public static final String WATER_FRAGMENT_SHADER_FILE = SHADER_FOLDER + "waterFragmentShader.txt";
    public static final String NM_VERTEX_SHADER_FILE = SHADER_FOLDER + "nmVertexShader.txt";
    public static final String NM_FRAGMENT_SHADER_FILE = SHADER_FOLDER + "nmFragmentShader.txt";
    public static final String FONT_VERTEX_SHADER_FILE = SHADER_FOLDER + "fontVertexShader.txt";
    public static final String FONT_FRAGMENT_SHADER_FILE = SHADER_FOLDER + "fontFragmentShader.txt";
    public static final String PARTICLE_VERTEX_SHADER_FILE = SHADER_FOLDER + "particleVertexShader.txt";
    public static final String PARTICLE_FRAGMENT_SHADER_FILE = SHADER_FOLDER + "particleFragmentShader.txt";
    // Game Settings
    // Window
    public static final int WIDTH = 2048;
    public static final int HEIGHT = 1536;
    public static final String TITLE = "FEngine";
    // Scene
    public static final float FOV = 70;
    public static final float NEAR_PLANE = 0.1f;
    public static final float FAR_PLANE = 1000;
    public static final int REFLECTION_WIDTH = 320;
    public static final int REFLECTION_HEIGHT = 180;
    public static final int REFRACTION_WIDTH = 1280;
    public static final int REFRACTION_HEIGHT = 720;
    public static final float LOD_BIAS = -2.0f;
    // Terrain
    public static final float TERRAIN_SIZE = 800;
    public static final float TERRAIN_MAX_HEIGHT = 80;
    public static final float MAX_PIXEL_COLOR = 256 * 256 * 256;
    public static final float TILE_SIZE = 60;
    public static final String DUDV_MAP = "waterDUDV";
    public static final String NORMAL_MAP = "normalMap";
    public static final float WAVE_SPEED = 0.03f;
    // Light
    public static final int MAX_LIGHT = 4;
    public static final float CEL_LEVEL = 3f;
    public static final float SKY_CEL_LEVEL = 10f;
    // Fog
    public static final float FOG_DENSITY = 0.005f;
    public static final float FOG_GRADIENT = 1.3f;
    public static final Vector3f SKY_COLOR = new Vector3f(0.5444f, 0.62f, 0.69f);
    // Skybox
    public static final float SKYBOX_ROTATE_SPEED = 1f;
    // Player
    public static final float RUN_SPEED = 20;
    public static final float TURN_SPEED = 40;
    public static final float JUMP_POWER = 30;
    public static final float GRAVITY = -50;
    // Font
    public static final double LINE_HEIGHT = 0.03f;
    public static final int SPACE_ASCII = 32;
    // Particle
    public static final int MAX_PARTICLE_INSTANCE = 10000;
}
