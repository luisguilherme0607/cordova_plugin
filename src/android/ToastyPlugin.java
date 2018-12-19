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
  
  private AidcManager manager;
  private BarcodeReader reader;
  private Object cor = this;
  
  @Override
  public boolean execute(String action, JSONArray args,
    final CallbackContext callbackContext) {

      return false;
     Context context = cordova.getActivity().getApplicationContext();
        if(action.equals("scan")) {
            this.scan(context);
            return true;
        }
        return false;
    }

  private boolean scan(Context context){
      
        Intent intent = new Intent(context, MainActivity.class);
        this.cordova.getActivity().startActivity(intent);
    }
}

