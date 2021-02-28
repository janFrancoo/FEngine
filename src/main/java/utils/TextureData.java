package utils;

public class TextureData {

    private final int width;
    private final int height;
    private final int[] data;

    public TextureData(int[] data, int width, int height){
        this.data = data;
        this.width = width;
        this.height = height;
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public int[] getBuffer(){
        return data;
    }

}
