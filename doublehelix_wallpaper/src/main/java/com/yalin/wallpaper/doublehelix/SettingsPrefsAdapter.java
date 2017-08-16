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

package com.yalin.wallpaper.doublehelix;

import android.content.Context;

import com.badlogic.gdx.graphics.Color;
import com.yalin.wallpaper.doublehelix.core.MainRenderer;
import com.yalin.wallpaper.doublehelix.core.Settings;
import com.yalin.wallpaper.doublehelix.lwp.PowerUtil;
import com.yalin.wallpaper.doublehelix.lwp.AdvancedColor;

import java.util.HashMap;
import java.util.Map;

public class SettingsPrefsAdapter implements MainRenderer.SettingsAdapter {

    public static boolean tripleTapSettings = true;

    Context context;
    PowerUtil mPowerUtil;

    Map<AdvancedColor, Color> mColors = new HashMap<>();

    public SettingsPrefsAdapter(Context context) {
        this.context = context;
        this.mPowerUtil = new PowerUtil(context, 10, 0.5f);
    }

    @Override
    public void updateAllSettings() {
        tripleTapSettings = false;

        prepareColor();
        updateColor(false);

        Settings.speed = (float) 3 / 10f + 0.05f;
        Settings.dof = true;
        Settings.bloom = true;
        Settings.filmGrain = false;
        Settings.scanLines = false;
        Settings.vignette = false;
        Settings.numParticles = 3 * 100;
        Settings.pointParticles = false;
        Settings.pseudoScrolling = false;
    }

    @Override
    public void updateInLoop(float deltaTime) {
        mPowerUtil.update(deltaTime);
        updateColor(true);
    }

    private void prepareColor() {
        mColors.clear();
        AdvancedColor color =
                AdvancedColor.fromString(AdvancedColor.constantColorToAdvancedColorString(0xff0e4c89));
        mColors.put(color, Settings.frontHelixColor);
    }

    private void updateColor(boolean powerBasedColorsOnly) {
        AdvancedColor.updateLibgdxColorsFromMap(
                mColors, mPowerUtil.getBatteryLevel(), mPowerUtil.getChargingState(), powerBasedColorsOnly);

        Settings.updateColorsFromFrontHelixColor();

    }

}
