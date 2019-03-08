package com.disco.audioprocess;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import java.util.Timer;
import java.util.TimerTask;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.unity3d.player.UnityPlayer;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;
import ca.uol.aig.fftpack.RealDoubleFFT;

public class TestVisActivity extends Activity implements OnClickListener{
    private String TAG = "DISCO_AUDIO-----" + this.getClass().getSimpleName();
    // vocal frequency from 50Hz(G#1) to 850Hz(G#5)

    int sampleRate = 44100; //sample rate in Hz
    //BUG: this setting may cause compatibility issues
    int sampleLength = 50;//ms
    int overSampleRatio = 3;//ms
    int sampleSize;
    double sample2freq;
    int sampleBlockSize;

    //TarsosDSP
    PitchDetectionHandler pdh = new PitchDetectionHandler(){
        @Override
        public void handlePitch(PitchDetectionResult result, AudioEvent e){
            final float pitchInHz = result.getPitch();
            runOnUiThread(new Runnable(){
                @Override
                public void run(){
                    TextView text = (TextView) findViewById(R.id.textView3);
                    double p  = pitchInHz;
                    if(p<=-1.0){
                        p = 150.0;
                    }
                    UnityPlayer.UnitySendMessage("BirdForeground","receiveData",""+ (int)p);
                    text.setText(""+p);
                }
            });
        }
    };

    int channelConfiguration = AudioFormat.CHANNEL_IN_MONO;
    int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;

    private RealDoubleFFT transformer;
    Button startStopButton;
    boolean started = false;

    RecordAudio recordTask;

    TextView maxFreqView;
    ImageView imageView;
    Bitmap bitmap;
    Canvas canvas;
    Paint paint;

    /**
     * Service
     */

//    Messenger mMessenger;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_vis);
        startStopButton = (Button) this.findViewById(R.id.start_stop_btn);
        startStopButton.setOnClickListener(this);
        maxFreqView = (TextView) this.findViewById(R.id.maxFreqView);
        sampleBlockSize = overSampleRatio * sampleRate * sampleLength / 1000;
        sample2freq = 1000.0/ overSampleRatio /sampleLength/2;
        sampleSize = sampleBlockSize * 2;
        transformer = new RealDoubleFFT(sampleBlockSize);

        imageView = (ImageView) this.findViewById(R.id.imageView1);
        int bitmapWidth = 256;
        int bitmapHeight = 300;

        bitmap = Bitmap.createBitmap((int)bitmapWidth,(int)bitmapHeight,Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        canvas.scale(1, -1,bitmapWidth/2, bitmapHeight/2);
        paint = new Paint();
        paint.setColor(Color.GREEN);
        imageView.setImageBitmap(bitmap);

        AudioDispatcher dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(44100,4096,0);
        AudioProcessor p = new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 44100, 4096, pdh);
        dispatcher.addAudioProcessor(p);

        new Thread(dispatcher, "Audio Dispatcher").start();
//        bindService(new Intent(this, ConnectionService.class), serviceConnection, Context.BIND_AUTO_CREATE);
    }

//    Messenger messenger;
//    private ServiceConnection serviceConnection = new ServiceConnection() {
//        public void onServiceConnected(ComponentName className, IBinder iBinder) {
//            messenger = new Messenger(iBinder);
//        }
//
//        public void onServiceDisconnected(ComponentName className) {
//        }
//    };

    private class RecordAudio extends AsyncTask<Void, double[], Void> {
        protected AudioRecord audioRecord;
        protected short[] buffer = new short[sampleBlockSize];
        protected double[] toTransform = new double[sampleBlockSize];
        protected Timer timer;

        protected void onCancelled(boolean p){
                timer.cancel();
                Log.d("timer","stopped");
                audioRecord.stop();
                audioRecord.release(); //release resources
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                int bufferSize = 9 * AudioRecord.getMinBufferSize(sampleRate,
                        channelConfiguration, audioEncoding);
                Log.d(getClass().getName(),"buffer size="+bufferSize);
                audioRecord = new AudioRecord(
                        MediaRecorder.AudioSource.UNPROCESSED, sampleRate,
                        channelConfiguration, audioEncoding, bufferSize);

                audioRecord.startRecording();
                // sampling timer
                timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        try{
                            //Log.d("AudioRecord-sampling", "new round of sampling");
                            int bufferReadResult = audioRecord.read(buffer, 0, sampleBlockSize);

                            for (int i = 0; i < sampleBlockSize && i < bufferReadResult; i++) {
                                toTransform[i] = (double) buffer[i] / 32768.0; // signed 16 bit
                            }

                            transformer.ft(toTransform);
                            publishProgress(toTransform);
                        }
                        catch(Throwable e){
                            Log.e("AudioRecord-sampling", "Sampling Failed");
                        }
                    }
                }, 0 , sampleLength);

            } catch (Throwable t) {
                Log.e("AudioRecord", "Recording Failed");
            }
            return null;
        }

        protected void onProgressUpdate(double[]... toTransform) {
            canvas.drawColor(Color.BLACK);
            double maxfreq = -100;
            int maxindex = 0;
            for (int i = 0; i < toTransform[0].length; i++) {
                int x = i;
                int downy = 0;
                int upy = (int) ((toTransform[0][i] * 10));
                //if(upy<10) downy = 10;
                canvas.drawLine(x, downy, x, upy, paint);
                if (toTransform[0][i] > maxfreq){
                    maxfreq = toTransform[0][i];
                    maxindex = i;
                }
//                Log.d(TAG, "onProgressUpdate: " + String.valueOf(toTransform[0][i]));
            }
//            Log.d(TAG, "onProgressUpdate: max index" + String.valueOf(maxindex));
            //Log.d(TAG, "onProgressUpdate: max frequency" + String.valueOf(maxfreq));
            if (maxfreq < 1){
//                maxFreqView.setText("Only noise");
            }
            else {
                maxindex = (int)((maxindex) * sample2freq);
//                sendMessageToService(maxindex);
                maxFreqView.setText(String.valueOf(maxindex) + " Hz");
                //UnityPlayer.UnitySendMessage("BirdForeground","receiveData",""+maxindex);
            }
            imageView.invalidate();
        }

//        private void sendMessageToService(int intvaluetosend) {
//            try {
//                Message msg = Message.obtain(null, 1, intvaluetosend, 0);
////                msg.replyTo = mMessenger;
//                mMessenger.send(msg);
//            }
//            catch (RemoteException e) {
//
//            }
//        }
    }



    public void onClick(View v) {
        if (started) {
            started = false;
            startStopButton.setText("Start");
            recordTask.onCancelled(true);
            boolean p = recordTask.cancel(true);
            Log.d("recordtask cancel", ""+p);
        } else {
            started = true;
            startStopButton.setText("Stop");
            recordTask = new RecordAudio();
            Log.e("onclick","pressed start");
            recordTask.execute();
        }
    }

}