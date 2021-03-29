package audio;

import java.io.IOException;

public class Test {

    public static void main(String[] args) throws IOException {
        Audio.init();
        Audio.setListenerData();

        int buffer = Audio.loadSound("bounce.wav");
        Source source = new Source();

        char c = ' ';
        while (c != 'q') {
            c = (char) System.in.read();
            if (c == 'p')
                source.play(buffer);
        }

        source.remove();
        Audio.clean();
    }

}
