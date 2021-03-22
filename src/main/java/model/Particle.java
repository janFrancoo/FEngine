package model;

import render_engine.DisplayManager;
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

    public Particle(Vector3f position, Vector3f velocity, float gravityEffect, float life, float rotation, float scale) {
        this.position = position;
        this.velocity = velocity;
        this.gravityEffect = gravityEffect;
        this.life = life;
        this.rotation = rotation;
        this.scale = scale;
        ParticleManager.addParticle(this);
    }

    public boolean update() {
        velocity.y += GRAVITY * gravityEffect * DisplayManager.getDelta();
        Vector3f dPos = new Vector3f(velocity.x, velocity.y, velocity.z);
        dPos.scale((float) DisplayManager.getDelta());
        position.add(dPos);
        elapsedTime += DisplayManager.getDelta();
        return elapsedTime < life;
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

}
