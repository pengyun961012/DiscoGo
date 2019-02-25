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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import ca.uol.aig.fftpack.RealDoubleFFT;

public class TestVisActivity extends Activity implements OnClickListener{
    private String TAG = "DISCO_AUDIO-----" + this.getClass().getSimpleName();
    int frequency = 8000;
    int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_MONO;
    int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;


    private RealDoubleFFT transformer;
    int blockSize = 256;
    Button startStopButton;
    boolean started = false;

    RecordAudio recordTask;

    TextView maxFreqView;
    ImageView imageView;
    Bitmap bitmap;
    Canvas canvas;
    Paint paint;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_vis);
        startStopButton = (Button) this.findViewById(R.id.start_stop_btn);
        maxFreqView = (TextView) this.findViewById(R.id.maxFreqView);
        startStopButton.setOnClickListener(this);

        transformer = new RealDoubleFFT(blockSize);

        imageView = (ImageView) this.findViewById(R.id.imageView1);
        bitmap = Bitmap.createBitmap((int)256,(int)100,Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        paint = new Paint();
        paint.setColor(Color.GREEN);
        imageView.setImageBitmap(bitmap);
    }

    private class RecordAudio extends AsyncTask<Void, double[], Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
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

                    publishProgress(toTransform);
                    Thread.sleep(500);
                }
                audioRecord.stop();
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
                int downy = (int) (100 - (toTransform[0][i] * 10));
                int upy = 100;
                canvas.drawLine(x, downy, x, upy, paint);
                if (toTransform[0][i] > maxfreq){
                    maxfreq = toTransform[0][i];
                    maxindex = i;
                }
//                Log.d(TAG, "onProgressUpdate: " + String.valueOf(toTransform[0][i]));
            }
//            Log.d(TAG, "onProgressUpdate: max index" + String.valueOf(maxindex));
            Log.d(TAG, "onProgressUpdate: max frequency" + String.valueOf(maxfreq));
            if (maxfreq < 1){
                maxFreqView.setText("Only noise");
            }
            else {
                maxindex *= 16;
                maxFreqView.setText(String.valueOf(maxindex) + " Hz");
            }
            imageView.invalidate();
        }
    }



    public void onClick(View v) {
        if (started) {
            started = false;
            startStopButton.setText("Start");
            recordTask.cancel(true);
        } else {
            started = true;
            startStopButton.setText("Stop");
            recordTask = new RecordAudio();
            recordTask.execute();
        }
    }
}