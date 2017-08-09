package com.yalin.wallpaper.boids;

import android.util.Log;

import java.util.ArrayList;

public class BinLattice {
    
    private static final String TAG = "BinLattice";


    public interface Visitor {
        void visit(Boid a, Boid b);
    }

    public class Bin {
        private final ArrayList<Boid> index = new ArrayList<Boid>();
        public int count = 0;
        
        public void add(Boid boid) {
            index.add(boid);
            count++;
        }

        public void remove(Boid boid) {
            index.remove(boid);
            count--;
        }

        public void clear() {
            index.clear();
            count = 0;
        }

        public Boid get(int idx) {
            return index.get(idx);
        }
    }

    private static final int FACTOR = 160;
    private Bin[][][] bins;

    private Profile profile;
    private int width, height, depth;

    public BinLattice(Profile profile) {
        this.allocate(profile);
    }
    
    void allocate(Profile profile) {
        this.profile = profile;
        
        width = ((int)profile.MAX_X - (int)profile.MIN_X) / FACTOR;
        height = ((int)profile.MAX_Y - (int)profile.MIN_Y) / FACTOR;
        depth = ((int)profile.MAX_Z - (int)profile.MIN_Z) / FACTOR;

        bins = new Bin[width][height][depth];

        for(int i=0; i<width; i++) {
            for(int j=0; j<height; j++) {
                for(int k=0; k<depth; k++) {
                    bins[i][j][k] = new Bin();
                }
            }
        }
    }
    
    public void clear() {
        for(int i=0; i<width; i++) {
            for(int j=0; j<height; j++) {
                for(int k=0; k<depth; k++) {
                    bins[i][j][k].clear();
                }
            }
        }
    }

    public void histogram() {

        for(int i=0; i<width; i++) {
            for(int j=0; j<height; j++) {
                for(int k=0; k<depth; k++) {

                    Bin bin = bins[i][j][k];
                    if(bin.count > 0) {
                        if (BuildConfig.DEBUG)Log.d(TAG, "bin[" + i + ", " + j + ", " + k + "] -> " + bin.count);
                    }
                }
            }
        }
    }

    private Bin index(Boid boid) {
        
        int x = ((int)boid.position.x - (int)profile.MIN_X) / FACTOR;
        int y = ((int)boid.position.y - (int)profile.MIN_Y) / FACTOR;
        int z = ((int)boid.position.z - (int)profile.MIN_Z) / FACTOR;

        if(x >= 0 && y >= 0 && z >= 0 && 
           x < width && y < height && z < depth) {
            return bins[x][y][z];
        }

        return null;
    }


    public void add(Boid boid) {
        Bin bin = index(boid);
        if(bin != null) {
            bin.add(boid);
        }
    }

    public void add(Boid[] boids) {
        for(int i=0; i<boids.length; i++) {
            add(boids[i]);
        }
    }

    public void remove(Boid boid) {
        Bin bin = index(boid);
        if(bin != null) {
            bin.remove(boid);
        }
    }

    public int scan(Boid boid, int radius, Visitor v, int limit) {
        int rv = 0;

        int bin_radius = radius / FACTOR;
        int diameter = bin_radius * 2;

        int x = ((int)boid.position.x - (int)profile.MIN_X) / FACTOR - bin_radius;
        int y = ((int)boid.position.y - (int)profile.MIN_Y) / FACTOR - bin_radius;
        int z = ((int)boid.position.z - (int)profile.MIN_Z) / FACTOR - bin_radius;
 
        for(int i=0; i<diameter; i++) {
            for(int j=0; j<diameter; j++) {
                for(int k=0; k<diameter; k++) {
                    
                    int bx = x + i;
                    int by = y + j;
                    int bz = z + k;

                    if(bx >= 0 && 
                       by >= 0 && 
                       bz >= 0 && 
                       bx < bins.length &&
                       by < bins[bx].length && 
                       bz < bins[bx][by].length) {

                        Bin bin = bins[bx][by][bz];
                        for(int l=0; l<bin.count; l++) {

                            Boid b = (Boid)bin.get(l);

                            if(rv < limit && boid.position.distance(b.position) < radius) {
                                v.visit(boid, b);
                                rv++;
                            }

                            if (rv > limit)
                                return rv;
                        }
                    }
                }
            }
        }
        
        return rv;
    }
 
}
