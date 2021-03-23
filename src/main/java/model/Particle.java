package model;

import render_engine.DisplayManager;
import utils.math.Vector2f;
import utils.math.Vector3f;
import utils.particle.ParticleManager;

import static utils.Constants.GRAVITY;

public class Particle {

    private final Vector3f position;
    private final Vector3f velocity;
    private final float gravityEffect;
    private final float life;
    private final float rotation;
    private final float scale;

    private float elapsedTime = 0;
    private float distanceFromCamera;

    private final TextureParticle texture;
    private final Vector2f textureOffset = new Vector2f(0, 0);
    private final Vector2f textureOffset2 = new Vector2f(0, 0);
    private float blend;

    public Particle(TextureParticle textureParticle, Vector3f position, Vector3f velocity, float gravityEffect,
                    float life, float rotation, float scale) {
        this.texture = textureParticle;
        this.position = position;
        this.velocity = velocity;
        this.gravityEffect = gravityEffect;
        this.life = life;
        this.rotation = rotation;
        this.scale = scale;
        ParticleManager.addParticle(this);
    }

    public boolean update(Camera camera) {
        velocity.y += GRAVITY * gravityEffect * DisplayManager.getDelta();
        Vector3f dPos = new Vector3f(velocity.x, velocity.y, velocity.z);
        dPos.scale((float) DisplayManager.getDelta());
        position.add(dPos);
        distanceFromCamera = Vector3f.sub(camera.getPosition(), position).lengthSquared();
        updateTextureCoord();
        elapsedTime += DisplayManager.getDelta();
        return elapsedTime < life;
    }

    private void updateTextureCoord() {
        float lifeFactor = elapsedTime / life;
        int stageCount = texture.getRows() * texture.getRows();
        float atlasProgression = lifeFactor * stageCount;
        int idx = (int) Math.floor(atlasProgression);
        int idx2 = idx < stageCount - 1 ? idx + 1 : idx;
        blend = atlasProgression % 1;
        setTextureOffset(textureOffset, idx);
        setTextureOffset(textureOffset2, idx2);
    }

    private void setTextureOffset(Vector2f offset, int index) {
        int column = index % texture.getRows();
        int row = index / texture.getRows();
        offset.x = (float) column / texture.getRows();
        offset.y = (float) row / texture.getRows();
    }

    public TextureParticle getTexture() {
        return texture;
    }

    public Vector2f getTextureOffset() {
        return textureOffset;
    }

    public Vector2f getTextureOffset2() {
        return textureOffset2;
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getRotation() {
        return rotation;
    }

    public float getScale() {
        return scale;
    }

    public float getBlend() {
        return blend;
    }

    public float getDistanceFromCamera() {
        return distanceFromCamera;
    }

}
