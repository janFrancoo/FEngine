package audio;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import utils.loader.Wave;
import utils.loader.WaveLoader;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.openal.ALC10.*;

public class Audio {

    private static final List<Integer> buffers = new ArrayList<>();

    public static void init() {
        long device = alcOpenDevice((ByteBuffer)null);
        ALCCapabilities deviceCaps = ALC.createCapabilities(device);
        long context = alcCreateContext(device, (IntBuffer) null);
        alcMakeContextCurrent(context);
        AL.createCapabilities(deviceCaps);

        try {
            ALC.create();
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
        }
    }

    public static int loadSound(String file) {
        int buffer = AL10.alGenBuffers();
        buffers.add(buffer);
        Wave wave = WaveLoader.loadWave(file);
        AL10.alBufferData(buffer, wave.getFormat(), wave.getData(), wave.getFreq());
        return buffer;
    }

    public static void setListenerData() {
        AL10.alListener3f(AL10.AL_POSITION, 0, 0 ,0);
        AL10.alListener3f(AL10.AL_VELOCITY, 0, 0 ,0);
    }

    public static void clean() {
        for (int buffer : buffers)
            AL10.alDeleteBuffers(buffer);
        ALC.destroy();
    }

}
