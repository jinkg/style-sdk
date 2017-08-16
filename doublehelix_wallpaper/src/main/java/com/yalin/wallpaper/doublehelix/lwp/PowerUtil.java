/*******************************************************************************
 * Copyright 2015 Cypher Cove, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.yalin.wallpaper.doublehelix.lwp;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

public class PowerUtil {
	
	float mTimeToNextBatteryCheck = 0;
	float mTimeToNextChargingCheck = 0;
	float mTimeBetweenBatteryChecks = 1;
	float mTimeBetweenChargingChecks = 1;
	float mLatestBatteryLevel = 1f;
	boolean mLatestChargingState = true;
	
	Context mContext;
	
	public PowerUtil(Context context, float timeBetweenBatteryChecks, float timeBetweenChargingChecks){
		mContext = context;
		mTimeBetweenBatteryChecks = timeBetweenBatteryChecks;
		mTimeBetweenChargingChecks = timeBetweenChargingChecks;
	}
	
	public void update(float deltaTime){
		mTimeToNextBatteryCheck -= deltaTime;
		mTimeToNextChargingCheck -= deltaTime;
	}
	

	public float getBatteryLevel(){
		if (mTimeToNextBatteryCheck <= 0){
			mTimeToNextBatteryCheck = mTimeBetweenBatteryChecks;
			Intent intent = mContext.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
			mLatestBatteryLevel = 
					(float)intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 50)/
					(float)intent.getIntExtra(BatteryManager.EXTRA_SCALE, 100);
			
		}
		
		return mLatestBatteryLevel;
	}
	
	public boolean getChargingState(){
		if (mTimeToNextChargingCheck <= 0){
			mTimeToNextChargingCheck = mTimeBetweenChargingChecks;
			Intent intent = mContext.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
	        int pluggedExtra = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0);
	        mLatestChargingState = !(pluggedExtra == 0);
		}
        
        return mLatestChargingState;
	}
	
}
