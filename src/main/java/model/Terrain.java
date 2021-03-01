package model;

import render_engine.ModelLoader;
import utils.math.GameMath;
import utils.math.Vector2f;
import utils.math.Vector3f;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static utils.Constants.*;

public class Terrain {

    private final float x, z;
    private final RawModel rawModel;
    private final TerrainTexturePack texturePack;
    private final Texture blendMap;
    private float[][] heights;

    public Terrain(int gridX, int gridZ, ModelLoader modelLoader, TerrainTexturePack terrainTexturePack,
                   Texture blendMap, String heightMap) {
        this.x = gridX * TERRAIN_SIZE;
        this.z = gridZ * TERRAIN_SIZE;
        this.rawModel = generateTerrain(modelLoader, heightMap);
        this.texturePack = terrainTexturePack;
        this.blendMap = blendMap;
    }

    private RawModel generateTerrain(ModelLoader loader, String heightMap){
        BufferedImage image;
        try {
            image = ImageIO.read(new File("res/" + heightMap + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        int TERRAIN_VERTEX_COUNT = image.getHeight();
        heights = new float[TERRAIN_VERTEX_COUNT][TERRAIN_VERTEX_COUNT];
        int count = TERRAIN_VERTEX_COUNT * TERRAIN_VERTEX_COUNT;

        float[] vertices = new float[count * 3];
        int[] indices = new int[6 * (TERRAIN_VERTEX_COUNT - 1) * (TERRAIN_VERTEX_COUNT - 1)];
        float[] textureCoords = new float[count*2];
        float[] normals = new float[count * 3];

        int vertexPointer = 0;
        for (int i=0; i<TERRAIN_VERTEX_COUNT; i++) {
            for (int j=0; j<TERRAIN_VERTEX_COUNT; j++) {
                float height = getHeightFromHeightMap(j, i, image);
                heights[j][i] = height;
                vertices[vertexPointer * 3] = (float) j / ((float) TERRAIN_VERTEX_COUNT - 1) * TERRAIN_SIZE;
                vertices[vertexPointer * 3 + 1] = height;
                vertices[vertexPointer * 3 + 2] = (float) i / ((float) TERRAIN_VERTEX_COUNT - 1) * TERRAIN_SIZE;
                Vector3f normal = calculateNormal(j, i, image);
                normals[vertexPointer * 3] = normal.x;
                normals[vertexPointer * 3 + 1] = normal.y;
                normals[vertexPointer * 3 + 2] = normal.z;
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

    private float getHeightFromHeightMap(int x, int z, BufferedImage image) {
        if (x < 0 || x >= image.getHeight() || z < 0 || z >= image.getHeight())
            return 0;

        float height = image.getRGB(x, z);
        height += MAX_PIXEL_COLOR / 2f;
        height /= MAX_PIXEL_COLOR;
        height *= TERRAIN_MAX_HEIGHT;
        return height;
    }

    private Vector3f calculateNormal(int x, int z, BufferedImage image) {
        float heightL = getHeightFromHeightMap(x - 1, z, image);
        float heightR = getHeightFromHeightMap(x + 1, z, image);
        float heightD = getHeightFromHeightMap(x, z - 1, image);
        float heightU = getHeightFromHeightMap(x, z + 1, image);
        Vector3f normal = new Vector3f(heightL - heightR, 2f, heightD - heightU);
        normal.normalise();
        return normal;
    }

    public float getHeight(float worldX, float worldZ) {
        float terrainX = worldX - x;
        float terrainZ = worldZ - z;
        float gridSquareSize = TERRAIN_SIZE / ((float) heights.length - 1);
        int gridX = (int) Math.floor(terrainX / gridSquareSize);
        int gridZ = (int) Math.floor(terrainZ / gridSquareSize);
        if (gridX >= heights.length - 1 || gridZ >= heights.length - 1 || gridX < 0 || gridZ < 0)
            return 0;
        float xCoord = (terrainX % gridSquareSize) / gridSquareSize;
        float zCoord = (terrainZ % gridSquareSize) / gridSquareSize;
        if (xCoord <= (1 - zCoord)) {
            return GameMath.barryCentric(new Vector3f(0, heights[gridX][gridZ], 0), new Vector3f(1,
                            heights[gridX + 1][gridZ], 0), new Vector3f(0,
                            heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
        } else {
             return GameMath.barryCentric(new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(1,
                            heights[gridX + 1][gridZ + 1], 1), new Vector3f(0,
                            heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
        }
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
