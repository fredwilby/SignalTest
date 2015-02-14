/*
 * Copyright (c) Fred Wilby 2015. All rights reserved
 */

public class ToneEncoder
{
    public static final int BITS_PER_CHAR = 16;

    private static final short MAX_AMPLITUDE = 32767; // Maximum Amplitude for 16-bit PCM


    private boolean[] data;
    private final double[] sample;
    private final int numBits;
    private final SignalProperties props;

    /**
     * Creates a new buffer that will encode the given number of bits.
     */
    public ToneEncoder(boolean[] bits, SignalProperties props)
    {
        numBits = bits.length;
        data = bits;
        this.props = props;
        sample = new double[numBits * props.getSamplesPerBit()];

        for(int x = 0; x < bits.length; x++)
        {
            setBit(x, bits[x]);
        }

    }

    /**
     * Sets a bit in the string
     * @param bit position in bit string 0 <= bit < numBits
     * @param value should we encode a high bit?
     *
     * @throws java.lang.ArrayIndexOutOfBoundsException if bit is outside of buffer
     */
    public void setBit(int bit, boolean value)
    {
        if(bit < 0 || bit >= numBits)
        {
            throw new ArrayIndexOutOfBoundsException("Invalid Bit Specified: "+bit+"; bit should be in range [0, "+numBits+")");
        }

        int startInd = bit * props.getSamplesPerBit();

        for(int ind = startInd; ind < startInd+props.getSamplesPerBit();  ind++)
        {
            double freq = props.getFrequency(value? 1 : 0);

           /* Sine wave of desired frequency */
            sample[ind] = Math.sin(2*Math.PI*(ind-startInd)*freq/props.getSampleRate());

        }


    }

    /**
     * Encodes a string into the buffer as little-endian 16-bit unicode characters
     * @param data string to encode
     * @param startBit position in the bit string to start
     *
     * @throws java.lang.ArrayIndexOutOfBoundsException if string is too long for buffer or startBit is out of bounds
     */
    public void encodeString(String data, int startBit)
    {
        if(startBit + data.length() * 16 > numBits)
        {
            throw new ArrayIndexOutOfBoundsException("String is too long to fit in buffer");
        }

        for(int ch = 0; ch < data.length(); ch++)
        {
            char c = data.charAt(ch);

            /* Iterate through each bit in char */
            for(int shift = 0; shift < BITS_PER_CHAR; shift++)
            {
                /* Set correct bit based on char's value */
                setBit(startBit + ch*BITS_PER_CHAR + shift, ((c & 1<<shift) == 1));
            }

        }


    }

    /**
     * Returns the encoded buffer
     */
    public double[] getSample()
    {
        return sample;
    }




}
