/**
 * Kdniao.com Inc.
 * Copyright (c) 2014-2017 All Rights Reserved.
 */
package zzq.zzqsimpleframeworkcommon.util;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * 
 * @author huangboke
 * @version $Id: GZIPUtils.java, v 0.1 2017年7月11日 上午9:49:50 huangboke Exp $
 */
public abstract class GZIPUtil {

    public static byte[] compress(byte[] source) throws IOException {
        if (source == null | source.length == 0) {
            return source;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzipOut = null;
        try {
            gzipOut = new GZIPOutputStream(out);
            gzipOut.write(source);
            gzipOut.close();
            return out.toByteArray();
        } finally {
            IOUtils.closeQuietly(gzipOut);
            IOUtils.closeQuietly(out);
        }
    }

    public static byte[] decompress(byte[] source) throws IOException {
        if (source == null | source.length == 0) {
            return source;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(source);
        GZIPInputStream gzipIn = null;
        try {
            gzipIn = new GZIPInputStream(in);
            byte[] buffer = new byte[512];
            int n;
            while ((n = gzipIn.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
            gzipIn.close();
            return out.toByteArray();
        } finally {
            IOUtils.closeQuietly(gzipIn);
            IOUtils.closeQuietly(out);
        }
    }
}