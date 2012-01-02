/***
  Copyright (c) 2008-2012 CommonsWare, LLC
  
  Licensed under the Apache License, Version 2.0 (the "License"); you may
  not use this file except in compliance with the License. You may obtain
  a copy of the License at
    http://www.apache.org/licenses/LICENSE-2.0
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
*/

package com.commonsware.android.weather;

import java.util.ArrayList;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

public class WeatherFragment extends Fragment
  implements LocationListener, WeatherListener, ServiceConnection {
  private WebView browser;
  private LocationManager mgr;
  private WeatherBinder weather;
  private String forecast;
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    setRetainInstance(true);
    
    getActivity()
      .getApplicationContext()
      .bindService(new Intent(getActivity(), WeatherService.class),
                   this, Context.BIND_AUTO_CREATE);

    mgr=(LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
    mgr.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                                3600000, 1000, this);
  }
  
  @Override
  public View onCreateView(LayoutInflater inflater,
                           ViewGroup parent,
                           Bundle savedInstanceState) {
    View result=inflater.inflate(R.layout.fragment, parent, false);
    
    browser=(WebView)result.findViewById(R.id.webkit);
    
    if (forecast!=null) {
      showForecast();
    }
    
    return(result);
  }
  
  @Override
  public void onDestroy() {
    if (mgr!=null) {
      mgr.removeUpdates(this);
    }
    
    getActivity().getApplicationContext().unbindService(this);
    super.onDestroy();
  }
  
  private void goBlooey(Throwable t) {
    AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
    
    builder
      .setTitle("Exception!")
      .setMessage(t.toString())
      .setPositiveButton("OK", null)
      .show();
  }
  
  static String generatePage(ArrayList<Forecast> forecasts) {
    StringBuilder bufResult=new StringBuilder("<html><body><table>");
    
    bufResult.append("<tr><th width=\"50%\">Time</th>"+
                      "<th>Temperature</th><th>Forecast</th></tr>");
    
    for (Forecast forecast : forecasts) {
      bufResult.append("<tr><td align=\"center\">");
      bufResult.append(forecast.getTime());
      bufResult.append("</td><td align=\"center\">");
      bufResult.append(forecast.getTemp());
      bufResult.append("</td><td><img src=\"");
      bufResult.append(forecast.getIcon());
      bufResult.append("\"></td></tr>");
    }
    
    bufResult.append("</table></body></html>");
    
    return(bufResult.toString());
  }
  
  void showForecast() {
    browser.loadDataWithBaseURL(null, forecast, "text/html",
                                "UTF-8", null);
  }
  
  public void onLocationChanged(Location location) {
    if (weather!=null) {
      weather.getForecast(location, this);
    }
    else {
      Log.w(getClass().getName(), "Unable to fetch forecast -- no WeatherBinder");
    }
  }
  
  public void onProviderDisabled(String provider) {
    // required for interface, not used
  }
  
  public void onProviderEnabled(String provider) {
    // required for interface, not used
  }
  
  public void onStatusChanged(String provider, int status,
                                Bundle extras) {
    // required for interface, not used
  }
  
  public void updateForecast(ArrayList<Forecast> triples) {
    forecast=generatePage(triples);
    showForecast();
  }
  
  public void handleError(Exception e) {
    goBlooey(e);
  }
    
  public void onServiceConnected(ComponentName className,
                                 IBinder rawBinder) {
    weather=(WeatherBinder)rawBinder;
  }

  public void onServiceDisconnected(ComponentName className) {
    weather=null;
  }
}

