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

    public Vector3f(Vector4f vector4f) {
        this.x = vector4f.x;
        this.y = vector4f.y;
        this.z = vector4f.z;
    }

    public Vector3f(Vector3f vector3f) {
        this.x = vector3f.x;
        this.y = vector3f.y;
        this.z = vector3f.z;
    }

    public void add(Vector3f vector) {
        x += vector.x;
        y += vector.y;
        z += vector.z;
    }

    public static Vector3f add(Vector3f vectorL, Vector3f vectorR) {
        return new Vector3f(vectorL.x + vectorR.x, vectorL.y + vectorR.y, vectorL.z + vectorR.z);
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

    public float lengthSquared() {
        return x * x + y * y + z * z;
    }

    public static Vector3f cross(Vector3f left, Vector3f right) {
        return new Vector3f(
                left.y * right.z - left.z * right.y,
                right.x * left.z - right.z * left.x,
                left.x * right.y - left.y * right.x
        );
    }

    public void negate() {
        x = -x;
        y = -y;
        z = -z;
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
