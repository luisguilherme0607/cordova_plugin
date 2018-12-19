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

     AidcManager.create(new Inner(), new AidcManager.CreatedCallback() {

              @Override
             public void onCreated(AidcManager aidcManager) {
                 manager = aidcManager;
                 reader = manager.createBarcodeReader();
             }
         }

      String message;
      com.honeywell.aidc.BarcodeReader reader;
      
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

class Inner extends Context {

}
