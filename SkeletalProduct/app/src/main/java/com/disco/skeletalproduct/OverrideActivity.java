package com.disco.skeletalproduct;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;

import com.unity3d.player.UnityPlayerActivity;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class OverrideActivity extends UnityPlayerActivity {
    private String TAG = "DISCO_SKELETAL-----" + this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // call UnityPlayerActivity.onCreate()
        super.onCreate(savedInstanceState);
        // print debug message to logcat
        Log.d("OverrideActivity_", "onCreate called!");
    }

    @Override
    public void onBackPressed()
    {
        // instead of calling UnityPlayerActivity.onBackPressed() we just ignore the back button event
//        super.onBackPressed();
//        Log.d("OverrideActivity_", "onBackPressed: ");
    }

    public void stopRecorder(){
        SongListAdapter.dispatcher.stop();
        if (SongListAdapter.dispatcher.isStopped()){
            Log.d(TAG, "testMethod: unity function called in override");
        }
        // mix two files

    }

    private void mixFiles(){

        try {

            InputStream is1 = getResources().openRawResource(R.raw.test1);

            List<Short> music1 = createMusicArray(is1);



            InputStream is2 = getResources().openRawResource(R.raw.test2);

            List<Short> music2 = createMusicArray(is2);




            completeStreams(music1,music2);

            short[] music1Array = buildShortArray(music1);

            short[] music2Array = buildShortArray(music2);


            short[] output = new short[music1Array.length];

            for(int i=0; i < output.length; i++){



                float samplef1 = music1Array[i] / 32768.0f;

                float samplef2 = music2Array[i] / 32768.0f;


                float mixed = samplef1 + samplef2;

                // reduce the volume a bit:

                mixed *= 0.8;

                // hard clipping

                if (mixed > 1.0f) mixed = 1.0f;

                if (mixed < -1.0f) mixed = -1.0f;

                short outputSample = (short)(mixed * 32768.0f);





                output[i] = outputSample;

            }

            saveToFile(output);

        } catch (Resources.NotFoundException e) {

            // TODO Auto-generated catch block

            e.printStackTrace();

        } catch (IOException e) {

            // TODO Auto-generated catch block

            e.printStackTrace();

        }

    }

    private void completeStreams(List<Short> music1, List<Short> music2) {
    }

    private List<Short> createMusicArray(InputStream is2) {

    }

    public static double[] load16BitPCMRawDataFileAsDoubleArray(File file) {
        InputStream in = null;
        if (file.isFile()) {
            long size = file.length();
            try {
                in = new FileInputStream(file);
                return readStreamAsDoubleArray(in, size);
            } catch (Exception e) {
            }
        }
        return null;
    }

    public static double[] readStreamAsDoubleArray(InputStream in, long size)
            throws IOException {
        int bufferSize = (int) (size / 2);
        double[] result = new double[bufferSize];
        DataInputStream is = new DataInputStream(in);
        for (int i = 0; i < bufferSize; i++) {
            result[i] = is.readShort() / 32768.0;
        }
        return result;
    }
    /**

     * Dealing with big endian streams

     * @param byte0

     * @param byte1

     * @return a shrt with the two bytes swapped

     */

    private static short swapBytes(byte byte0, byte byte1){

        return (short)((byte1 & 0xff) << 8 | (byte0 & 0xff));

    }



    /**

     * From file to byte[] array

     * @param sample

     * @param swap should swap bytes?

     * @return

     * @throws IOException

     */

    public static byte[] sampleToByteArray(File sample, boolean swap) throws IOException{

        ByteArrayOutputStream baos = new ByteArrayOutputStream();



        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(sample));

        int BUFFERSIZE = 4096;

        byte[] buffer = new byte[BUFFERSIZE];

        while(bis.read(buffer) != - 1){

            baos.write(buffer);

        }

        byte[] outputByteArray = baos.toByteArray();

        bis.close();

        baos.close();



        if(swap){

            for(int i=0; i < outputByteArray.length - 1; i=i+2){

                byte byte0 = outputByteArray[i];

                outputByteArray[i] = outputByteArray[i+1];

                outputByteArray[i+1] = byte0;

            }

        }



        return outputByteArray;

    }



    /**

     * Read a file and returns its contents as array of shorts

     * @param sample the sample file

     * @param swap true if we should swap the bytes of short (reading a little-endian file), false otherwise (reading a big-endian file)

     * @return

     * @throws IOException

     */

    public static short[] sampleToShortArray(File sample, boolean swap) throws IOException{

        short[] outputArray = new short[(int)sample.length()/2];





        byte[] outputByteArray = sampleToByteArray(sample,false);





        for(int i=0, j=0; i < outputByteArray.length; i+= 2, j++){

            if(swap){

                outputArray[j] = swapBytes(outputByteArray[i], outputByteArray[i + 1]);

            }

            else{

                outputArray[j] = swapBytes(outputByteArray[i + 1], outputByteArray[i]);

            }

        }

        return outputArray;

    }
}
