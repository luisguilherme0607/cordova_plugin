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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.content.Context;

public class ToastyPlugin extends CordovaPlugin implements BarcodeReader.BarcodeListener {
  
   private AidcManager manager;
   private BarcodeReader reader;
  
  @Override
  public boolean execute(String action, JSONArray args,
    final CallbackContext callbackContext) {

     Context context = cordova.getActivity().getApplicationContext();
        if(action.equals("scan")) {
            this.scan(context, callbackContext);
            return true;
        }
        return false;
    }

  private void scan(Context context, CallbackContext callbackContext){

        Toast toast = Toast.makeText(cordova.getActivity(), "here", Toast.LENGTH_SHORT);
        // Display toast
        toast.show();
        // Send a positive result to the callbackContext
        PluginResult pluginResult = new PluginResult(PluginResult.Status.OK);
        callbackContext.sendPluginResult(pluginResult);
      
        /*
        Intent intent = new Intent(this.cordova.getActivity().getBaseContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
     //   this.cordova.getActivity().startActivity(intent);
        this.cordova.setActivityResultCallback(this);
        intent.setPackage(this.cordova.getActivity().getApplicationContext().getPackageName());
        this.cordova.startActivityForResult(this, intent, 0);
    */
    AidcManager.create(this.cordova.getActivity().getApplicationContext() , new AidcManager.CreatedCallback() {

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
           e.printStackTrace();
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
         reader.addBarcodeListener(ToastyPlugin.this);

         
         try{
             
             reader.claim();
            }catch(ScannerUnavailableException e){
                e.printStackTrace();
            }
        try{
            
            reader.light(true);
            reader.aim(true);
            reader.decode(true);

        }catch(ScannerNotClaimedException e){
            e.printStackTrace();
        }catch(ScannerUnavailableException e){
            e.printStackTrace();
        }       
     }
 });
  }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

       @Override
     public void onBarcodeEvent(final BarcodeReadEvent event) {
         cordova.getActivity().runOnUiThread(new Runnable() {
              @Override
             public void run() {
                 String barcodeData = event.getBarcodeData();
                 String timestamp = event.getTimestamp();
                
                 Toast.makeText(cordova.getActivity(), barcodeData + " " + timestamp,
                     Toast.LENGTH_SHORT).show();
                 // update UI to reflect the data

                 Intent resultIntent = new Intent();
                // TODO Add extras or a data URI to this intent as appropriate.
                resultIntent.putExtra("barcodeData", barcodeData); 
             }
         });
     }


       @Override
     public void onFailureEvent(final BarcodeFailureEvent event) {
         runOnUiThread(new Runnable() {
              @Override
             public void run() {
                 Toast.makeText(cordova.getActivity(), "Barcode read failed",
                     Toast.LENGTH_SHORT).show();
             }
         });
     }
}

