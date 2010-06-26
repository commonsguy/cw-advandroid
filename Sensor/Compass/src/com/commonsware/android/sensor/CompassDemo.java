/***
	Copyright (c) 2008-2010 CommonsWare, LLC
	
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

package com.commonsware.android.sensor;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class CompassDemo extends Activity {
	private SensorManager mgr=null;
	private TextView degrees=null;
	private float[] lastMagFields=null;
	private float[] lastAccels=null;
	private float[] rotationMatrix=new float[9];
	private float[] orientation=new float[3];

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		degrees=(TextView)findViewById(R.id.degrees);

		mgr=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
		mgr.registerListener(magListener,
													mgr.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
													SensorManager.SENSOR_DELAY_UI);
		mgr.registerListener(accListener,
													mgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
													SensorManager.SENSOR_DELAY_UI);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		mgr.unregisterListener(accListener);
		mgr.unregisterListener(magListener);
	}
	
	private void computeOrientation() {
		if (SensorManager.getRotationMatrix(rotationMatrix, null,
																				lastMagFields, lastAccels)) {
			SensorManager.getOrientation(rotationMatrix, orientation);
			
			float north=orientation[0]*57.2957795f;
			
			if (north<0) {
				north=360.0f+north;
			}
			
			degrees.setText(String.valueOf(north));
		}
	}
	
	private SensorEventListener magListener=new SensorEventListener() {
		public void onSensorChanged(SensorEvent e) {
			if (lastMagFields==null) {
				lastMagFields=new float[3];
			}
			
			System.arraycopy(e.values, 0, lastMagFields, 0, 3);
			
			if (lastAccels!=null) {
				computeOrientation();
			}
		}
		
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// unused
		}
	};
	
	private SensorEventListener accListener=new SensorEventListener() {
		public void onSensorChanged(SensorEvent e) {
			if (lastAccels==null) {
				lastAccels=new float[3];
			}
			
			System.arraycopy(e.values, 0, lastAccels, 0, 3);
			
			if (lastMagFields!=null) {
				computeOrientation();
			}
		}
		
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// unused
		}
	};
}
