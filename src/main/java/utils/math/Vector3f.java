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

    public void add(Vector3f vector) {
        x += vector.x;
        y += vector.y;
        z += vector.z;
    }

    public static Vector3f sub(Vector3f left, Vector3f right) {
        return new Vector3f(left.x - right.x, left.y - right.y, left.z - right.z);
    }

    public void normalise() {
        float vectorLength = length();
        x /= vectorLength;
        y /= vectorLength;
        z /= vectorLength;
    }

    public void scale(float val) {
        x *= val;
        y *= val;
        z *= val;
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
