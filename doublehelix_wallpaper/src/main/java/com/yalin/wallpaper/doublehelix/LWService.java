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

import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.yalin.style.engine.GDXWallpaperServiceProxy;
import com.yalin.wallpaper.doublehelix.core.MainRenderer;
import com.yalin.wallpaper.doublehelix.lwp.LiveWallpaperAndroidAdapter;

public final class LWService extends GDXWallpaperServiceProxy {
    private static final int FPS_MAX = 39; //Allowing high FPS can make the launcher laggy.

    MainRenderer.SettingsAdapter mSettingsPrefsAdapter;
    LiveWallpaperAndroidAdapter applicationListener;

    public LWService(Context host) {
        super(host);
    }

    @Override
    public void onCreateApplication() {
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.disableAudio = true;
        config.numSamples = 2;
        config.useAccelerometer = false;
        config.useCompass = false;
        config.r = 8;
        config.g = 8;
        config.b = 8;
        config.a = 8;

        mSettingsPrefsAdapter = new SettingsPrefsAdapter(getApplicationContext());

        applicationListener = new LiveWallpaperAndroidAdapter(
                new MainRenderer(mSettingsPrefsAdapter),
                new TripleTapSettingsListener(getBaseContext()),
                getBaseContext(), FPS_MAX);

        initialize(applicationListener, config);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}