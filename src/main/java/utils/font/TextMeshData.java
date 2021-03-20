package utils.font;

public class TextMeshData {

    private final float[] vertexPositions;
    private final float[] textureCoords;

    protected TextMeshData(float[] vertexPositions, float[] textureCoords) {
        this.vertexPositions = vertexPositions;
        this.textureCoords = textureCoords;
    }

    public int getVertexCount() {
        return vertexPositions.length / 2;
    }

    public float[] getVertexPositions() {
        return vertexPositions;
    }

    public float[] getTextureCoords() {
        return textureCoords;
    }

}
