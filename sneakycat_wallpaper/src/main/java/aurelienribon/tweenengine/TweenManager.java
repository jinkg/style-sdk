package aurelienribon.tweenengine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TweenManager {
    private boolean isPaused = false;
    private final ArrayList<BaseTween<?>> objects = new ArrayList(20);

    public static void setAutoRemove(BaseTween<?> object, boolean value) {
        object.isAutoRemoveEnabled = value;
    }

    public static void setAutoStart(BaseTween<?> object, boolean value) {
        object.isAutoStartEnabled = value;
    }

    public TweenManager add(BaseTween<?> object) {
        if (!this.objects.contains(object)) {
            this.objects.add(object);
        }
        if (object.isAutoStartEnabled) {
            object.start();
        }
        return this;
    }

    public boolean containsTarget(Object target) {
        int n = this.objects.size();
        for (int i = 0; i < n; i++) {
            if (((BaseTween) this.objects.get(i)).containsTarget(target)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsTarget(Object target, int tweenType) {
        int n = this.objects.size();
        for (int i = 0; i < n; i++) {
            if (((BaseTween) this.objects.get(i)).containsTarget(target, tweenType)) {
                return true;
            }
        }
        return false;
    }

    public void killAll() {
        int n = this.objects.size();
        for (int i = 0; i < n; i++) {
            ((BaseTween) this.objects.get(i)).kill();
        }
    }

    public void killTarget(Object target) {
        int n = this.objects.size();
        for (int i = 0; i < n; i++) {
            ((BaseTween) this.objects.get(i)).killTarget(target);
        }
    }

    public void killTarget(Object target, int tweenType) {
        int n = this.objects.size();
        for (int i = 0; i < n; i++) {
            ((BaseTween) this.objects.get(i)).killTarget(target, tweenType);
        }
    }

    public void ensureCapacity(int minCapacity) {
        this.objects.ensureCapacity(minCapacity);
    }

    public void pause() {
        this.isPaused = true;
    }

    public void resume() {
        this.isPaused = false;
    }

    public void update(float delta) {
        int i;
        for (i = this.objects.size() - 1; i >= 0; i--) {
            BaseTween<?> obj = (BaseTween) this.objects.get(i);
            if (obj.isFinished() && obj.isAutoRemoveEnabled) {
                this.objects.remove(i);
                obj.free();
            }
        }
        if (!this.isPaused) {
            if (delta >= 0.0f) {
                int n = this.objects.size();
                for (i = 0; i < n; i++) {
                    ((BaseTween) this.objects.get(i)).update(delta);
                }
                return;
            }
            for (i = this.objects.size() - 1; i >= 0; i--) {
                ((BaseTween) this.objects.get(i)).update(delta);
            }
        }
    }

    public int size() {
        return this.objects.size();
    }

    public int getRunningTweensCount() {
        return getTweensCount(this.objects);
    }

    public int getRunningTimelinesCount() {
        return getTimelinesCount(this.objects);
    }

    public List<BaseTween<?>> getObjects() {
        return Collections.unmodifiableList(this.objects);
    }

    private static int getTweensCount(List<BaseTween<?>> objs) {
        int cnt = 0;
        int n = objs.size();
        for (int i = 0; i < n; i++) {
            BaseTween<?> obj = (BaseTween) objs.get(i);
            if (obj instanceof Tween) {
                cnt++;
            } else {
                cnt += getTweensCount(((Timeline) obj).getChildren());
            }
        }
        return cnt;
    }

    private static int getTimelinesCount(List<BaseTween<?>> objs) {
        int cnt = 0;
        int n = objs.size();
        for (int i = 0; i < n; i++) {
            BaseTween<?> obj = (BaseTween) objs.get(i);
            if (obj instanceof Timeline) {
                cnt += getTimelinesCount(((Timeline) obj).getChildren()) + 1;
            }
        }
        return cnt;
    }
}
