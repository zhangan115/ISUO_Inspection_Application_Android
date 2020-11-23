package com.isuo.inspection.application.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Environment;


import java.io.File;
import java.util.Objects;


/**
 * Created by Yangzb on 2019/6/13 12:53
 * E-mail: yangzongbin@si-top.com
 * Describe:
 */
public class FileUtil {
    /*
     * Java文件操作 获取文件扩展名
     * */
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }

    /**
     * 判读文件是否是图片
     */
    public static Boolean isImg(String filename) {
        if (getExtensionName(filename).equals("png") || getExtensionName(filename).equals("jpg") || getExtensionName(filename).equals("jpeg")) {
            return true;
        }
        return false;
    }


    /**
     * 得到资源文件中图片的Uri
     *
     * @param context 上下文对象
     * @param id      资源id
     * @return Uri
     */
    public static Uri getUriFromDrawableRes(Context context, int id) {
        Resources resources = context.getResources();
        String path = ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + resources.getResourcePackageName(id) + "/"
                + resources.getResourceTypeName(id) + "/"
                + resources.getResourceEntryName(id);
        return Uri.parse(path);
    }

    /**
     * 获取文件cache路径
     */
    public static File getDiskCachePath(Context context) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
            return new File(context.getExternalCacheDir().getPath() + "/files/");
        } else {
            return new File(context.getCacheDir().getPath() + "/files/");
        }
    }

    /**
     * 获取文件file路径
     */
    public static File getDiskFilePath() {
        return new File(Environment.getExternalStorageDirectory().getPath() + "/文档/");
    }

    public static String getFileSize(File file) {
        String sizeStr = "";
        for (String f : Objects.requireNonNull(file.list())) {

        }
        return sizeStr;
    }


    //定义成员变量，用于累加文件大小
    private static long size = 0;

    public static void initFile() {
        size = 0;
    }

    //统计目录大小的方法
    public static long getDirSize(File file) {
        if (file.isFile()) {
            //如果是文件，获取文件大小累加
            size += file.length();
        } else if (file.isDirectory()) {
            //获取目录中的文件及子目录信息
            File[] f1 = file.listFiles();
            assert f1 != null;
            for (File value : f1) {
                //调用递归遍历f1数组中的每一个对象
                getDirSize(value);
            }
        }
        return size;
    }
}