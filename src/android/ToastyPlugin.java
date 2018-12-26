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

import android.content.Context;

public class ToastyPlugin extends CordovaPlugin implements BarcodeReader.BarcodeListener {
  
   private AidcManager manager;
   private BarcodeReader reader;
   private CallbackContext callbackContext;
   private boolean light;
  
  @Override
  public boolean execute(String action, JSONArray args,
    final CallbackContext callbackContext) {

     Context context = cordova.getActivity().getApplicationContext();
        if(action.equals("scan")) {
            this.scan(context, callbackContext, args);
            return true;
        } 
        if(action.equals("stopScan")){
            this.stopScan(context , callbackContext);
            return true;
        }
        return false;
    }

  private void scan(Context context, CallbackContext callbackContext, JSONArray args){

        this.callbackContext = callbackContext;

        try {
          JSONObject options = args.getJSONObject(0);
          this.light = options.getBoolean("light");

        } catch (JSONException e) {
            callbackContext.error("Error encountered: " + e.getMessage());
        }
      
        AidcManager.create(this.cordova.getActivity().getApplicationContext() , new AidcManager.CreatedCallback() {

           @Override
           public void onCreated(AidcManager aidcManager) {
           manager = aidcManager;
           reader = manager.createBarcodeReader();

           try {
               reader.setProperty(BarcodeReader.PROPERTY_CODE_39_ENABLED, false);
               reader.setProperty(BarcodeReader.PROPERTY_DATAMATRIX_ENABLED, true);
               reader.setProperty(BarcodeReader.PROPERTY_TRIGGER_CONTROL_MODE, BarcodeReader.TRIGGER_CONTROL_MODE_AUTO_CONTROL);

           } catch (UnsupportedPropertyException e) {
               e.printStackTrace();
           }

          Map<String, Object> properties = new HashMap<String, Object>();

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
          properties.put(BarcodeReader.PROPERTY_CODE_39_MAXIMUM_LENGTH, 10);
          properties.put(BarcodeReader.PROPERTY_CENTER_DECODE, true);
          properties.put(BarcodeReader.PROPERTY_NOTIFICATION_BAD_READ_ENABLED, true);
          reader.setProperties(properties);

          reader.addBarcodeListener(ToastyPlugin.this);

         
          try{
             
             reader.claim();
          }catch(ScannerUnavailableException e){

             Toast.makeText(cordova.getActivity(), "Scanner unnavailable", Toast.LENGTH_SHORT).show();
             e.printStackTrace();
            }
        try{
            
            reader.light(ToastyPlugin.this.light);
            reader.aim(true);
            reader.decode(true);

        }catch(ScannerNotClaimedException e){
            Toast.makeText(cordova.getActivity(), "Scanner not claimed", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }catch(ScannerUnavailableException e){
            Toast.makeText(cordova.getActivity(), "Scanner unnavailable", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }       
     }
  });
  }

       @Override
       public void onBarcodeEvent(final BarcodeReadEvent event) {
         cordova.getActivity().runOnUiThread(new Runnable() {
              @Override
              public void run() {
                 String barcodeData = event.getBarcodeData();
                 String timestamp = event.getTimestamp();

                 try {

                     JSONObject data = new JSONObject();
                     data.put("barcode", barcodeData);
                     data.put("timestamp", timestamp);

                     PluginResult result = new PluginResult(PluginResult.Status.OK, data);
                     result.setKeepCallback(true);
                     callbackContext.sendPluginResult(result);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
             }
         });
     }

       @Override
       public void onFailureEvent(final BarcodeFailureEvent event) {
         cordova.getActivity().runOnUiThread(new Runnable() {
               @Override
               public void run() {
                 Toast.makeText(cordova.getActivity(), "Barcode read failed", Toast.LENGTH_SHORT).show();
              }
          });
     }


     private void stopScan(Context context, CallbackContext callbackContext){

          try{
            
            reader.light(false);
            reader.aim(false);
            reader.decode(false);

        }catch(ScannerNotClaimedException e){
            Toast.makeText(cordova.getActivity(), "Scanner not claimed exception", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }catch(ScannerUnavailableException e){
            Toast.makeText(cordova.getActivity(), "Scanner unnavailable", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }       
         if (reader != null) {
             reader.removeBarcodeListener(this);
             reader.close();
         }
         if (manager != null) {
             manager.close();
         }

         PluginResult pluginResult = new PluginResult(PluginResult.Status.OK);
         callbackContext.sendPluginResult(pluginResult);
     }
}

