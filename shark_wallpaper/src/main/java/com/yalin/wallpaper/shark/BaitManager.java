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


import android.util.Log;

import java.util.Stack;

/**
 * 餌
 */
public class BaitManager {
  private static final String TAG = BaitManager.class.getName();
  private static final boolean _debug = false;
  private int max_count = 100;
  private int nowCount = 0;
  private Stack<Bait> stack = new Stack<Bait>();
  public void addBait(float x, float y, float z) {
    synchronized (this) {
      if (nowCount < max_count) {
        if (_debug) Log.d(TAG,"("+x+","+y+","+z+")に餌を追加します");
        stack.push(new Bait(x,y,z));
        nowCount++;
      }
    }
  }

  public Bait getBait() {
    synchronized (this) {
      if (nowCount!=0) {
        return stack.get(0);
      }
      return null;
    }
  }

  public void eat(Bait bait) {
    synchronized (this) {
      // 要素が無かった場合は無視
      if (stack.remove(bait)) {
        // 消せた
        nowCount--;
        if (_debug) Log.d(TAG,"("+bait.getX()+","+bait.getY()+","+bait.getZ()+")の餌を食べました");
      }
    }
  }
}
