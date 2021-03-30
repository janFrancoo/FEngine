package model;

import audio.Source;
import org.lwjgl.glfw.GLFW;
import render_engine.DisplayManager;
import utils.input.KeyInput;
import utils.math.Vector3f;

import static utils.Constants.*;

public class Player extends Entity {

    private float currentRunSpeed = 0;
    private float currentTurnSpeed = 0;
    private float currentUpwardsSpeed = 0;
    private boolean isJumping = false;

    public Player(TexturedModel texturedModel, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        super(texturedModel, position, rotX, rotY, rotZ, scale);
    }

    public void move(Terrain terrain, Source source, int jumpSound) {
        checkInputs(source, jumpSound);
        super.increaseRotation(0, (float) (currentTurnSpeed * DisplayManager.getDelta()), 0);
        float distance = (float) (currentRunSpeed * DisplayManager.getDelta());
        float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
        float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
        super.increasePosition(new Vector3f(dx, 0, dz));
        currentUpwardsSpeed += GRAVITY * DisplayManager.getDelta();
        super.increasePosition(new Vector3f(0, (float) (currentUpwardsSpeed * DisplayManager.getDelta()), 0));
        float terrainHeight = terrain.getHeight(super.getPosition().x, super.getPosition().z);
        if (super.getPosition().y < terrainHeight) {
            currentUpwardsSpeed = 0;
            super.getPosition().y = terrainHeight;
            isJumping = false;
        }
    }

    private void checkInputs(Source source, int jumpSound) {
        if (KeyInput.isKeyDown(GLFW.GLFW_KEY_W))
            currentRunSpeed = RUN_SPEED;
        else if (KeyInput.isKeyDown(GLFW.GLFW_KEY_S))
            currentRunSpeed = -RUN_SPEED;
        else
            currentRunSpeed = 0;

        if (KeyInput.isKeyDown(GLFW.GLFW_KEY_A))
            currentTurnSpeed = TURN_SPEED;
        else if (KeyInput.isKeyDown(GLFW.GLFW_KEY_D))
            currentTurnSpeed = -TURN_SPEED;
        else
            currentTurnSpeed = 0;

        if (!isJumping && KeyInput.isKeyDown(GLFW.GLFW_KEY_SPACE)) {
            source.play(jumpSound);
            isJumping = true;
            currentUpwardsSpeed = JUMP_POWER;
        }
    }

}
