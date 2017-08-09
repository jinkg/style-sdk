/*
 * Copyright (C) 2011 QSDN,Inc.
 * Copyright (C) 2011 Atsushi Konno
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yalin.wallpaper.shark;

/**
 * 餌だよーん
 */
public class Bait {
  private float[] pos = {0f,0f,0f};
  public Bait() {
  }
  public Bait(float x, float y, float z) {
    setX(x);
    setY(y);
    setZ(z);
  }
  public float getX() {
    return pos[0];
  }
  public float getY() {
    return pos[1];
  }
  public float getZ() {
    return pos[2];
  }
  public void setX(float x) {
    this.pos[0] = x;
  }
  public void setY(float x) {
    this.pos[1] = x;
  }
  public void setZ(float x) {
    this.pos[2] = x;
  }
}
