package utils.math;

public class Vector3f {

    public float x, y, z;

    public Vector3f() {
        this.x = this.y = this.z = 0;
    }

    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void normalise() {
        float vectorLength = length();
        x /= vectorLength;
        y /= vectorLength;
        z /= vectorLength;
    }

    public float length() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    @Override
    public String toString() {
        return "Vector3f{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}
