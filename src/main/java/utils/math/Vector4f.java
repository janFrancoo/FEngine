package utils.math;

public class Vector4f {

    public float x, y, z, w;

    public Vector4f() {
        this.x = this.y = this.z = this.w = 0;
    }

    public Vector4f(float value) {
        this.x = value;
        this.y = value;
        this.z = value;
        this.w = value;
    }

    public Vector4f(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

}
