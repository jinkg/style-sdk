package com.yalin.wallpaper.water02;

import com.badlogic1.gdx.Gdx;
import com.badlogic1.gdx.graphics.Camera;
import com.badlogic1.gdx.graphics.Mesh;
import com.badlogic1.gdx.graphics.OrthographicCamera;
import com.badlogic1.gdx.graphics.Texture;
import com.badlogic1.gdx.graphics.VertexAttribute;
import com.badlogic1.gdx.graphics.glutils.ShaderProgram;
import com.badlogic1.gdx.math.Intersector;
import com.badlogic1.gdx.math.Plane;
import com.badlogic1.gdx.math.Vector2;
import com.badlogic1.gdx.math.Vector3;

import java.lang.reflect.Array;
import java.nio.FloatBuffer;
import java.util.Timer;
import java.util.TimerTask;

public class WaterRipples {
    public static String[] HbgIndexNames = new String[]{"hbg3"};
    public static String[] VbgIndexNames = new String[]{"bg3"};
    static final /* synthetic */ boolean $assertionsDisabled = (!WaterRipples.class.desiredAssertionStatus());
    short CellSuggestedDimension = (short) 24;
    float accum;
    Camera camera;
    boolean computingTouchArray = false;
    float[][] curr;
    Vector3 gridDims;
    short height;
    int index;
    int index1;
    boolean isAutomatic;
    float[][] last;
    Mesh mesh;
    Plane plane;
    Vector2 point2 = new Vector2();
    Vector3 point3 = new Vector3();
    float posx;
    float posy;
    int sh;
    int sw;
    int sw1;
    TimerTask task;
    Texture texture;
    Timer timer;
    boolean updateDirectBufferAccess = true;
    float[] vertices;
    short width;
    float zDepthCoord = 0.0f;

    public WaterRipples() {
    }

    public void render(boolean directBufferAccess) {
        if (!(this.camera == null || Gdx.graphics.isGL20Available())) {
            this.camera.apply(Gdx.gl10);
        }
        Gdx.gl.glDisable(3042);
        Gdx.gl.glBlendFunc(770, 771);
        Gdx.gl.glLineWidth(1.0f);
        Gdx.gl10.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.accum += Gdx.graphics.getDeltaTime();
        while (this.accum > 0.024f) {
            while (this.computingTouchArray) {
                this.updateDirectBufferAccess = directBufferAccess;
            }
            short y = (short) 0;
            while (y <= this.height) {
                short x = (short) 0;
                while (x <= this.width) {
                    if (x > (short) 0 && x < this.width && y > (short) 0 && y < this.height) {
                        this.curr[x][y] = ((((this.last[x - 1][y] + this.last[x + 1][y]) + this.last[x][y + 1]) + this.last[x][y - 1]) / 2.0f) - this.curr[x][y];
                    }
                    float[] fArr = this.curr[x];
                    fArr[y] = fArr[y] * 0.9f;
                    x++;
                }
                y++;
            }
            float[][] tmp = this.curr;
            this.curr = this.last;
            this.last = tmp;
            this.accum -= 0.024f;
        }
        updateVertices(this.curr, this.accum / 0.024f);
        Gdx.gl.glActiveTexture(33984);
        Gdx.gl.glEnable(3553);
        this.texture.bind(0);
        this.mesh.render(4);
    }

    public void RandomWaterRipple(final boolean swich) {
        if (swich) {
            if (this.timer == null) {
                this.timer = new Timer();
                this.task = new TimerTask() {
                    public void run() {
                        if (swich) {
                            WaterRipples.this.touchScreen((int) (Math.random() * ((double) WaterRipples.this.sw)), (int) (Math.random() * ((double) WaterRipples.this.sh)));
                        }
                    }
                };
                this.timer.schedule(this.task, 3000, 3000);
            }
        } else if (this.timer != null) {
            this.timer.cancel();
            this.task.cancel();
            this.timer = null;
            this.task = null;
        }
    }

    void touchWater(Vector2 point) {
        this.computingTouchArray = true;
        float px = point.x - this.posx;
        float py = point.y - this.posy;
        for (int y = Math.max(0, ((int) py) - 2); y < Math.min(this.height, ((int) py) + 2); y++) {
            for (int x = Math.max(0, ((int) px) - 2); x < Math.min(this.width, ((int) px) + 2); x++) {
                float a = ((float) x) - px;
                float b = ((float) y) - py;
                float val = this.curr[x][y] + (8.0f * Math.max(0.0f, (float) Math.cos((1.5707963267948966d * Math.sqrt((double) ((a * a) + (b * b)))) / 2.0d)));
                if (val < 8.0f) {
                    val = 8.0f;
                } else if (val > -8.0f) {
                    val = -8.0f;
                }
                this.curr[x][y] = val;
            }
        }
        this.computingTouchArray = false;
    }

    public void touchScreen(int x, int y) {
        Intersector.intersectRayPlane(this.camera.getPickRay((float) x, (float) y),
                this.plane, point3);
        touchWater(this.point2.set(this.point3.x, this.point3.y));
    }

    void reset(int w, int h) {
        this.sw = w;
        this.sh = h;
        this.index = 0;
        restTexture();
        if (this.sw != this.sw1) {
            resetCamera();
        }
        this.sw1 = this.sw;
        this.index1 = this.index;
    }

    void restTexture() {
        this.isAutomatic = false;

        if (this.sw != this.sw1 || this.index != this.index1) {
            if (this.texture != null) {
                this.texture.dispose();
                this.texture = null;
            }
            if (this.sw < this.sh) {
                this.texture = new Texture(Gdx.files.internal("bg/" + VbgIndexNames[this.index] + ".jpg"));
            } else {
                this.texture = new Texture(Gdx.files.internal("bg/" + HbgIndexNames[this.index] + ".jpg"));
            }
        }
    }

    public void resetCamera() {
        float scale;
        this.camera = null;
        if (this.sw < this.sh) {
            scale = ((float) this.sw) / 1080.0f;
        } else {
            scale = ((float) this.sh) / 1080.0f;
        }
        System.out.println(this.sw);
        short gridWidth = (short) ((int) (((float) this.sw) / (((float) this.CellSuggestedDimension) * scale)));
        short gridHeight = (short) ((int) (((float) this.sh) / (((float) this.CellSuggestedDimension) * scale)));
        this.camera = new OrthographicCamera((float) gridWidth, (float) gridHeight);
        ((OrthographicCamera) this.camera).zoom = 1.0f;
        this.camera.position.set(((float) gridWidth) / 2.0f, ((float) gridHeight) / 2.0f, 0.0f);
        float gridZ = this.camera.position.z - (this.camera.near + ((this.camera.far - this.camera.near) / 5.0f));
        this.camera.update();
        this.gridDims = new Vector3((float) gridWidth, (float) gridHeight, gridZ);
        updateZ(this.gridDims.z);
        this.posx = 0.0f;
        this.posy = 0.0f;
        this.width = (short) ((int) this.gridDims.x);
        this.height = (short) ((int) this.gridDims.y);
        restMesh();
    }

    void restMesh() {
        short y;
        short x;
        this.last = null;
        this.curr = null;
        this.mesh = null;
        this.vertices = null;
        this.last = (float[][]) Array.newInstance(Float.TYPE, this.width + 1, this.height + 1);
        this.curr = (float[][]) Array.newInstance(Float.TYPE, this.width + 1, this.height + 1);
        int nIndices = (this.width * this.height) * 6;
        int nVertices = (this.width + 1) * (this.height + 1);
        this.mesh = new Mesh(true, nVertices, nIndices,
                new VertexAttribute(0, 3, ShaderProgram.POSITION_ATTRIBUTE),
                new VertexAttribute(3, 2, "a_texCoord0"));
        short[] indices = new short[nIndices];
        int idx = 0;
        for (y = (short) 0; y < this.height; y++) {
            short vidx = (short) ((this.width + 1) * y);
            for (x = (short) 0; x < this.width; x++) {
                int i = idx + 1;
                indices[idx] = vidx;
                idx = i + 1;
                indices[i] = (short) (vidx + 1);
                i = idx + 1;
                indices[idx] = (short) ((this.width + vidx) + 1);
                idx = i + 1;
                indices[i] = (short) (vidx + 1);
                i = idx + 1;
                indices[idx] = (short) ((this.width + vidx) + 2);
                idx = i + 1;
                indices[i] = (short) ((this.width + vidx) + 1);
                vidx = (short) (vidx + 1);
            }
        }
        this.mesh.setIndices(indices);
        this.vertices = new float[(nVertices * 5)];
        idx = 0;
        for (y = (short) 0; y <= this.height; y++) {
            x = (short) 0;
            while (x <= this.width) {
                int i = idx + 1;
                this.vertices[idx] = 0.0f;
                idx = i + 1;
                this.vertices[i] = 0.0f;
                i = idx + 1;
                this.vertices[idx] = 0.0f;
                idx = i + 1;
                this.vertices[i] = 0.0f;
                i = idx + 1;
                this.vertices[idx] = 0.0f;
                x++;
                idx = i;
            }
        }
        this.mesh.setVertices(this.vertices);
        updateVertices(this.curr, 0.0f);
    }

    public void updateZ(float z) {
        this.zDepthCoord = z;
        this.plane = new Plane(new Vector3(0.0f, 0.0f, this.zDepthCoord),
                new Vector3(1.0f, 0.0f, this.zDepthCoord),
                new Vector3(0.0f, 1.0f, this.zDepthCoord));
    }

    float interpolate(float alpha, int x, int y) {
        return (this.last[x][y] * alpha) + ((1.0f - alpha) * this.curr[x][y]);
    }

    void updateVertices(float[][] curr, float alpha) {
        FloatBuffer buffer = null;
        float floatsPerVertex = ((float) this.mesh.getVertexSize()) / 4.0f;
        if (this.updateDirectBufferAccess) {
            float floatBufferSize = ((float) this.mesh.getNumVertices()) * floatsPerVertex;
            buffer = this.mesh.getVerticesBuffer();
            if (!($assertionsDisabled || floatBufferSize == ((float) buffer.limit()))) {
                throw new AssertionError();
            }
        }
        int idx = 0;
        short y = (short) 0;
        while (y <= this.height) {
            short x = (short) 0;
            while (x <= this.width) {
                float xOffset = 0.0f;
                float yOffset = 0.0f;
                if (x > (short) 0 && x < this.width && y > (short) 0 && y < this.height) {
                    xOffset = interpolate(alpha, x - 1, y) - interpolate(alpha, x + 1, y);
                    yOffset = interpolate(alpha, x, y - 1) - interpolate(alpha, x, y + 1);
                }
                if (this.updateDirectBufferAccess) {
                    buffer.put(idx + 0, ((float) x) + this.posx);
                    buffer.put(idx + 1, ((float) y) + this.posy);
                    buffer.put(idx + 2, this.zDepthCoord);
                } else {
                    int i = idx + 1;
                    this.vertices[idx] = ((float) x) + this.posx;
                    idx = i + 1;
                    this.vertices[i] = ((float) y) + this.posy;
                    i = idx + 1;
                    this.vertices[idx] = this.zDepthCoord;
                    idx = i;
                }
                float u = (((((float) x) + xOffset) * (((float) this.texture.getWidth()) / ((float) this.width))) + 0.0f) / ((float) this.texture.getWidth());
                float v = 1.0f - ((((((float) y) + yOffset) * (((float) this.texture.getHeight()) / ((float) this.height))) + 0.0f) / ((float) this.texture.getHeight()));
                if (this.updateDirectBufferAccess) {
                    buffer.put(idx + 3, u);
                    buffer.put(idx + 4, v);
                    idx = (int) (((float) idx) + floatsPerVertex);
                } else {
                    int i = idx + 1;
                    this.vertices[idx] = u;
                    idx = i + 1;
                    this.vertices[i] = v;
                }
                x++;
            }
            y++;
        }
        if (!this.updateDirectBufferAccess) {
            this.mesh.setVertices(this.vertices);
        }
    }
}
