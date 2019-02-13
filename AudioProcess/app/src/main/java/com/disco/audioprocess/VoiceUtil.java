package com.disco.audioprocess;
/**
 * Voice dB calculation util.
 */

public class VoiceUtil {
    /**
     * Get dB of the voice.
     */
    public static double getVolume(byte[] bufferRead, int length) {
        int volume = 0;

        for (int i = 0; i < bufferRead.length; i++) {
            volume += bufferRead[i] * bufferRead[i];
        }

        double mean = volume / (float) length;
        return mean;//10 * Math.log10(mean);
    }
}
