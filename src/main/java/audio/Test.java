package audio;

import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;
import utils.math.Vector3f;

import java.io.IOException;

public class Test {

    public static void main(String[] args) throws IOException, InterruptedException {
        AudioManager.init();
        AudioManager.setListenerData(new Vector3f(0, 0, 0));
        AL10.alDistanceModel(AL11.AL_LINEAR_DISTANCE_CLAMPED);

        int buffer = AudioManager.loadSound("bounce.wav");
        Source source = new Source();
        source.setLooping(true);
        source.play(buffer);

        float xPos = 0;
        source.setPosition(xPos, 0, 0);

        char c = ' ';
        while (c != 'q') {
            xPos -= 0.03f;
            source.setPosition(xPos, 0, 0);
            System.out.println(xPos);
            Thread.sleep(10);
        }

        source.remove();
        AudioManager.clean();
    }

}
