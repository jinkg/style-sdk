package com.yalin.livewallpaper3d;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

public class GLTriangle {
	private FloatBuffer _vertexBuffer;
	private final int _nrOfVertices = 3;

	private ShortBuffer _indexBuffer;

	public GLTriangle() {
		init();
	}

	private void init() {
		// We use ByteBuffer.allocateDirect() to get memory outside of
		// the normal, garbage collected heap. I think this is done
		// because the buffer is subject to native I/O.
		// See http://download.oracle.com/javase/1.4.2/docs/api/java/nio/ByteBuffer.html#direct

		// 3 is the number of coordinates to each vertex.
		_vertexBuffer = BufferFactory.createFloatBuffer(_nrOfVertices * 3);

		_indexBuffer = BufferFactory.createShortBuffer(_nrOfVertices);

		// Coordinates for the vertexes of the triangle.
		float[] coords = {
				-1f, -1f,  0f,  // (x1, y1, z1)
				 1f, -1f,  0f,  // (x2, y2, z2)
				 0f,  1f,  0f   // (x3, y3, z3)
		};

		short[] _indicesArray = {0, 1, 2};

		_vertexBuffer.put(coords);
		_indexBuffer.put(_indicesArray);

		_vertexBuffer.position(0);
		_indexBuffer.position(0);
	}

	public void draw(GL10 gl) {
		// 3 coordinates in each vertex
		// 0 is the space between each vertex. They are densely packed
		//   in the array, so the value is 0
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, getVertexBuffer());

		// Draw the primitives, in this case, triangles.
		gl.glDrawElements(GL10.GL_TRIANGLES, _nrOfVertices, GL10.GL_UNSIGNED_SHORT, _indexBuffer);
	}

	private FloatBuffer getVertexBuffer() {
		return _vertexBuffer;
	}
}
