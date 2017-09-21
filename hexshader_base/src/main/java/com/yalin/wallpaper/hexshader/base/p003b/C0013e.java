package com.yalin.wallpaper.hexshader.base.p003b;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

public class C0013e {
    private static final Charset f33a = Charset.forName("UTF-8");
    private static final byte[] f34b = new byte[4096];

    public static OutputStream m30a(InputStream inputStream, OutputStream outputStream) {
        try {
            while (true) {
                int read = 0;
                read = inputStream.read(f34b);
                if (read == -1) {
                    return outputStream;
                }
                outputStream.write(f34b, 0, read);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void m31a(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
            }
        }
    }

    public static byte[] m32a(InputStream inputStream) {
        return ((ByteArrayOutputStream) C0013e.m30a(inputStream, new ByteArrayOutputStream())).toByteArray();
    }

    public static String m33b(InputStream inputStream) {
        return new String(C0013e.m32a(inputStream), f33a);
    }
}
