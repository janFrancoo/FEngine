package utils.particle;

import model.Particle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ParticleManager {

    public static final List<Particle> particles = new ArrayList<>();

    public static void addParticle(Particle particle) {
        particles.add(particle);
    }

    public static void update() {
        Iterator<Particle> iterator = particles.iterator();
        while (iterator.hasNext()) {
            Particle particle = iterator.next();
            boolean stillAlive = particle.update();
            if (!stillAlive)
                iterator.remove();
        }
    }

    public static void clean() {
        particles.clear();
    }

}
