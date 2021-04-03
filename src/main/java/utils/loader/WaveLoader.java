package utils.loader;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;
import javax.sound.sampled.*;
import java.io.*;
import java.nio.ByteBuffer;

public class WaveLoader {

    public static Wave loadWave(String fileName) {
        InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);

        assert stream != null;
        InputStream bufferedInput = new BufferedInputStream(stream);
        AudioInputStream audioStream = null;

        try {
            audioStream = AudioSystem.getAudioInputStream(bufferedInput);
        } catch (UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }

        assert audioStream != null;
        AudioFormat audioFormat = audioStream.getFormat();
        int format;
        if (audioFormat.getChannels() == 1)
            format = audioFormat.getSampleSizeInBits() == 8 ? AL10.AL_FORMAT_MONO8 : AL10.AL_FORMAT_MONO16;
        else
            format = audioFormat.getSampleSizeInBits() == 8 ? AL10.AL_FORMAT_STEREO8 : AL10.AL_FORMAT_STEREO16;

        int sampleRate = (int) audioFormat.getSampleRate();
        int bytesPerFrame = audioFormat.getFrameSize();
        int totalBytes = (int) (audioStream.getFrameLength() * bytesPerFrame);
        ByteBuffer data = BufferUtils.createByteBuffer(totalBytes);
        byte[] dataArray = new byte[totalBytes];

        try {
            int bytesRead = audioStream.read(dataArray, 0, totalBytes);
            data.clear();
            data.put(dataArray, 0, bytesRead);
            data.flip();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Couldn't read bytes from audio stream!");
        }

        return new Wave(format, sampleRate, data);
    }

}
