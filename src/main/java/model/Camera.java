package model;

import utils.input.CursorInput;
import utils.input.MouseButtonInput;
import utils.input.ScrollInput;
import utils.math.Vector3f;

public class Camera {

    private final Vector3f position;
    private float pitch, yaw, roll;
    private Player player;
    private float distanceFromPlayer, angleAroundPlayer;

    public Camera(Player player) {
        this.position = new Vector3f(0, 0, 0);
        this.player = player;
        distanceFromPlayer = 50;
        angleAroundPlayer = 0;
    }

    public void move() {
        changeZoom();
        changePitch();
        changeAngleAroundPlayer();

        float theta = player.getRotY() + angleAroundPlayer;
        float horizontalDistance = (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
        float verticalDistance = (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
        float offsetX = (float) (horizontalDistance * Math.sin(Math.toRadians(theta)));
        float offsetZ = (float) (horizontalDistance * Math.cos(Math.toRadians(theta)));
        this.position.x = player.getPosition().x - offsetX;
        this.position.y = player.getPosition().y + verticalDistance;
        this.position.z = player.getPosition().z - offsetZ;
    }

    private void changeZoom() {
        double zoomLevel = ScrollInput.dWheel * 0.5f;
        ScrollInput.dWheel = 0;
        System.out.println(distanceFromPlayer);
        if (distanceFromPlayer - zoomLevel > 15 && distanceFromPlayer - zoomLevel < 150)
            distanceFromPlayer -= zoomLevel;
    }

    private void changePitch() {
        if (MouseButtonInput.isLeftButtonDown) {
            double pitchChange = CursorInput.dY * 0.1f;
            if (pitch - pitchChange > 0 && pitch - pitchChange < 180)
                pitch -= pitchChange;
        }
    }

    private void changeAngleAroundPlayer() {
        if (MouseButtonInput.isRightButtonDown) {
            double angleChange = CursorInput.dX * 0.3f;
            angleAroundPlayer -= angleChange;
        }
        yaw = 180 - (player.getRotY() + angleAroundPlayer);
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

    public void setPlayer(Player player) {
        this.player = player;
    }

}
