package com.yunniao.appiumtest.utils;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import com.yunniao.appiumtest.Constants;
import io.appium.java_client.AppiumDriver;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by melinda on 2016/8/3.
 */
public class PhotoUtil {
    /**
     * 默认截图log,type=1
     * type=2,为报错截图
     *
     * @param driver
     */
    public static void takeScreenShot(AppiumDriver driver) {
        takeScreenShot(driver, 1);
    }

    public static void takeScreenShot(AppiumDriver driver, Integer type) {
        String reportName = KeyValueUtil.get(Constants.REPORT_NAME);

        File screen = driver.getScreenshotAs(OutputType.FILE);
        File photoDir = new File("report/" + reportName + "/photo").getAbsoluteFile();
        if (!photoDir.exists()) {
            if (photoDir.mkdir()) {
                LogUtil.i("photo文件夹创建成功");
            }
        }

        File screenFile = new File(photoDir + "/" + type + "_" + getCurrentDateTime() + ".png");
        try {
            FileUtils.copyFile(screen, screenFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getCurrentDateTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");//设置日期格式
        return df.format(new Date());
    }

    public static boolean fileToZip(String sourceFilePath, String zipFilePath, String fileName) {
        boolean flag = false;
        File sourceFile = new File(sourceFilePath);
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        ZipOutputStream zos = null;

        if (sourceFile.exists() == false) {
            System.out.println("待压缩的文件目录：" + sourceFilePath + "不存在.");
        } else {
            try {
                File zipFile = new File(zipFilePath + "/" + fileName + ".zip");
                if (zipFile.exists()) {
                    System.out.println(zipFilePath + "目录下存在名字为:" + fileName + ".zip" + "打包文件.");
                } else {
                    File[] sourceFiles = sourceFile.listFiles();
                    if (null == sourceFiles || sourceFiles.length < 1) {
                        System.out.println("待压缩的文件目录：" + sourceFilePath + "里面不存在文件，无需压缩.");
                    } else {
                        fos = new FileOutputStream(zipFile);
                        zos = new ZipOutputStream(new BufferedOutputStream(fos));
                        byte[] bufs = new byte[1024 * 10];
                        for (int i = 0; i < sourceFiles.length; i++) {
                            //创建ZIP实体，并添加进压缩包
                            ZipEntry zipEntry = new ZipEntry(sourceFiles[i].getName());
                            zos.putNextEntry(zipEntry);
                            //读取待压缩的文件并写进压缩包里
                            fis = new FileInputStream(sourceFiles[i]);
                            bis = new BufferedInputStream(fis, 1024 * 10);
                            int read = 0;
                            while ((read = bis.read(bufs, 0, 1024 * 10)) != -1) {
                                zos.write(bufs, 0, read);
                            }
                        }
                        flag = true;
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } finally {
                //关闭流
                try {
                    if (null != bis) bis.close();
                    if (null != zos) zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        }
        return flag;
    }

    public static String setFile() {
        File f = new File("report").getAbsoluteFile();
        if (!f.exists()) {
            if (f.mkdir()) {
                //
            }
        }

        //添加随机数
        Random rand = new Random();
        int randNum = 100000 + rand.nextInt(899999);
        String reportFile = "report_" + getCurrentDateTime() + "_" + randNum;

        File file = new File("report/" + reportFile).getAbsoluteFile();
        if (!file.exists()) {
            if (file.mkdir()) {
//				LogUtil.i(reportFile + "文件夹创建成功");
            }
        }
        return reportFile;
    }

    public static void renameFile(String file, String toFile) {

        File toBeRenamed = new File(file);
        //检查要重命名的文件是否存在，是否是文件
        if (!toBeRenamed.exists() || toBeRenamed.isDirectory()) {

            System.out.println("File does not exist: " + file);
            return;
        }

        File newFile = new File(toFile);

        //修改文件名
        if (toBeRenamed.renameTo(newFile)) {
            System.out.println("File has been renamed.");
        } else {
            System.out.println("Error renmaing file");
        }

    }

    public static void renameDirectory(String fromDir, String toDir) {

        File from = new File(fromDir);

        if (!from.exists() || !from.isDirectory()) {
            System.out.println("Directory does not exist: " + fromDir);
            return;
        }

        File to = new File(toDir);

        //Rename
        if (from.renameTo(to))
            System.out.println("Success!");
        else
            System.out.println("Error");

    }

    public static void doSCP(String hostname, String user, String psw, String localPath, String remotePath) throws Exception {
        //1, 创建一个连接connection对象
        Connection conn = new Connection(hostname);
        //2, 进行连接操作
        conn.connect();
        //3, 进行连接访问授权验证
//		if (conn.isAuthMethodAvailable(user, "publickey")) {
//			System.out.println("--> public key auth method supported by server");
//		} else {
//			System.out.println("--> public key auth method not supported by server");
//		}
//		if (conn.isAuthMethodAvailable(user, "keyboard-interactive")) {
//			System.out.println("--> keyboard interactive auth method supported by server");
//		} else {
//			System.out.println("--> keyboard interactive auth method not supported by server");
//		}
//		if (conn.isAuthMethodAvailable(user, "password")) {
//			System.out.println("--> password auth method supported by server");
//		} else {
//			System.out.println("--> password auth method not supported by server");
//		}
        boolean isAuth = conn.authenticateWithPassword(user, psw);
        if (!isAuth)
            throw new Exception("Authentication failed");
        //4, 创建一个SCPClient对象
        SCPClient client = new SCPClient(conn);
        try {
            client.put(localPath, remotePath);
        } finally {
            //6, 使用完关闭连接
            conn.close();
        }
    }
}
