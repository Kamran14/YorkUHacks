package com.kam.drivesafe;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.os.VibrationEffect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.Locale;
import android.os.Vibrator;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private static final String TAG = "NFC";
    private NfcAdapter nfcAdapter;
    private NfcManager nfcManager;
    private PendingIntent nfcPendingIntent;
    private TextView myText;
    private TextView value;
    private SensorManager sensorManager;
    private Vibrator mVibrator;
    private Double oldVal;
    private Double currVal;
    public static DecimalFormat DECIMAL_FORMATTER;


    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_main);
        myText = (TextView) findViewById(R.id.textView3);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        value = (TextView) findViewById(R.id.value);
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        symbols.setDecimalSeparator('.');
        DECIMAL_FORMATTER = new DecimalFormat("#.000", symbols);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        oldVal = null;
        currVal = null;

    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.d(TAG, "enableForegroundMode");
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED); // filter for tags
        IntentFilter[] writeTagFilters = new IntentFilter[]{tagDetected};
        nfcAdapter.enableForegroundDispatch(this, nfcPendingIntent, writeTagFilters, null);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            // get values for each axes X,Y,Z
            float magX = event.values[0];
            float magY = event.values[1];
            float magZ = event.values[2];
            double magnitude = Math.sqrt((magX * magX) + (magY * magY) + (magZ * magZ));

            value.setTextColor(Color.BLACK);
            value.setText(DECIMAL_FORMATTER.format(magnitude) + " \u00B5Tesla");

            if(oldVal == null)
                oldVal = magnitude;
            if((oldVal + 50) <= Double.parseDouble(DECIMAL_FORMATTER.format(magnitude)) || (oldVal - 50) <= Double.parseDouble(DECIMAL_FORMATTER.format(magnitude))){
                oldVal = magnitude;
                mVibrator.vibrate(2000);
            }

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Log.d(TAG, "onNewIntent");

        // check for NFC related actions


            if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
                Toast.makeText(this, "Got the NFC TAG", Toast.LENGTH_SHORT).show();
                myText.setText("{Data}");
                Toast.makeText(this, NfcAdapter.ACTION_NDEF_DISCOVERED.toString(), Toast.LENGTH_SHORT);
            /*ImageView lata = (ImageView) findViewById(R.id.);

            if(lata.getVisibility()== View.VISIBLE)
                lata.setVisibility(View.INVISIBLE);
            else{
                lata.setVisibility(View.VISIBLE);
            }*/

            }else{
                if(myText.getText().equals("TextView")){
                    myText.setText("No Tag detected");
                }
            }

    }
}