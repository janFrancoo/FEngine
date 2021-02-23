package model;

import org.lwjgl.glfw.GLFW;
import utils.Constants;
import utils.Input;
import utils.math.Vector3f;

public class Camera {

    private final Vector3f position;
    private float pitch, yaw, roll;

    public Camera() {
        this.position = new Vector3f();
    }

    public Camera(Vector3f position) {
        this.position = position;
    }

    public void move() {

    }

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getRoll() {
        return roll;
    }

    public void setRoll(float roll) {
        this.roll = roll;
    }

}
