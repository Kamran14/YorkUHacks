package com.kam.drivesafe;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.nfc.NfcManager;
import android.nfc.Tag;
import android.nfc.TagLostException;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.os.Debug;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatViewInflater;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.Console;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    public Intent mIntent;
    private Tag myTag;
    private static final String TAG = "NFC";
    private NfcAdapter nfcAdapter;
    private NfcManager nfcManager;
    private PendingIntent nfcPendingIntent;
    private TextView myText;


        @Override
        public void onCreate(Bundle savedInstance){
            super.onCreate(savedInstance);
            setContentView(R.layout.activity_main);
            myText = (TextView) findViewById(R.id.textView3);
            nfcAdapter = NfcAdapter.getDefaultAdapter(this);
            nfcPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
            }

        @Override
        protected void onResume(){
                super.onResume();
                Log.d(TAG, "enableForegroundMode");

                // foreground mode gives the current active application priority for reading scanned tags

                IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED); // filter for tags
                IntentFilter[] writeTagFilters = new IntentFilter[]{tagDetected};
                nfcAdapter.enableForegroundDispatch(this, nfcPendingIntent, writeTagFilters, null);

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