package utils.terrain;

import utils.math.GameMath;

import java.util.Random;

public class HeightGenerator {

    private static final float AMPLITUDE = 70f;
    private static final float OCTAVES = 3;
    public static final float ROUGHNESS = 0.3f;

    private final Random random = new Random();
    private final int seed;

    public HeightGenerator() {
        seed = random.nextInt(1000000000);
    }

    public float generateHeight(int x, int z) {
        float total = 0;
        float d = (float) Math.pow(2, OCTAVES - 1);
        for (int i = 0; i < OCTAVES; i++) {
            float freq = (float) (Math.pow(2, i) / d);
            float amp = (float) Math.pow(ROUGHNESS, i) * AMPLITUDE;
            total += getInterpolatedNoise(x * freq, z * freq) * amp;
        }
        return total;
    }

    private float getSmoothNoise(int x, int z) {
        float corners = (getNoise(x - 1, z - 1) + getNoise(x + 1, z - 1) + getNoise(x - 1, z + 1) +
                getNoise(x + 1, z + 1)) / 16f;
        float sides = (getNoise(x - 1, z) + getNoise(x + 1, z) + getNoise(x, z - 1)
                + getNoise(x, z + 1)) / 8f;
        float center = getNoise(x, z) / 4f;
        return corners + sides + center;
    }

    private float getNoise(int x, int z) {
        random.setSeed(x * 49632L + z * 325176L + seed);
        return random.nextFloat() * 2f - 1f;
    }

    private float getInterpolatedNoise(float x, float z) {
        int intX = (int) x;
        int intZ = (int) z;
        float fracX = x - intX;
        float fracZ = z - intZ;

        float v1 = getSmoothNoise(intX, intZ);
        float v2 = getSmoothNoise(intX + 1, intZ);
        float v3 = getSmoothNoise(intX, intZ + 1);
        float v4 = getSmoothNoise(intX + 1, intZ + 1);
        float i1 = GameMath.interpolate(v1, v2, fracX);
        float i2 = GameMath.interpolate(v3, v4, fracX);
        return GameMath.interpolate(i1, i2, fracZ);
    }

}
