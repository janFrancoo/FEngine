package model;

import org.lwjgl.glfw.GLFW;
import utils.Constants;
import utils.Input;
import utils.math.Vector3f;

public class Camera {

    private Vector3f position;
    private float pitch, yaw, roll;

    public Camera() {
        this.position = new Vector3f();
    }

    public void move() {
        if (Input.isKeyDown(GLFW.GLFW_KEY_W))
            position.z -= Constants.GAME_SPEED;
        if (Input.isKeyDown(GLFW.GLFW_KEY_S))
            position.z += Constants.GAME_SPEED;
        if (Input.isKeyDown(GLFW.GLFW_KEY_A))
            position.x -= Constants.GAME_SPEED;
        if (Input.isKeyDown(GLFW.GLFW_KEY_D))
            position.x += Constants.GAME_SPEED;
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getRoll() {
        return roll;
    }

}
