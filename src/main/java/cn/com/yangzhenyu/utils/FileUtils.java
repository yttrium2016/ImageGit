package cn.com.yangzhenyu.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA
 * Created By 杨振宇
 * Date: 2017/9/13
 * Time: 13:38
 */
public class FileUtils {


    /**
     * 删除文件或者文件夹以下所有文件
     *
     * @param file
     */
    public static void deleteAllFilesOfDir(File file) {
        if (!file.exists())
            return;
        if (file.isFile()) {
            boolean delete = file.delete();
            if (!delete) {
                System.gc();    //回收资源
                file.delete();
            }
            return;
        }
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            deleteAllFilesOfDir(files[i]);
        }
        file.delete();
    }

    /**
     * 获取所有文件的名字
     *
     * @param file
     * @return
     */
    public static List<String> getFileNames(File file) {
        List<String> result = new ArrayList<>();
        if (!file.exists())
            return null;
        if (file.isFile()) {
            result.add(file.getName());
            return result;
        }
        File[] files = file.listFiles();
        if (files.length > 0) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile())
                    result.add(files[i].getName());
            }
            return result;
        }
        return null;
    }


    /**
     * 删除单个文件
     *
     * @param file
     * @return
     */
    public static boolean delFile(File file) {
        if (!file.exists()) {
            return false;
        }
        if (file.isFile())
            return file.delete();
        else
            return false;
    }

    /**
     * 读取txt文件的内容
     *
     * @param file 想要读取的文件对象
     * @return 返回文件内容
     */
    public static String File2String(File file) {
        StringBuilder result = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            String s = null;
            while ((s = br.readLine()) != null) {//使用readLine方法，一次读一行
                result.append(System.lineSeparator() + s);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    public static void rename(File file) {
        rename(file, null);
    }

    public static void rename(File file, String[] whileList) {
        for (File f : file.listFiles()) {
            String name = f.getName();
            boolean flag = true;
            if (whileList != null && whileList.length > 0) {
                for (String value : whileList) {
                    if (value.equals(name)) {
                        flag = false;
                    }
                }
            }
            if (flag && !Pattern.matches("\\d{17} | \\d{14}", name.substring(0, name.indexOf(".")))) {
                f.renameTo(new File(file.getPath() + "\\" + name.replace(name.substring(0, name.indexOf(".")), StringUtils.getNumberName())));
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }


    }
}
