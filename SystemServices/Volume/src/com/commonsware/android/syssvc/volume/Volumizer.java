/* Copyright (c) 2008-09 -- CommonsWare, LLC

	 Licensed under the Apache License, Version 2.0 (the "License");
	 you may not use this file except in compliance with the License.
	 You may obtain a copy of the License at

		 http://www.apache.org/licenses/LICENSE-2.0

	 Unless required by applicable law or agreed to in writing, software
	 distributed under the License is distributed on an "AS IS" BASIS,
	 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	 See the License for the specific language governing permissions and
	 limitations under the License.
*/
	 
package com.commonsware.android.syssvc.volume;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;

public class Volumizer extends Activity {
	Meter alarm=null;
	Meter music=null;
	Meter ring=null;
	Meter system=null;
	Meter voice=null;
	AudioManager mgr=null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		mgr=(AudioManager)getSystemService(Context.AUDIO_SERVICE);
		
		alarm=(Meter)findViewById(R.id.alarm);
		music=(Meter)findViewById(R.id.music);
		ring=(Meter)findViewById(R.id.ring);
		system=(Meter)findViewById(R.id.system);
		voice=(Meter)findViewById(R.id.voice);
		
		alarm.setTag(AudioManager.STREAM_ALARM);
		music.setTag(AudioManager.STREAM_MUSIC);
		ring.setTag(AudioManager.STREAM_RING);
		system.setTag(AudioManager.STREAM_SYSTEM);
		voice.setTag(AudioManager.STREAM_VOICE_CALL);
		
		initMeter(alarm);
		initMeter(music);
		initMeter(ring);
		initMeter(system);
		initMeter(voice);
	}
	
	private void initMeter(Meter meter) {
		final int stream=((Integer)meter.getTag()).intValue();
		
		meter.setMax(mgr.getStreamMaxVolume(stream));
		meter.setProgress(mgr.getStreamVolume(stream));
		meter.setOnIncrListener(new View.OnClickListener() {
			public void onClick(View v) {
				mgr.adjustStreamVolume(stream,
																AudioManager.ADJUST_RAISE, 0);
			}
		});
		meter.setOnDecrListener(new View.OnClickListener() {
			public void onClick(View v) {
				mgr.adjustStreamVolume(stream,
																AudioManager.ADJUST_LOWER, 0);
			}
		});
	}
}
