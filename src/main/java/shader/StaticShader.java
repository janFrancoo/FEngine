package shader;

public class StaticShader extends ShaderLoader {

    private static final String VERTEX_FILE = "C:\\Users\\PC\\Documents\\Java Projects\\FEngine\\src\\main\\java" +
            "\\shader\\vertexShader.txt";
    private static final String FRAGMENT_FILE = "C:\\Users\\PC\\Documents\\Java Projects\\FEngine\\src\\main\\java" +
            "\\shader\\fragmentShader.txt";

    public StaticShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }

}
