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
 * 压缩解压工具类
 *
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2023-01-03 11:17
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
        if (source == null || source.length == 0 || !isGZIPMagic(source)) {
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

    /**
     * 判断是不是gzip压缩的
     *
     * @param magic
     * @return
     */
    public static boolean isGZIPMagic(byte[] magic) {
        return (magic != null &&
                magic.length > 2 &&
                magic[0] == (byte) 0x1F &&
                magic[1] == (byte) 0x8B &&
                magic[2] == (byte) 0x08);
    }
}