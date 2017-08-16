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

/**
 * An alternative to LibGDX ApplicationAdapter that makes it easier to create interactive live wallpapers. This should
 * be wrapped in an ApplicationAdapter that passes down the extra functionality.
 */
public interface LiveWallpaperBaseRenderer {
	void create();
	void dispose();
	void pause();
	void resume();
	void resize(int width, int height);
	void draw(float deltaTime, float xOffsetFake, float xOffsetLooping, float xOffsetSmooth, float yOffset);
	void onSettingsChanged();
	void onDoubleTap();
	void onTripleTap();
	void setIsPreview(boolean isPreview);
}
