package utils.math;

public class Vector2f {

    public float x, y;

    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public static Vector2f sub(Vector2f left, Vector2f right) {
        return new Vector2f(left.x - right.x, left.y - right.y);
    }

}
