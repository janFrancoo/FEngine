package utils.input;

import org.lwjgl.glfw.GLFWCursorPosCallback;

public class CursorInput extends GLFWCursorPosCallback {

    public static double dX = 0;
    public static double dY = 0;
    public static double X = 0; // prevX
    public static double Y = 0; // prevY

    @Override
    public void invoke(long window, double xPos, double yPos) {
        dX = xPos - X;
        dY = yPos - Y;

        X = xPos;
        Y = yPos;
    }

}
