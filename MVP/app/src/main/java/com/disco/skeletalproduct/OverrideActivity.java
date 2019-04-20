package com.disco.skeletalproduct;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.TarsosDSPAudioFormat;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;
import be.tarsos.dsp.writer.WriterProcessor;

public class OverrideActivity extends UnityPlayerActivity {
    private String TAG = "DISCO_SKELETAL-----" + this.getClass().getSimpleName();
    private String fileName;
    private String[] levelList;
    private int score;
    private int token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // call UnityPlayerActivity.onCreate()
        super.onCreate(savedInstanceState);
        // print debug message to logcat
        Log.d(TAG, "onCreate called!");
        Intent intent = getIntent();
        fileName = intent.getStringExtra("fileName");
        Log.d(TAG, "onCreate: filename " + fileName);

        levelList = new String[3];
        levelList[0] = "alphabet";
        levelList[1] = "JustTheWayYouAre";
        levelList[2] = "birthday";
    }

    @Override
    public void onBackPressed()
    {
        // instead of calling UnityPlayerActivity.onBackPressed() we just ignore the back button event
//        super.onBackPressed();
//        Log.d("OverrideActivity_", "onBackPressed: ");
    }

    public void stopRecorder(int score, int token){
        if (!SongListAdapter.dispatcher.isStopped()){
            SongListAdapter.dispatcher.stop();
            this.score = score;
            this.token = token;
            changeFileName();
        }
        if (SongListAdapter.dispatcher.isStopped()){
            Log.d(TAG, "testMethod: unity function called in override");
        }

    }

    public void startRecorder(){
        SongListAdapter.dispatcher.run();
//        SongListAdapter.dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(44100,4096,0);
//        TarsosDSPAudioFormat format = new TarsosDSPAudioFormat( TarsosDSPAudioFormat.Encoding.PCM_SIGNED, 44100, 16, 1, 2*1, 44100, ByteOrder.BIG_ENDIAN.equals(ByteOrder.nativeOrder()));
//        File wavfile = new File(getFilesDir(), "temp");
//        Log.e("file created at",getFilesDir().getAbsolutePath());
//        try{
//            RandomAccessFile recordFile = new RandomAccessFile(wavfile, "rw");
//            AudioProcessor p1 = new WriterProcessor(format, recordFile);
//            SongListAdapter.dispatcher.addAudioProcessor(p1);
//        }
//        catch(Throwable e){
//            e.printStackTrace();
//        }
//        AudioProcessor p2 = new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 44100, 4096, pdh);
//        SongListAdapter.dispatcher.addAudioProcessor(p2);
//
//        new Thread(SongListAdapter.dispatcher, "Audio Dispatcher").start();
    }

    public void levelUp(int level){
        if (level + 1 >= levelList.length){
            return;
        }
        UnityPlayer.UnitySendMessage("GameController","receiveData", levelList[level + 1]);
    }

    private void changeFileName(){
        String currentFileName = "temp";
        Log.i("Current file name", currentFileName);

        File directory = getFilesDir();
        File from      = new File(directory, currentFileName);
        File to        = new File(directory, fileName + "_" + Integer.toString(score));
        try {
            rawToWave(from, to);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("was converter", "failed!");
        }
    }

    private static void rawToWave(final File rawFile, final File waveFile) throws IOException {

        byte[] rawData = new byte[(int) rawFile.length()];
        DataInputStream input = null;
        try {
            input = new DataInputStream(new FileInputStream(rawFile));
            input.read(rawData);
        } finally {
            if (input != null) {
                input.close();
            }
        }

        DataOutputStream output = null;
        try {
            output = new DataOutputStream(new FileOutputStream(waveFile));
            // WAVE header
            // see http://ccrma.stanford.edu/courses/422/projects/WaveFormat/
            writeString(output, "RIFF"); // chunk id
            writeInt(output, 36 + rawData.length); // chunk size
            writeString(output, "WAVE"); // format
            writeString(output, "fmt "); // subchunk 1 id
            writeInt(output, 16); // subchunk 1 size
            writeShort(output, (short) 1); // audio format (1 = PCM)
            writeShort(output, (short) 1); // number of channels
            writeInt(output, 44100); // sample rate
            writeInt(output, 44100 * 2); // byte rate
            writeShort(output, (short) 2); // block align
            writeShort(output, (short) 16); // bits per sample
            writeString(output, "data"); // subchunk 2 id
            writeInt(output, rawData.length); // subchunk 2 size
            // Audio data (conversion big endian -> little endian)
            short[] shorts = new short[rawData.length / 2];
            ByteBuffer.wrap(rawData).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shorts);
            ByteBuffer bytes = ByteBuffer.allocate(shorts.length * 2);
            for (short s : shorts) {
                bytes.putShort(s);
            }

            output.write(fullyReadFileToBytes(rawFile));
        } finally {
            if (output != null) {
                output.close();
            }
        }
    }


    static byte[] fullyReadFileToBytes(File f) throws IOException {
        int size = (int) f.length();
        byte bytes[] = new byte[size];
        byte tmpBuff[] = new byte[size];
        FileInputStream fis= new FileInputStream(f);
        try {

            int read = fis.read(bytes, 0, size);
            if (read < size) {
                int remain = size - read;
                while (remain > 0) {
                    read = fis.read(tmpBuff, 0, remain);
                    System.arraycopy(tmpBuff, 0, bytes, size - remain, read);
                    remain -= read;
                }
            }
        }  catch (IOException e){
            throw e;
        } finally {
            fis.close();
        }

        return bytes;
    }
    private static void writeInt(final DataOutputStream output, final int value) throws IOException {
        output.write(value >> 0);
        output.write(value >> 8);
        output.write(value >> 16);
        output.write(value >> 24);
    }

    private static void writeShort(final DataOutputStream output, final short value) throws IOException {
        output.write(value >> 0);
        output.write(value >> 8);
    }

    private static void writeString(final DataOutputStream output, final String value) throws IOException {
        for (int i = 0; i < value.length(); i++) {
            output.write(value.charAt(i));
        }
    }
}
