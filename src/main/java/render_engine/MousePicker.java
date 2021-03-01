package render_engine;

import model.Camera;
import utils.input.CursorInput;
import utils.math.*;

public class MousePicker {

    private Vector3f currentRay;

    private final Matrix4f projectionMatrix;
    private Matrix4f viewMatrix;
    private final Camera camera;

    public MousePicker(Camera camera, Matrix4f projectionMatrix) {
        this.camera = camera;
        this.projectionMatrix = projectionMatrix;
        this.viewMatrix = GameMath.createViewMatrix(camera);
    }

    public void update() {
        viewMatrix = GameMath.createViewMatrix(camera);
        currentRay = calculateMouseRay();
    }

    private Vector3f calculateMouseRay() {
        float mouseX = (float) CursorInput.X;
        float mouseY = (float) CursorInput.Y;

        // viewPort to normalizedSpace
        Vector2f normalizedCoords = GameMath.convertViewportToNormalizedDeviceCoord(mouseX, mouseY);
        // normalized to homogeneous clip space
        // we want a vector instead of a point, so no necessity for inversion
        Vector4f clipCoords = new Vector4f(normalizedCoords.x, normalizedCoords.y, -1f, 1f);
        // clipCoords to eyeCoords
        Vector4f eyeCoords = GameMath.convertClipSpaceToEyeSpaceCoords(clipCoords, projectionMatrix);
        // eyeCoords to worldCoords
        return GameMath.convertEyeCoordsToWorldCoords(eyeCoords, viewMatrix);
    }

    public Vector3f getCurrentRay() {
        return currentRay;
    }

}
