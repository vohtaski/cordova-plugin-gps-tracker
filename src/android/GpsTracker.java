package com.android.plugins;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.PluginResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.content.Intent;
import android.widget.Toast;
import android.os.Bundle;


/**
 * This class echoes a string called from JavaScript.
 */
public class GpsTracker extends CordovaPlugin implements LocationListener {
  private static final String TAG = "GpsTrackerPlugin";

  private long interval = 2000;
  private float distanceFilter = 5.0f;
  private boolean isDebugging = false;

  private CallbackContext callbackContext;  // Keeps track of the JS callback context.

  private LocationManager locationManager;

  /**
   * Sets the context of the Command. This can then be used to do things like
   * get file paths associated with the Activity.
   *
   * @param cordova The context of the main Activity.
   * @param webView The associated CordovaWebView.
   */
  @Override
  public void initialize(CordovaInterface cordova, CordovaWebView webView) {
    super.initialize(cordova, webView);
    this.locationManager = (LocationManager) cordova.getActivity().getSystemService(Context.LOCATION_SERVICE);
  }

  @Override
  public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
    Boolean result = false;

    if (action.equals("configure")) {
      result = true;
      this.callbackContext = callbackContext;
      try {
        // [interval, distanceFilter, debug]
        this.interval = Long.parseLong(args.getString(0));
        this.distanceFilter = Float.parseFloat(args.getString(1));
        this.isDebugging = Boolean.parseBoolean(args.getString(2));
      } catch (JSONException e) {
        Log.d(TAG, "Json Exception" + e);
        callbackContext.error("JSON Exception" + e.getMessage());
      }
    } else if (action.equals("start")) {
      result = true;
      this.start(callbackContext);
    } else if (action.equals("stop")) {
      result = true;
      this.stop(callbackContext);
    }
    return result;
  }

  private void start(CallbackContext callbackContext) {
    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
        this.interval, this.distanceFilter, this);
  }

  private void stop(CallbackContext callbackContext) {
    locationManager.removeUpdates(GpsTracker.this);
  }

  @Override
  public void onLocationChanged(Location location) {

    // String msg = "New Latitude: " + location.getLatitude()
    //     + "New Longitude: " + location.getLongitude();
    //
    // Toast.makeText(cordova.getActivity().getBaseContext(), msg, Toast.LENGTH_LONG).show();
    //
    PluginResult result = new PluginResult(PluginResult.Status.OK, this.locationToJSON(location));
    result.setKeepCallback(true);
    callbackContext.sendPluginResult(result);
  }

  @Override
  public void onProviderDisabled(String provider) {

    // Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
    // startActivity(intent);
    // Toast.makeText(getBaseContext(), "Gps is turned off!! ",
    //     Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onProviderEnabled(String provider) {

    // Toast.makeText(getBaseContext(), "Gps is turned on!! ",
    //     Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onStatusChanged(String provider, int status, Bundle extras) {
    // TODO Auto-generated method stub

  }

  private JSONObject locationToJSON(Location location) {
    JSONObject data = new JSONObject();
    try {
      data.put("latitude", location.getLatitude());
      data.put("longitude", location.getLongitude());
      data.put("accuracy", location.getAccuracy());
      data.put("altitude", location.getAltitude());
      data.put("bearing", location.getBearing());
      data.put("speed", location.getSpeed());
      data.put("timestamp", location.getTime());
    } catch(JSONException e) {
      Log.d(TAG, "ERROR CREATING JSON" + e);
    }

    return data;
  }
}
