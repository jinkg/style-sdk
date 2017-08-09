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
 * 水槽
 */
public class Aquarium {
  // 水槽の大きさ（案）
  public static Float min_x = new Float(-16.0f);
  public static Float max_x = new Float(16.0f);

  public static Float min_y = new Float(-4.0f);
  public static Float max_y = new Float(4.0f);

  public static Float min_z = new Float(-16.0f);
  public static Float max_z = new Float(16.0f);

  // 水槽の中心
  public static float[] center = { 0.0f, 0.0f, 0.0f };
}
