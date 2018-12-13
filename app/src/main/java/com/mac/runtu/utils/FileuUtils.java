package com.mac.runtu.utils;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.GZIPOutputStream;

import static com.mac.runtu.fragment.BaseFragment02.TAG;

/**
 * Description:将路径中的文件编码成字符串
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/10/22 15:52
 */
public class FileuUtils {

    //文件转换成字符串
    public static String file2String(String path) {
        String byt2str = "";

        try {
            //FileInputStream fis = new FileInputStream(new File(path));

            //byt2str = Base64.encode(readInputStream(fis));

            byt2str = Base64.encode(ImageCompressUtils.getImage4Stream(path));

        } catch (Exception e) {


            e.printStackTrace();
        }

        return byt2str;
    }

    /**
     * 根据文件路径 将数据转成字符串
     *
     * @param path
     * @return
     */
    public static String file2StringGzip(String path) {
        String byt2str = "";
        try {

            byt2str = Base64.encode(ImageCompressUtils.getImage4Stream(path));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return byt2str;
    }

    //将流中的数据转成字符串
    public static String file2StringGzip(InputStream is) {
        String byt2str = "";
        try {
            byt2str = Base64.encode(readInputStreamAndZip(is));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return byt2str;
    }

    //将流中的数据转成字符串
    public static String file2String(InputStream is) {
        String byt2str = "";
        try {
            byt2str = Base64.encode(readInputStream(is));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return byt2str;
    }


    public static byte[] readInputStream(InputStream inStream) throws
            Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }


    /**
     * 压缩
     *
     * @param inStream
     * @return
     * @throws Exception
     */
    public static byte[] readInputStreamAndZip(InputStream inStream) throws
            Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        GZIPOutputStream gzip = new GZIPOutputStream(outStream);

        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            gzip.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }

    public static File compressImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 500) {  //循环判断如果压缩后图片是否大于500kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            options -= 10;//每次都减少10
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            long length = baos.toByteArray().length;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date(System.currentTimeMillis());
        String filename = format.format(date);
        File file = new File(Environment.getExternalStorageDirectory(),filename+".png");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            try {
                fos.write(baos.toByteArray());
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        recycleBitmap(bitmap);
        return file;
    }

    public static void recycleBitmap(Bitmap... bitmaps) {
        if (bitmaps==null) {
            return;
        }
        for (Bitmap bm : bitmaps) {
            if (null != bm && !bm.isRecycled()) {
                bm.recycle();
            }
        }
    }

}
