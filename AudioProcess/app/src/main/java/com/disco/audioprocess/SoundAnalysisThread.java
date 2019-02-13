package com.disco.audioprocess;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.nio.ByteBuffer;

import uk.me.berndporr.kiss_fft.KISSFastFourierTransformer;


/**
 * Analysis Sound in real time.
 */

public class SoundAnalysisThread extends Thread {
    private String TAG = "DISCO_AUDIO-----"  + this.getClass().getSimpleName();

    private Handler handler;
    private FFT fft = new FFT();

//    private static final int[] SAMPLE_RATES_LIST = {11025, 8000, 22050, 44100, 16000};
    private static final int[] SAMPLE_RATES_LIST = {8000};//{44100, 22050, 16000, 11025, 8000};
    private static final int[] SAMPLE_COUNT = {1024};//{32 * 1024,
            //16 * 1024, 8 * 1024, 8 * 1024, 4 * 1024};

    private int sampleRate = 44100;
    private int sampleCount = 32 * 1024;
    private Sound sound = new Sound();

    private double currentFrequency;
    private double currentVolume;

    private KISSFastFourierTransformer myfftTransformer;

    private AudioRecord audioRecord;

    SoundAnalysisThread(Handler handler) {
        this.handler = handler;
        initAudioRecord();
    }

    public static double toDouble(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getDouble();
    }

    /**
     * Initialize audio recording when handler is called.
     */
    private void initAudioRecord() {
        Log.i(TAG, "initAudioRecord");
        for (int i = 0; i < SAMPLE_RATES_LIST.length; i++) {

            audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATES_LIST[i],
                    AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT,
                    AudioRecord.getMinBufferSize(SAMPLE_RATES_LIST[i],
                    AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT));

            Log.i(TAG, "TRY STATE_INITIALIZE " + audioRecord.getState());
            if (audioRecord.getState() == AudioRecord.STATE_INITIALIZED) {
                Log.i(TAG, "STATE_INITIALIZED" + String.valueOf(SAMPLE_RATES_LIST[i]));
                sampleRate = SAMPLE_RATES_LIST[i];
                sampleCount = SAMPLE_COUNT[i];
                break;
            }
        }
    }

    @Override
    public void run() {
        super.run();
        Log.i(TAG, "run" + sampleCount);
        audioRecord.startRecording();
        byte[] bufferRead = new byte[sampleCount];
        int length;

        while ((length = audioRecord.read(bufferRead, 0, sampleCount)) > 0) {

            currentFrequency = fft.getFrequency(bufferRead, sampleRate, sampleCount);
            currentVolume = VoiceUtil.getVolume(bufferRead, length);

//            myfftTransformer = new KISSFastFourierTransformer();
//            double[] indata = toDouble(bufferRead);
//            myfftTransformer.
//            Complex[] outdata = myfftTransformer.transform(bufferRead);

            sound.mFrequency = currentFrequency;
            sound.mVolume = currentVolume;
            Message message = Message.obtain();
            message.obj = sound;
            message.what = MainActivity.SOUND_MESSAGE;

            handler.sendMessage(message);

            Log.i(TAG, "currentFrequency" + currentFrequency + "---" + currentVolume);
//            if (currentFrequency > 0) {
//                try {
//                    if (audioRecord.getState() == AudioRecord.STATE_INITIALIZED) {
//                        audioRecord.stop();
//                        audioRecord.release();
//                    }
//                    Thread.sleep(1);
//                    initAudioRecord();
//                    if (audioRecord.getState() == AudioRecord.STATE_INITIALIZED) {
//                        audioRecord.startRecording();
//                    }
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
        }
    }

    public void close() {
        if (audioRecord != null && audioRecord.getState() == AudioRecord.STATE_INITIALIZED) {
            audioRecord.stop();
            audioRecord.release();
        }

    }
}


