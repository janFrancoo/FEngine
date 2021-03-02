package utils;

import utils.math.Vector3f;

public class Constants {
    // Window
    public static final int WIDTH = 2048;
    public static final int HEIGHT = 1536;
    public static final String TITLE = "FEngine";

    // Folders & files
    public static final String VERTEX_SHADER_FILE = "C:\\Users\\PC\\Documents\\Java Projects\\FEngine\\src\\main" +
            "\\java\\shader\\vertexShader.txt";
    public static final String FRAGMENT_SHADER_FILE = "C:\\Users\\PC\\Documents\\Java Projects\\FEngine\\src\\main" +
            "\\java\\shader\\fragmentShader.txt";
    public static final String TERRAIN_VERTEX_SHADER_FILE = "C:\\Users\\PC\\Documents\\Java Projects\\FEngine\\src\\main" +
            "\\java\\shader\\terrainVertexShader.txt";
    public static final String TERRAIN_FRAGMENT_SHADER_FILE = "C:\\Users\\PC\\Documents\\Java Projects\\FEngine\\src\\main" +
            "\\java\\shader\\terrainFragmentShader.txt";
    public static final String GUI_VERTEX_SHADER_FILE = "C:\\Users\\PC\\Documents\\Java Projects\\FEngine\\src\\main" +
            "\\java\\shader\\guiVertexShader.txt";
    public static final String GUI_FRAGMENT_SHADER_FILE = "C:\\Users\\PC\\Documents\\Java Projects\\FEngine\\src\\main" +
            "\\java\\shader\\guiFragmentShader.txt";
    public static final String SKYBOX_VERTEX_SHADER_FILE = "C:\\Users\\PC\\Documents\\Java Projects\\FEngine\\src\\main" +
            "\\java\\shader\\skyboxVertexShader.txt";
    public static final String SKYBOX_FRAGMENT_SHADER_FILE = "C:\\Users\\PC\\Documents\\Java Projects\\FEngine\\src\\main" +
            "\\java\\shader\\skyboxFragmentShader.txt";
    public static final String RES_FOLDER = "res";

    // Scene
    public static final float FOV = 70;
    public static final float NEAR_PLANE = 0.1f;
    public static final float FAR_PLANE = 1000;

    // Game Settings
    // Terrain
    public static final float TERRAIN_SIZE = 800;
    public static final float TERRAIN_MAX_HEIGHT = 80;
    public static final float MAX_PIXEL_COLOR = 256 * 256 * 256;
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
}
