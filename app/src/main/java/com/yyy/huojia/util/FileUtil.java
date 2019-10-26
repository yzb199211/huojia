package com.yyy.huojia.util;

import java.io.File;
import java.io.IOException;

public class FileUtil {
    /*创建一级目录*/
    public static File  creatDir(String uri) {
        File file = new File(uri);
        if (!file.exists()) {
            try {
                //创建文件
                file.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /*新建文件*/
    public static File creatFile(String uri) {
        File file = new File(uri);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /*新建文件*/
    public static File creatFile(File dirFlie, String uri) {
        File file = new File(dirFlie, uri);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }
}
