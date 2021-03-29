package utils.loader;

import java.nio.ByteBuffer;

public class Wave {

    private final int format, freq;
    private final ByteBuffer data;

    public Wave(int format, int freq, ByteBuffer data) {
        this.format = format;
        this.freq = freq;
        this.data = data;
    }

    public int getFormat() {
        return format;
    }

    public int getFreq() {
        return freq;
    }

    public ByteBuffer getData() {
        return data;
    }

}
