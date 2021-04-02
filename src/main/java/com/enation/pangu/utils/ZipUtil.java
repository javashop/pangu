package com.enation.pangu.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @Author cs
 * @Date 2020/12/31 11:51
 * @Version 1.0
 */
public class ZipUtil {

    /**
     * zip文件解压
     * @param inputFile  待解压文件夹/文件
     * @param destDirPath  解压路径
     */
    public static void unZipUncompress(String inputFile,String destDirPath){
        File srcFile = new File(inputFile);//获取当前压缩文件
        InputStream is = null;
        FileOutputStream fos = null;
        try{
            // 判断源文件是否存在
            if (!srcFile.exists()) {
                throw new Exception(srcFile.getPath() + "所指文件不存在");
            }
            ZipFile zipFile = new ZipFile(srcFile);//创建压缩文件对象
            //开始解压
            Enumeration<?> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                // 如果是文件夹，就创建个文件夹
                if (entry.isDirectory()) {
                    srcFile.mkdirs();
                } else {
                    // 如果是文件，就先创建一个文件，然后用io流把内容copy过去
                    File targetFile = new File(destDirPath + "/" + entry.getName());
                    // 保证这个文件的父文件夹必须要存在
                    if (!targetFile.getParentFile().exists()) {
                        targetFile.getParentFile().mkdirs();
                    }
                    targetFile.createNewFile();
                    // 将压缩文件内容写入到这个文件中
                    is = zipFile.getInputStream(entry);
                    fos = new FileOutputStream(targetFile);
                    int len;
                    byte[] buf = new byte[1024];
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                    // 关流顺序，先打开的后关闭
                    fos.close();
                    is.close();
                }
            }
            zipFile.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //删除压缩文件
            boolean delete = srcFile.delete();
            System.out.println(delete);
        }
    }
}
