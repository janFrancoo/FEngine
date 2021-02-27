package model;

import utils.math.Vector2f;
import utils.math.Vector3f;

public class Entity {

    private Vector3f position;
    private float rotX, rotY, rotZ;
    private float scale;
    private TexturedModel texturedModel;
    private int textureIdx = 0;

    public Entity(TexturedModel texturedModel, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        this.texturedModel = texturedModel;
        this.position = position;
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
        this.scale = scale;
    }

    public Entity(TexturedModel texturedModel, int textureIdx, Vector3f position,
                  float rotX, float rotY, float rotZ, float scale) {
        this.texturedModel = texturedModel;
        this.textureIdx = textureIdx;
        this.position = position;
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
        this.scale = scale;
    }

    public Vector2f getTextureOffset() {
        int rows = texturedModel.getTexture().getRows();
        int column = textureIdx % rows;
        int row = textureIdx / rows;
        return new Vector2f(
                (float) column / (float) texturedModel.getTexture().getRows(),
                (float) row / (float) texturedModel.getTexture().getRows());
    }

    public void increasePosition(Vector3f dPos) {
        this.position.x += dPos.x;
        this.position.y += dPos.y;
        this.position.z += dPos.z;
    }

    public void increaseRotation(float dx, float dy, float dz) {
        this.rotX += dx;
        this.rotY += dy;
        this.rotZ += dz;
    }

    public TexturedModel getTexturedModel() {
        return texturedModel;
    }

    public void setTexturedModel(TexturedModel texturedModel) {
        this.texturedModel = texturedModel;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public float getRotX() {
        return rotX;
    }

    public void setRotX(float rotX) {
        this.rotX = rotX;
    }

    public float getRotY() {
        return rotY;
    }

    public void setRotY(float rotY) {
        this.rotY = rotY;
    }

    public float getRotZ() {
        return rotZ;
    }

    public void setRotZ(float rotZ) {
        this.rotZ = rotZ;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

}
