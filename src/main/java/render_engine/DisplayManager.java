package render_engine;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.system.MemoryStack;
import utils.Constants;
import utils.input.CursorInput;
import utils.input.KeyInput;
import utils.input.MouseButtonInput;
import utils.input.ScrollInput;

import java.nio.IntBuffer;
import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.*;

public class DisplayManager {

    private static long window;
    private static double prevTime = glfwGetTime();
    private static double delta;

    public static void createDisplay() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit())
            throw new IllegalStateException("Unable to init GLFW");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

        window = glfwCreateWindow(Constants.WIDTH, Constants.HEIGHT, "Hello World!", NULL, NULL);
        if (window == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            glfwGetWindowSize(window, pWidth, pHeight);
            GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            assert vidMode != null;
            glfwSetWindowPos(window, (vidMode.width() - pWidth.get(0)) / 2,
                    (vidMode.height() - pHeight.get(0)) / 2);
        }

        glfwSetKeyCallback(window, new KeyInput());
        glfwSetMouseButtonCallback(window, new MouseButtonInput());
        glfwSetCursorPosCallback(window, new CursorInput());
        glfwSetScrollCallback(window, new ScrollInput());
        glfwSetWindowTitle(window, Constants.TITLE);
        glfwMakeContextCurrent(window);
        glfwSwapInterval(1); // v-sync enable
        glfwShowWindow(window);
        // glfwWindowHint(GLFW_SAMPLES, 8);

        GL.createCapabilities();
        GL11.glEnable(GL13.GL_MULTISAMPLE);
    }

    public static void updateDisplay() {
        glfwSwapBuffers(window);
        glfwPollEvents();

        double currentTime = glfwGetTime();
        delta = currentTime - prevTime;
        prevTime = currentTime;

        calculateFPS();
    }

    public static boolean windowShouldClose() {
        return glfwWindowShouldClose(window);
    }

    private static void calculateFPS() {
        glfwSetWindowTitle(window, Constants.TITLE + " FPS: " + Math.round(1f / delta));
    }

    public static double getDelta() {
        return delta;
    }

    public static void closeDisplay() {
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();
    }

}
