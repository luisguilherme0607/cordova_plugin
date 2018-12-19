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

     Activity activity = this.cordova.getActivity();

     AidcManager.create(activity, new AidcManager.CreatedCallback() {

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
                     Toast.makeText(activity, "Failed to apply properties",
                         Toast.LENGTH_SHORT).show();
                 }

                 // register bar code event listener
                 reader.addBarcodeListener(activity);
             
          });
     }
  }
}

