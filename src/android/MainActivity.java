package com.stanleyidesis.cordova.plugin;
// The native Toast API
import android.widget.Toast;
// Cordova-required packages
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.honeywell.aidc.*;

import android.app.Activity;
import android.os.Bundle;

import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;

import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity implements BarcodeReader.BarcodeListener {

     private AidcManager manager;
     private BarcodeReader reader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String package_name = getApplication().getPackageName();
//        setContentView(getApplication().getResources().getIdentifier("mainactivity", "layout", package_name));

         // Create the text view
        TextView textView = new TextView(this);
        textView.setTextSize(40);
        textView.setText("This is the text view");

        // Set the text view as the activity layout
        setContentView(textView);

         // create the AidcManager providing a Context and an
         // CreatedCallback implementation.
         AidcManager.create(this, new AidcManager.CreatedCallback() {

              @Override
             public void onCreated(AidcManager aidcManager) {
                 manager = aidcManager;
                 // use the manager to create a BarcodeReader with a session
                 // associated with the internal imager.
                 reader = manager.createBarcodeReader();

                 try {
                     // apply settings
                     reader.setProperty(BarcodeReader.PROPERTY_CODE_39_ENABLED, false);
                     reader.setProperty(BarcodeReader.PROPERTY_DATAMATRIX_ENABLED, true);

                     // set the trigger mode to automatic control
                     reader.setProperty(BarcodeReader.PROPERTY_TRIGGER_CONTROL_MODE,
                     
                         BarcodeReader.TRIGGER_CONTROL_MODE_AUTO_CONTROL);
                 } catch (UnsupportedPropertyException e) {
                     Toast.makeText(MainActivity.this, "Failed to apply properties",
                         Toast.LENGTH_SHORT).show();
                 }

                  Map<String, Object> properties = new HashMap<String, Object>();
                  // Set Symbologies On/Off
                  properties.put(BarcodeReader.PROPERTY_CODE_128_ENABLED, true);
                  properties.put(BarcodeReader.PROPERTY_GS1_128_ENABLED, true);
                  properties.put(BarcodeReader.PROPERTY_QR_CODE_ENABLED, true);
                  properties.put(BarcodeReader.PROPERTY_CODE_39_ENABLED, true);
                  properties.put(BarcodeReader.PROPERTY_DATAMATRIX_ENABLED, true);
                  properties.put(BarcodeReader.PROPERTY_UPC_A_ENABLE, true);
                  properties.put(BarcodeReader.PROPERTY_EAN_13_ENABLED, false);
                  properties.put(BarcodeReader.PROPERTY_AZTEC_ENABLED, false);
                  properties.put(BarcodeReader.PROPERTY_CODABAR_ENABLED, false);
                  properties.put(BarcodeReader.PROPERTY_INTERLEAVED_25_ENABLED, false);
                  properties.put(BarcodeReader.PROPERTY_PDF_417_ENABLED, false);
                  // Set Max Code 39 barcode length
                  properties.put(BarcodeReader.PROPERTY_CODE_39_MAXIMUM_LENGTH, 10);
                  // Turn on center decoding
                  properties.put(BarcodeReader.PROPERTY_CENTER_DECODE, true);
                  // Enable bad read response
                  properties.put(BarcodeReader.PROPERTY_NOTIFICATION_BAD_READ_ENABLED, true);
                  // Apply the settings
                  reader.setProperties(properties);

                 // register bar code event listener
                 reader.addBarcodeListener(MainActivity.this);

                 
                 try{
                     
                     reader.claim();
                    }catch(ScannerUnavailableException e){
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "Scanner unavailable", Toast.LENGTH_SHORT).show();
                    }
                try{
                    
                    reader.light(true);
                    reader.aim(true);
                    reader.decode(true);
                    reader.captureImage();

                }catch(ScannerNotClaimedException e){
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Scanner not claimed", Toast.LENGTH_SHORT).show();
                }catch(ScannerUnavailableException e){
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Scanner unavailable", Toast.LENGTH_SHORT).show();
                }       
             }
         });
     }

      @Override
     public void onResume() {
         super.onResume();
         if (reader != null) {
             try {
                 reader.claim();

                 reader.light(true);
                 reader.aim(true);
                 reader.decode(true);

             } catch (ScannerUnavailableException e) {
                 e.printStackTrace();
                 Toast.makeText(this, "Scanner unavailable", Toast.LENGTH_SHORT).show();
             }catch(ScannerNotClaimedException e){
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Scanner not claimed", Toast.LENGTH_SHORT).show();
                }       
         }
     }

      @Override
     public void onPause() {
         super.onPause();
         if (reader != null) {
             // release the scanner claim so we don't get any scanner
             // notifications while paused.
             reader.release();
         }
     }

      @Override
     public void onDestroy() {
         super.onDestroy();
         if (reader != null) {
             // unregister barcode event listener
             reader.removeBarcodeListener(this);

             // close BarcodeReader to clean up resources.
             // once closed, the object can no longer be used.
             reader.close();
         }
         if (manager != null) {
             // close AidcManager to disconnect from the scanner service.
             // once closed, the object can no longer be used.
             manager.close();
         }
     }

      @Override
     public void onBarcodeEvent(final BarcodeReadEvent event) {
         runOnUiThread(new Runnable() {
              @Override
             public void run() {
                 String barcodeData = event.getBarcodeData();
                 String timestamp = event.getTimestamp();
                
                 Toast.makeText(MainActivity.this, barcodeData + " " + timestamp,
                     Toast.LENGTH_SHORT).show();
                 // update UI to reflect the data
             }
         });
     }

      @Override
     public void onFailureEvent(final BarcodeFailureEvent event) {
         runOnUiThread(new Runnable() {
              @Override
             public void run() {
                 Toast.makeText(MainActivity.this, "Barcode read failed",
                     Toast.LENGTH_SHORT).show();
             }
         });
     }
 }
