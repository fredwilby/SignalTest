/*
 * Copyright (c) Fred Wilby 2015. All rights reserved
 */

/**
 * Stores settings about the data signal.
 */
public final class SignalProperties
{
    private final int sampleRate, samplesPerBit;
    private int[] frequencies;

    private SignalProperties(SignalPropertiesBuilder spb)
    {
        this.sampleRate = spb.sampleRate;
        this.samplesPerBit = spb.samplesPerBit;
        this.frequencies = spb.frequencies;
    }


    public int getSampleRate()
    {
        return sampleRate;
    }

    public int getSamplesPerBit()
    {
        return samplesPerBit;
    }

    public int getFrequency(int value)
    {
        return frequencies[value];
    }

    public static class SignalPropertiesBuilder
    {
        /* Encoding Defaults */
        private static final int SAMPLES_PER_BIT = 10; // needs experimental results to tune
        private static final int SAMPLE_RATE = 44100; // 44.1 kHz
        /* I have no idea what the frequencies should be, or if it even matters */
        private static final int HIGH_BIT_FREQ = 1000; // Hz
        private static final int LOW_BIT_FREQ = 4000; // Hz

        private int sampleRate, samplesPerBit;
        private int[] frequencies;

        public SignalPropertiesBuilder()
        {
            this.sampleRate = SAMPLE_RATE;
            this.samplesPerBit = SAMPLES_PER_BIT;
            frequencies = new int[] { LOW_BIT_FREQ, HIGH_BIT_FREQ };
        }

        public SignalPropertiesBuilder sampleRate(int sr)
        {
            this.sampleRate = sr;
            return this;
        }

        public SignalPropertiesBuilder samplesPerBit(int spb)
        {
            this.samplesPerBit = spb;
            return this;
        }

        public SignalPropertiesBuilder frequencies(int[] freq)
        {
            this.frequencies = freq;
            return this;
        }

        public SignalProperties build()
        {
            return new SignalProperties(this);
        }


    }

}
