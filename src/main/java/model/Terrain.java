package model;

import render_engine.ModelLoader;

import static utils.Constants.TERRAIN_SIZE;
import static utils.Constants.TERRAIN_VERTEX_COUNT;

public class Terrain {

    private final float x, z;
    private final RawModel rawModel;
    private final TerrainTexturePack texturePack;
    private final Texture blendMap;

    public Terrain(int gridX, int gridZ, ModelLoader modelLoader, TerrainTexturePack terrainTexturePack, Texture blendMap) {
        this.x = gridX * TERRAIN_SIZE;
        this.z = gridZ * TERRAIN_SIZE;
        this.rawModel = generateTerrain(modelLoader);
        this.texturePack = terrainTexturePack;
        this.blendMap = blendMap;
    }

    private RawModel generateTerrain(ModelLoader loader){
        int count = TERRAIN_VERTEX_COUNT * TERRAIN_VERTEX_COUNT;

        float[] vertices = new float[count * 3];
        int[] indices = new int[6 * (TERRAIN_VERTEX_COUNT - 1) * (TERRAIN_VERTEX_COUNT - 1)];
        float[] textureCoords = new float[count*2];
        float[] normals = new float[count * 3];

        int vertexPointer = 0;
        for (int i=0; i<TERRAIN_VERTEX_COUNT; i++) {
            for (int j=0; j<TERRAIN_VERTEX_COUNT; j++) {
                vertices[vertexPointer * 3] = (float) j / ((float) TERRAIN_VERTEX_COUNT - 1) * TERRAIN_SIZE;
                vertices[vertexPointer * 3 + 1] = 0;
                vertices[vertexPointer * 3 + 2] = (float) i / ((float) TERRAIN_VERTEX_COUNT - 1) * TERRAIN_SIZE;
                normals[vertexPointer * 3] = 0;
                normals[vertexPointer * 3 + 1] = 1;
                normals[vertexPointer * 3 + 2] = 0;
                textureCoords[vertexPointer * 2] = (float) j / ((float) TERRAIN_VERTEX_COUNT - 1);
                textureCoords[vertexPointer * 2 + 1] = (float) i / ((float) TERRAIN_VERTEX_COUNT - 1);
                vertexPointer++;
            }
        }

        int pointer = 0;
        for (int gz = 0; gz<TERRAIN_VERTEX_COUNT - 1; gz++) {
            for (int gx = 0; gx<TERRAIN_VERTEX_COUNT - 1; gx++) {
                int topLeft = gz * TERRAIN_VERTEX_COUNT + gx;
                int topRight = topLeft + 1;
                int bottomLeft = (gz + 1) * TERRAIN_VERTEX_COUNT + gx;
                int bottomRight = bottomLeft + 1;
                indices[pointer++] = topLeft;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = topRight;
                indices[pointer++] = topRight;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = bottomRight;
            }
        }

        return loader.loadToVao(vertices, indices, textureCoords, normals);
    }

    public float getX() {
        return x;
    }

    public float getZ() {
        return z;
    }

    public RawModel getRawModel() {
        return rawModel;
    }

    public TerrainTexturePack getTexturePack() {
        return texturePack;
    }

    public Texture getBlendMap() {
        return blendMap;
    }

}
