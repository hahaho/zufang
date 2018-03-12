package com.apass.zufang.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.apass.gfb.framework.utils.ImageUtils;

/**
 * 文件处理工具
 * 
 * @author zhanwendong
 *
 */
public class FileUtilsCommons {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtilsCommons.class);

    private FileUtilsCommons() {

    }

    /**
     * 公用上传文件方法
     * 
     * @param fatherPath 根路径：统一为/data/nfs/zufang
     * @param file：文件
     * @param urlOld:特定目录
     *            文件
     */
    public static void uploadFilesUtil(String fatherPath, String urlOld, MultipartFile file) {
        InputStream in = null;
        FileOutputStream out = null;

        try {
            String url = fatherPath + urlOld;
            // 判断目录是否存在
            String fil = new File(url).getParent();
            if (!new File(fil).isDirectory()) {
                new File(fil).mkdirs();
            }
            in = file.getInputStream();
            out = new FileOutputStream(url);

            // 每次读取的字节长度
            int n = 0;
            // 存储每次读取的内容
            byte[] bb = new byte[1024];
            while ((n = in.read(bb)) != -1) {
                // 将读取的内容，写入到输出流当中
                out.write(bb, 0, n);
            }

        } catch (Exception e) {
            LOGGER.error("文件上传失败", e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    LOGGER.error("关闭输入输出流异常", e);
                } // 关闭输入输出流
            }
            if (out != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    LOGGER.error("关闭输入输出流异常", e);
                }
            }
        }
    }
    /**
     * 公用上传文件方法
     * 
     * @param fatherPath
     */
    public static void uploadImg(String fatherPath,String fileName,MultipartFile file) {
        FileOutputStream out = null;
        InputStream in = null;
        try {
            // 判断目录是否存在
            if (!new File(fatherPath).isDirectory()) {
                new File(fatherPath).mkdirs();
            }
            out = new FileOutputStream(fatherPath+fileName);
            in = file.getInputStream();

            // 每次读取的字节长度
            int len = 0;
            // 存储每次读取的内容
            byte[] bb = new byte[1024];
            while ((len = in.read(bb)) != -1) {
                // 将读取的内容，写入到输出流当中
                out.write(bb, 0, len);
            }

        } catch (Exception e) {
            LOGGER.error("文件上传失败", e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    LOGGER.error("关闭输入输出流异常", e);
                } // 关闭输入输出流
            }
        }
    }
}