package utils.particle;

import model.Camera;
import model.Particle;
import model.TextureParticle;

import java.util.*;

public class ParticleManager {

    public static final Map<TextureParticle, List<Particle>> particles = new HashMap<>();

    public static void addParticle(Particle particle) {
        List<Particle> particleList = particles.computeIfAbsent(particle.getTexture(), k -> new ArrayList<>());
        particleList.add(particle);
    }

    public static void update(Camera camera) {
        Iterator<Map.Entry<TextureParticle, List<Particle>>> iterator = particles.entrySet().iterator();
        while (iterator.hasNext()) {
            List<Particle> particleList = iterator.next().getValue();
            Iterator<Particle> particleIterator = particleList.iterator();
            while (particleIterator.hasNext()) {
                Particle particle = particleIterator.next();
                boolean stillAlive = particle.update(camera);
                if (!stillAlive) {
                    particleIterator.remove();
                    if (particleList.isEmpty())
                        iterator.remove();
                }
            }
            InsertionSort.sortHighToLow(particleList);
        }
    }

    public static void clean() {
        particles.clear();
    }

}
