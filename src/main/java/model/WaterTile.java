package model;

public class WaterTile {

    private final float x, z;
    private final float height;

    public WaterTile(float x, float z, float height) {
        this.x = x;
        this.z = z;
        this.height = height;
    }

    public float getX() {
        return x;
    }

    public float getZ() {
        return z;
    }

    public float getHeight() {
        return height;
    }

}
