package utils.math;

public class Vector3f {

    public float x, y, z;

    public Vector3f() {
        this.x = this.y = this.z = 0;
    }

    public Vector3f(float value) {
        this.x = value;
        this.y = value;
        this.z = value;
    }

    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

}
