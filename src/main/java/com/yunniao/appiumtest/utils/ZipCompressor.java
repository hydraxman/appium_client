package com.yunniao.appiumtest.utils;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipCompressor {
    static final int BUFFER = 8192;
    private File zipFile;

    /**
     * 压缩文件构造函数
     *
     * @param pathName 压缩的文件存放目录
     */
    public ZipCompressor(String pathName) {
        zipFile = new File(pathName);
    }

    /**
     * 执行压缩操作
     *
     * @param srcPathName 被压缩的文件/文件夹
     */
    public void compressExe(String srcPathName) {
        File file = new File(srcPathName);
        if (!file.exists()) {
            throw new RuntimeException(srcPathName + "不存在！");
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(zipFile);
            CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream, new CRC32());
            ZipOutputStream out = new ZipOutputStream(cos);
            String basedir = "";
            compressByType(file, out, basedir);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
//            logger.error("执行压缩操作时发生异常:"+e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 判断是目录还是文件，根据类型（文件/文件夹）执行不同的压缩方法
     *
     * @param file
     * @param out
     * @param basedir
     */
    private void compressByType(File file, ZipOutputStream out, String basedir) {
        /* 判断是目录还是文件 */
        if (file.isDirectory()) {
//            logger.info("压缩：" + basedir + file.getName());
            this.compressDirectory(file, out, basedir);
        } else {
//            logger.info("压缩：" + basedir + file.getName());
            this.compressFile(file, out, basedir);
        }
    }

    /**
     * 压缩一个目录
     *
     * @param dir
     * @param out
     * @param basedir
     */
    private void compressDirectory(File dir, ZipOutputStream out, String basedir) {
        if (!dir.exists()) {
            return;
        }

        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            /* 递归 */
            compressByType(files[i], out, basedir + dir.getName() + "/");
        }
    }

    /**
     * 压缩一个文件
     *
     * @param file
     * @param out
     * @param basedir
     */
    private void compressFile(File file, ZipOutputStream out, String basedir) {
        if (!file.exists()) {
            return;
        }
        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            ZipEntry entry = new ZipEntry(basedir + file.getName());
            out.putNextEntry(entry);
            int count;
            byte[] data = new byte[BUFFER];
            while ((count = bis.read(data, 0, BUFFER)) != -1) {
                out.write(data, 0, count);
            }
            bis.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void doSCP() throws Exception {
        //String hostname, String user, String psw, String localPath, String remotePath;
        //1, 创建一个连接connection对象
        String hostname = "172.16.41.23";
        Connection conn = new Connection(hostname);
        //2, 进行连接操作
        conn.connect();
        //3, 进行连接访问授权验证
        String user = "yunniao";
        String psw = "wbyyunniao";
        boolean isAuth = conn.authenticateWithPassword(user, psw);
        if (!isAuth)
            throw new Exception("Authentication failed");
        //4, 创建一个SCPClient对象
        SCPClient client = new SCPClient(conn);
        try {
            String localPath = "/Users/melinda/test/beeper_test/appium_client/report/report_20160809_205427.zip";
            String remotePath = "/Users/yunniao/workspace/";
            client.put(localPath, remotePath);
//            for(int i = files.length - 1; i >= 0 ; i --) {
//                File cur = files[i];
//                String curname = cur.getName();
//                File rnm = null;
//                if(cur.exists()) {
//                    String name = localPath + File.separatorChar + cur.getName() + ".scping";
//                    rnm = new File(name);
//                    cur.renameTo(rnm);
//                } else {
//                    continue;
//                }
//                if((rnm != null) && rnm.exists()) {
//                    String scplocal = localPath + File.separatorChar + rnm.getName();
//                    //5, 进行文件scp远程拷贝
//                    client.put(scplocal, curname, remotePath, "0777");
//                    rnm.delete();
//                }
//            }
        } finally {
            //6, 使用完关闭连接
            conn.close();
        }
    }
}