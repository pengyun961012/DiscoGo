package com.disco.audioprocess;

import android.app.Service;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import ca.uol.aig.fftpack.RealDoubleFFT;

public class ConnectionService extends Service {
    private String TAG = "DISCO_AUDIO-----" + this.getClass().getSimpleName();

    /**
     * Binder
     */
    private final IBinder mBinder = new LocalBinder();
    public class LocalBinder extends Binder {
        ConnectionService getService() {
            // Return this instance of LocalService so clients can call public methods
            return ConnectionService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /**
     * Field
     */
    int frequency = 8000;
    int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_MONO;
    int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;

    private RealDoubleFFT transformer;
    RecordAudio recordTask;
    int blockSize = 256;
    boolean started = true;

    // When service is started
    @Override
    public void onCreate() {
        // We first start the Handler
        super.onCreate();
        Log.d(TAG, "onStartCommand: start service");
        transformer = new RealDoubleFFT(blockSize);
        recordTask = new RecordAudio();
        recordTask.execute();
//        handler.removeCallbacks(sendData);
//        handler.postDelayed(sendData, 1000);
//        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        recordTask.cancel(true);
    }

    /**
     * Recorder
     */

    private class RecordAudio extends AsyncTask<Void, double[], Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
//                Log.d(TAG, "doInBackground: recorder started");
                int bufferSize = AudioRecord.getMinBufferSize(frequency,
                        channelConfiguration, audioEncoding);
                AudioRecord audioRecord = new AudioRecord(
                        MediaRecorder.AudioSource.DEFAULT, frequency,
                        channelConfiguration, audioEncoding, bufferSize);

                short[] buffer = new short[blockSize];
                double[] toTransform = new double[blockSize];
                audioRecord.startRecording();
                while (started) {
                    int bufferReadResult = audioRecord.read(buffer, 0, blockSize);

                    for (int i = 0; i < blockSize && i < bufferReadResult; i++) {
                        toTransform[i] = (double) buffer[i] / 32768.0; // signed 16 bit
                    }

                    transformer.ft(toTransform);
//                    Log.d(TAG, "doInBackground: " + String.valueOf(toTransform[0]));

                    publishProgress(toTransform);
                    Thread.sleep(100);
                }
                audioRecord.stop();
            } catch (Throwable t) {
                Log.e(TAG, "Recording Failed");
            }
            return null;
        }

        protected void onProgressUpdate(double[]... toTransform) {
//            Log.d(TAG, "onProgressUpdate: start progress update");
            double maxfreq = -100;
            int maxindex = 0;
            for (int i = 0; i < toTransform[0].length; i++) {
                int x = i;
                int downy = (int) (100 - (toTransform[0][i] * 10));
                int upy = 100;
                if (toTransform[0][i] > maxfreq) {
                    maxfreq = toTransform[0][i];
                    maxindex = i;
                }
            }
//            Log.d(TAG, "onProgressUpdate: max frequency" + String.valueOf(maxfreq));
            maxindex *= 16;
            frequency = maxindex;
            Log.d(TAG, "onProgressUpdate: " + String.valueOf(frequency));
        }
    }

    /**
     * send
     */
    private final Handler handler = new Handler();

    private int numIntent;

    // It's the code we want our Handler to execute to send data
    private Runnable sendData = new Runnable() {
        // the specific method which will be executed by the handler
        public void run() {
            numIntent++;

            // sendIntent is the object that will be broadcast outside our app
            Intent sendIntent = new Intent();

            // We add flags for example to work from background
            sendIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION|Intent.FLAG_FROM_BACKGROUND|Intent.FLAG_INCLUDE_STOPPED_PACKAGES	);

            // SetAction uses a string which is an important name as it identifies the sender of the itent and that we will give to the receiver to know what to listen.
            // By convention, it's suggested to use the current package name
            sendIntent.setAction("com.test.sendintent.IntentToUnity");

            // Here we fill the Intent with our data, here just a string with an incremented number in it.
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Intent "+numIntent);
            // And here it goes ! our message is send to any other app that want to listen to it.
            sendBroadcast(sendIntent);

            // In our case we run this method each second with postDelayed
            handler.removeCallbacks(this);
            handler.postDelayed(this, 1000);
        }
    };
}