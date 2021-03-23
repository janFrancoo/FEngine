package utils.particle;

import model.Particle;
import model.TextureParticle;
import render_engine.DisplayManager;
import utils.math.Vector3f;

public class ParticleSystem {

    private final float pps;
    private final float speed;
    private final float gravityComplient;
    private final float lifeLength;
    private final TextureParticle texture;

    public ParticleSystem(TextureParticle texture, float pps, float speed, float gravityComplient, float lifeLength) {
        this.texture = texture;
        this.pps = pps;
        this.speed = speed;
        this.gravityComplient = gravityComplient;
        this.lifeLength = lifeLength;
    }

    public void generateParticles(Vector3f systemCenter){
        double delta = DisplayManager.getDelta();
        float particlesToCreate = (float) (pps * delta);
        int count = (int) Math.floor(particlesToCreate);
        float partialParticle = particlesToCreate % 1;
        for(int i=0;i<count;i++){
            emitParticle(systemCenter);
        }
        if(Math.random() < partialParticle){
            emitParticle(systemCenter);
        }
    }

    private void emitParticle(Vector3f center){
        float dirX = (float) Math.random() * 2f - 1f;
        float dirZ = (float) Math.random() * 2f - 1f;
        Vector3f velocity = new Vector3f(dirX, 1, dirZ);
        velocity.normalise();
        velocity.scale(speed);
        new Particle(texture, new Vector3f(center.x, center.y, center.z), velocity, gravityComplient, lifeLength,
                0, 2);
    }

}
