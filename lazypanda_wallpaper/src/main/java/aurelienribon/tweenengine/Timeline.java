package aurelienribon.tweenengine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import aurelienribon.tweenengine.Pool.Callback;

public final class Timeline extends BaseTween<Timeline> {
    static final /* synthetic */ boolean $assertionsDisabled = (!Timeline.class.desiredAssertionStatus());
    private static final Callback<Timeline> poolCallback = new C03781();
    static final Pool<Timeline> pool = new Pool<Timeline>(10, poolCallback) {
        protected Timeline create() {
            return new Timeline();
        }
    };

    private final List<BaseTween<?>> children;
    private Timeline current;
    private boolean isBuilt;
    private Modes mode;
    private Timeline parent;

    private enum Modes {
        SEQUENCE,
        PARALLEL
    }

    static class C03781 implements Callback<Timeline> {
        C03781() {
        }

        public void onPool(Timeline obj) {
            obj.reset();
        }

        public void onUnPool(Timeline obj) {
            obj.reset();
        }
    }

    public static int getPoolSize() {
        return pool.size();
    }

    public static void ensurePoolCapacity(int minCapacity) {
        pool.ensureCapacity(minCapacity);
    }

    public static Timeline createSequence() {
        Timeline tl = (Timeline) pool.get();
        tl.setup(Modes.SEQUENCE);
        return tl;
    }

    public static Timeline createParallel() {
        Timeline tl = (Timeline) pool.get();
        tl.setup(Modes.PARALLEL);
        return tl;
    }

    private Timeline() {
        this.children = new ArrayList(10);
        reset();
    }

    protected void reset() {
        super.reset();
        this.children.clear();
        this.parent = null;
        this.current = null;
        this.isBuilt = false;
    }

    private void setup(Modes mode) {
        this.mode = mode;
        this.current = this;
    }

    public Timeline push(Tween tween) {
        if (this.isBuilt) {
            throw new RuntimeException("You can't push anything to a timeline once it is started");
        }
        this.current.children.add(tween);
        return this;
    }

    public Timeline push(Timeline timeline) {
        if (this.isBuilt) {
            throw new RuntimeException("You can't push anything to a timeline once it is started");
        } else if (timeline.current != timeline) {
            throw new RuntimeException("You forgot to call a few 'end()' statements in your pushed timeline");
        } else {
            timeline.parent = this.current;
            this.current.children.add(timeline);
            return this;
        }
    }

    public Timeline pushPause(float time) {
        if (this.isBuilt) {
            throw new RuntimeException("You can't push anything to a timeline once it is started");
        }
        this.current.children.add(Tween.mark().delay(time));
        return this;
    }

    public Timeline beginSequence() {
        if (this.isBuilt) {
            throw new RuntimeException("You can't push anything to a timeline once it is started");
        }
        Timeline tl = (Timeline) pool.get();
        tl.parent = this.current;
        tl.mode = Modes.SEQUENCE;
        this.current.children.add(tl);
        this.current = tl;
        return this;
    }

    public Timeline beginParallel() {
        if (this.isBuilt) {
            throw new RuntimeException("You can't push anything to a timeline once it is started");
        }
        Timeline tl = (Timeline) pool.get();
        tl.parent = this.current;
        tl.mode = Modes.PARALLEL;
        this.current.children.add(tl);
        this.current = tl;
        return this;
    }

    public Timeline end() {
        if (this.isBuilt) {
            throw new RuntimeException("You can't push anything to a timeline once it is started");
        } else if (this.current == this) {
            throw new RuntimeException("Nothing to end...");
        } else {
            this.current = this.current.parent;
            return this;
        }
    }

    public List<BaseTween<?>> getChildren() {
        if (this.isBuilt) {
            return Collections.unmodifiableList(this.current.children);
        }
        return this.current.children;
    }

    public Timeline build() {
        if (!this.isBuilt) {
            this.duration = 0.0f;
            for (int i = 0; i < this.children.size(); i++) {
                BaseTween<?> obj = (BaseTween) this.children.get(i);
                if (obj.getRepeatCount() < 0) {
                    throw new RuntimeException("You can't push an object with infinite repetitions in a timeline");
                }
                obj.build();
                switch (this.mode) {
                    case SEQUENCE:
                        float tDelay = this.duration;
                        this.duration += obj.getFullDuration();
                        obj.delay += tDelay;
                        break;
                    case PARALLEL:
                        this.duration = Math.max(this.duration, obj.getFullDuration());
                        break;
                    default:
                        break;
                }
            }
            this.isBuilt = true;
        }
        return this;
    }

    public Timeline start() {
        super.start();
        for (int i = 0; i < this.children.size(); i++) {
            ((BaseTween) this.children.get(i)).start();
        }
        return this;
    }

    public void free() {
        for (int i = this.children.size() - 1; i >= 0; i--) {
            ((BaseTween) this.children.remove(i)).free();
        }
        pool.free(this);
    }

    protected void updateOverride(int step, int lastStep, boolean isIterationStep, float delta) {
        int n;
        int i;
        float dt;
        if (isIterationStep || step <= lastStep) {
            if (isIterationStep || step >= lastStep) {
                if (!$assertionsDisabled && !isIterationStep) {
                    throw new AssertionError();
                } else if (step > lastStep) {
                    if (isReverse(step)) {
                        forceEndValues();
                        n = this.children.size();
                        for (i = 0; i < n; i++) {
                            ((BaseTween) this.children.get(i)).update(delta);
                        }
                        return;
                    }
                    forceStartValues();
                    n = this.children.size();
                    for (i = 0; i < n; i++) {
                        ((BaseTween) this.children.get(i)).update(delta);
                    }
                } else if (step >= lastStep) {
                    if (isReverse(step)) {
                        dt = -delta;
                    } else {
                        dt = delta;
                    }
                    if (delta >= 0.0f) {
                        n = this.children.size();
                        for (i = 0; i < n; i++) {
                            ((BaseTween) this.children.get(i)).update(dt);
                        }
                        return;
                    }
                    for (i = this.children.size() - 1; i >= 0; i--) {
                        ((BaseTween) this.children.get(i)).update(dt);
                    }
                } else if (isReverse(step)) {
                    forceStartValues();
                    for (i = this.children.size() - 1; i >= 0; i--) {
                        ((BaseTween) this.children.get(i)).update(delta);
                    }
                } else {
                    forceEndValues();
                    for (i = this.children.size() - 1; i >= 0; i--) {
                        ((BaseTween) this.children.get(i)).update(delta);
                    }
                }
            } else if ($assertionsDisabled || delta <= 0.0f) {
                dt = isReverse(lastStep) ? (-delta) - 1.0f : delta + 1.0f;
                for (i = this.children.size() - 1; i >= 0; i--) {
                    ((BaseTween) this.children.get(i)).update(dt);
                }
            } else {
                throw new AssertionError();
            }
        } else if ($assertionsDisabled || delta >= 0.0f) {
            dt = isReverse(lastStep) ? (-delta) - 1.0f : delta + 1.0f;
            n = this.children.size();
            for (i = 0; i < n; i++) {
                ((BaseTween) this.children.get(i)).update(dt);
            }
        } else {
            throw new AssertionError();
        }
    }

    protected void forceStartValues() {
        for (int i = this.children.size() - 1; i >= 0; i--) {
            ((BaseTween) this.children.get(i)).forceToStart();
        }
    }

    protected void forceEndValues() {
        int n = this.children.size();
        for (int i = 0; i < n; i++) {
            ((BaseTween) this.children.get(i)).forceToEnd(this.duration);
        }
    }

    protected boolean containsTarget(Object target) {
        int n = this.children.size();
        for (int i = 0; i < n; i++) {
            if (((BaseTween) this.children.get(i)).containsTarget(target)) {
                return true;
            }
        }
        return false;
    }

    protected boolean containsTarget(Object target, int tweenType) {
        int n = this.children.size();
        for (int i = 0; i < n; i++) {
            if (((BaseTween) this.children.get(i)).containsTarget(target, tweenType)) {
                return true;
            }
        }
        return false;
    }
}
