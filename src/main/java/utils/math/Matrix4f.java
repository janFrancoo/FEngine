package utils.math;

import model.Camera;
import utils.Constants;

import java.nio.FloatBuffer;

public class Matrix4f {

    public float m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, m30, m31, m32, m33;

    public Matrix4f() {
        super();
        setIdentity(this);
    }

    public static void setIdentity(Matrix4f m) {
        m.m00 = 1.0f;
        m.m01 = 0.0f;
        m.m02 = 0.0f;
        m.m03 = 0.0f;
        m.m10 = 0.0f;
        m.m11 = 1.0f;
        m.m12 = 0.0f;
        m.m13 = 0.0f;
        m.m20 = 0.0f;
        m.m21 = 0.0f;
        m.m22 = 1.0f;
        m.m23 = 0.0f;
        m.m30 = 0.0f;
        m.m31 = 0.0f;
        m.m32 = 0.0f;
        m.m33 = 1.0f;
    }

    public void store(FloatBuffer buf) {
        buf.put(m00);
        buf.put(m01);
        buf.put(m02);
        buf.put(m03);
        buf.put(m10);
        buf.put(m11);
        buf.put(m12);
        buf.put(m13);
        buf.put(m20);
        buf.put(m21);
        buf.put(m22);
        buf.put(m23);
        buf.put(m30);
        buf.put(m31);
        buf.put(m32);
        buf.put(m33);
    }

    public static void scale(Vector3f vec, Matrix4f src, Matrix4f dest) {
        dest.m00 = src.m00 * vec.x;
        dest.m01 = src.m01 * vec.x;
        dest.m02 = src.m02 * vec.x;
        dest.m03 = src.m03 * vec.x;
        dest.m10 = src.m10 * vec.y;
        dest.m11 = src.m11 * vec.y;
        dest.m12 = src.m12 * vec.y;
        dest.m13 = src.m13 * vec.y;
        dest.m20 = src.m20 * vec.z;
        dest.m21 = src.m21 * vec.z;
        dest.m22 = src.m22 * vec.z;
        dest.m23 = src.m23 * vec.z;
    }

    public static void rotate(float angle, Vector3f axis, Matrix4f src, Matrix4f dest) {
        float c = (float) Math.cos(angle);
        float s = (float) Math.sin(angle);
        float oneminusc = 1.0f - c;
        float xy = axis.x*axis.y;
        float yz = axis.y*axis.z;
        float xz = axis.x*axis.z;
        float xs = axis.x*s;
        float ys = axis.y*s;
        float zs = axis.z*s;

        float f00 = axis.x*axis.x*oneminusc+c;
        float f01 = xy*oneminusc+zs;
        float f02 = xz*oneminusc-ys;
        // n[3] not used
        float f10 = xy*oneminusc-zs;
        float f11 = axis.y*axis.y*oneminusc+c;
        float f12 = yz*oneminusc+xs;
        // n[7] not used
        float f20 = xz*oneminusc+ys;
        float f21 = yz*oneminusc-xs;
        float f22 = axis.z*axis.z*oneminusc+c;

        float t00 = src.m00 * f00 + src.m10 * f01 + src.m20 * f02;
        float t01 = src.m01 * f00 + src.m11 * f01 + src.m21 * f02;
        float t02 = src.m02 * f00 + src.m12 * f01 + src.m22 * f02;
        float t03 = src.m03 * f00 + src.m13 * f01 + src.m23 * f02;
        float t10 = src.m00 * f10 + src.m10 * f11 + src.m20 * f12;
        float t11 = src.m01 * f10 + src.m11 * f11 + src.m21 * f12;
        float t12 = src.m02 * f10 + src.m12 * f11 + src.m22 * f12;
        float t13 = src.m03 * f10 + src.m13 * f11 + src.m23 * f12;
        dest.m20 = src.m00 * f20 + src.m10 * f21 + src.m20 * f22;
        dest.m21 = src.m01 * f20 + src.m11 * f21 + src.m21 * f22;
        dest.m22 = src.m02 * f20 + src.m12 * f21 + src.m22 * f22;
        dest.m23 = src.m03 * f20 + src.m13 * f21 + src.m23 * f22;
        dest.m00 = t00;
        dest.m01 = t01;
        dest.m02 = t02;
        dest.m03 = t03;
        dest.m10 = t10;
        dest.m11 = t11;
        dest.m12 = t12;
        dest.m13 = t13;
    }

    public static void translate(Vector3f vec, Matrix4f src, Matrix4f dest) {
        dest.m30 += src.m00 * vec.x + src.m10 * vec.y + src.m20 * vec.z;
        dest.m31 += src.m01 * vec.x + src.m11 * vec.y + src.m21 * vec.z;
        dest.m32 += src.m02 * vec.x + src.m12 * vec.y + src.m22 * vec.z;
        dest.m33 += src.m03 * vec.x + src.m13 * vec.y + src.m23 * vec.z;
    }

    public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry, float rz, float scale) {
        Matrix4f matrix = new Matrix4f();
        Matrix4f.translate(translation, matrix, matrix);
        Matrix4f.rotate((float) Math.toRadians(rx), new Vector3f(1, 0, 0), matrix, matrix);
        Matrix4f.rotate((float) Math.toRadians(ry), new Vector3f(0, 1, 0), matrix, matrix);
        Matrix4f.rotate((float) Math.toRadians(rz), new Vector3f(0, 0, 1), matrix, matrix);
        Matrix4f.scale(new Vector3f(scale, scale, scale), matrix, matrix);
        return matrix;
    }

    public static Matrix4f createProjectionMatrix() {
        float aspectRatio = (float) Constants.WIDTH / (float) Constants.HEIGHT;
        float yScale = (float) ((1f / Math.tan(Math.toRadians(Constants.FOV / 2f))) * aspectRatio);
        float xScale = yScale / aspectRatio;
        float frustumLen = Constants.FAR_PLANE - Constants.NEAR_PLANE;

        Matrix4f matrix = new Matrix4f();
        matrix.m00 = xScale;
        matrix.m11 = yScale;
        matrix.m22 = -((Constants.FAR_PLANE + Constants.NEAR_PLANE) / frustumLen);
        matrix.m23 = -1;
        matrix.m32 = -((2 * Constants.NEAR_PLANE * Constants.FAR_PLANE) / frustumLen);
        matrix.m33 = 0;
        return matrix;
    }

    public static Matrix4f createViewMatrix(Camera camera) {
        Matrix4f matrix = new Matrix4f();
        Matrix4f.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1, 0, 0), matrix, matrix);
        Matrix4f.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0, 1, 0), matrix, matrix);
        Vector3f cameraPos = camera.getPosition();
        Vector3f negativeCameraPos = new Vector3f(-cameraPos.x, -cameraPos.y, -cameraPos.z);
        Matrix4f.translate(negativeCameraPos, matrix, matrix);
        return matrix;
    }

}
