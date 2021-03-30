package audio;

import org.lwjgl.openal.AL10;
import utils.math.Vector3f;

public class Source {

    private final int sourceId;

    public Source(float rollOfFactor, float referenceDistance, float maxDistance) {
        sourceId = AL10.alGenSources();
        AL10.alSourcef(sourceId, AL10.AL_ROLLOFF_FACTOR, rollOfFactor);
        AL10.alSourcef(sourceId, AL10.AL_REFERENCE_DISTANCE, referenceDistance);
        AL10.alSourcef(sourceId, AL10.AL_MAX_DISTANCE, maxDistance);
    }

    public void play(int buffer) {
        stop();
        AL10.alSourcei(sourceId, AL10.AL_BUFFER, buffer);
        continuePlaying();
    }

    public void setVelocity(float x, float y, float z) {
        AL10.alSource3f(sourceId, AL10.AL_VELOCITY, x, y, z);
    }

    public void setLooping(boolean loop) {
        AL10.alSourcei(sourceId, AL10.AL_LOOPING, loop ? AL10.AL_TRUE : AL10.AL_FALSE);
    }

    public void setGain(float gain) {
        AL10.alSourcef(sourceId, AL10.AL_GAIN, gain);
    }

    public void setPitch(float pitch) {
        AL10.alSourcef(sourceId, AL10.AL_PITCH, pitch);
    }

    public void setPosition(Vector3f position) {
        AL10.alSource3f(sourceId, AL10.AL_POSITION, position.x, position.y, position.z);
    }

    public boolean isPlaying() {
        return AL10.alGetSourcei(sourceId, AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING;
    }

    public void continuePlaying() {
        AL10.alSourcePlay(sourceId);
    }

    public void pause() {
        AL10.alSourcePause(sourceId);
    }

    public void stop() {
        AL10.alSourceStop(sourceId);
    }

    public void remove() {
        AL10.alDeleteSources(sourceId);
    }

}
