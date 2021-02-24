package utils.input;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

public class MouseButtonInput extends GLFWMouseButtonCallback {

    public static boolean isLeftButtonDown = false;
    public static boolean isRightButtonDown = false;

    @Override
    public void invoke(long window, int button, int action, int mods) {
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT)
            isLeftButtonDown = action == GLFW.GLFW_PRESS;

        if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT)
            isRightButtonDown = action == GLFW.GLFW_PRESS;
    }

}
