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

import android.content.Intent;
import android.content.Context;

public class ToastyPlugin extends CordovaPlugin {
  
  
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
      
        Intent intent = new Intent(this.cordova.getActivity().getBaseContext(), MainActivity.class);
     //   this.cordova.getActivity().startActivity(intent);
        this.cordova.setActivityResultCallback(this);
        intent.setPackage(this.cordova.getActivity().getApplicationContext().getPackageName());
        this.cordova.startActivityForResult(this, intent, 0);
    
  }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
       super.onActivityResult(requestCode, resultCode, data);

            String barcode = data.getStringExtra("barcodeData");
            String timestamp = data.getStringExtra("timestamp");
    }

}

