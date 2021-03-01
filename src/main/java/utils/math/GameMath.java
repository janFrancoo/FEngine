package utils.math;

import model.Camera;
import utils.Constants;

import static utils.Constants.WIDTH;
import static utils.Constants.HEIGHT;

public class GameMath {

    public static Matrix4f createTransformationMatrix(Vector2f translation, Vector2f scale) {
        Matrix4f matrix = new Matrix4f();
        Matrix4f.translate(translation, matrix, matrix);
        Matrix4f.scale(new Vector3f(scale.x, scale.y, 1f), matrix, matrix);
        return matrix;
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

    public static float barryCentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos) {
        float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
        float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
        float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
        float l3 = 1.0f - l1 - l2;
        return l1 * p1.y + l2 * p2.y + l3 * p3.y;
    }

    public static Vector2f convertViewportToNormalizedDeviceCoord(float x, float y) {
        return new Vector2f(
                (2f * x) / WIDTH - 1,
                (2f * y) / HEIGHT - 1
        );
    }

    public static Vector4f convertClipSpaceToEyeSpaceCoords(Vector4f clipCoords, Matrix4f projectionMatrix) {
        Matrix4f invertedProjection = Matrix4f.invert(projectionMatrix, null);
        Vector4f eyeCoords = Matrix4f.transform(invertedProjection, clipCoords, null);
        return new Vector4f(eyeCoords.x, eyeCoords.y, -1f, 0f);
    }

    public static Vector3f convertEyeCoordsToWorldCoords(Vector4f eyeCoords, Matrix4f viewMatrix) {
        Matrix4f invertedView = Matrix4f.invert(viewMatrix, null);
        Vector4f rayWorld = Matrix4f.transform(invertedView, eyeCoords, null);
        Vector3f mouseRay = new Vector3f(rayWorld.x, rayWorld.y, rayWorld.z);
        mouseRay.normalise();
        return mouseRay;
    }

}
