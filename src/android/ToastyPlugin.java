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

public class ToastyPlugin extends CordovaPlugin {
  
  private static final String DURATION_LONG = "long";
  private AidcManager manager;
  private BarcodeReader reader;
  
  @Override
  public boolean execute(String action, JSONArray args,
    final CallbackContext callbackContext) {
      
      if (action.equals("scan")) {
          return scan(args, callbackContext); 
      }

      return false;
  }

  private boolean scan(JSONArray args, CallbackContext callbackContext){

     AidcManager.create(this.cordova.getActivity(), new AidcManager.CreatedCallback() {

              @Override
             public void onCreated(AidcManager aidcManager) {
                 manager = aidcManager;
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

                 // register bar code event listener
                 reader.addBarcodeListener(this.cordova.getActivity());
             }


              @Override
              public void onBarcodeEvent(final BarcodeReadEvent event) {
                 runOnUiThread(new Runnable() {
                 @Override
                 public void run() {
                   String barcodeData = event.getBarcodeData();
                   String timestamp = event.getTimestamp();

                 // update UI to reflect the data
                  }
              });
     }

         });

      String message;
      
      try {

        JSONObject options = args.getJSONObject(0);
        message = options.getString("message");
      } catch (JSONException e) {
        callbackContext.error("Error encountered: " + e.getMessage());
        return false;
      }
     
    Toast toast = Toast.makeText(cordova.getActivity(), message, 2);
      toast.show();

    PluginResult pluginResult = new PluginResult(PluginResult.Status.OK);
    callbackContext.sendPluginResult(pluginResult);

    return true;
  }
}

