package org.bird.breeze.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author pompey
 */
public class FileUtils {

    public static File createFile(String realFilePath, String content) throws IOException {
        FileWriter fw = null;
        BufferedWriter bw = null;

        File file = new File(realFilePath);

        if(file.exists()){
            file.delete();
        }
        try {
            file.createNewFile();
            fw = new FileWriter(file.getAbsoluteFile());
            bw = new BufferedWriter(fw);
            bw.write(content);
            bw.flush();
        } catch (IOException e) {
            throw new IOException("创建文件【"+realFilePath+"】失败", e);
        } finally{
            if (bw != null) {
                bw.close();
            }
            if (fw != null) {
                fw.close();
            }
        }

        return file;
    }

    public static File appendFile(String realFilePath, String content) throws IOException {
        FileWriter fw = null;
        BufferedWriter bw = null;

        File file = new File(realFilePath);
        try {
            if(!file.exists()){
                file.createNewFile();
            }
            fw = new FileWriter(file.getAbsoluteFile(),true);
            bw = new BufferedWriter(fw);
            bw.write(content);
            bw.flush();
        } catch (IOException e) {
            throw new IOException("写入文件【"+realFilePath+"】失败", e);
        } finally{
            if (bw != null) {
                bw.close();
            }
            if (fw != null) {
                fw.close();
            }
        }


        return file;
    }

    public static List<String> getFileNameList(String realFilePath, String suffix){

        return getFileNameList(realFilePath,suffix,true);
    }

    public static List<String> getFileNameList(String realFilePath, String suffix, boolean sort){

        File dir = new File(realFilePath);
        // 该文件目录下文件全部放入数组
        File[] files = dir.listFiles();
        List<String> fileList = new ArrayList<>();

        if (files != null) {
            for (int i = 0; i < files.length; i++) {

                String fileName = files[i].getName();
                // 判断是文件还是文件夹
                if (files[i].isDirectory()) {
                    // 获取文件绝对路径
                    getFileNameList(files[i].getAbsolutePath(), suffix, sort);
                // 判断文件名是否以目标后缀结尾
                } else if (fileName.endsWith(suffix)) {
                    String strFileName = files[i].getAbsolutePath();
                    fileList.add(strFileName);
                }
            }

        }
        if(sort){
            fileList.sort(String::compareTo);
        }
        return fileList;
    }
}
