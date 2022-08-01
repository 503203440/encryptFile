package io.yx.encrypt;

import cn.hutool.core.thread.ThreadUtil;
import org.jasypt.encryption.pbe.PooledPBEByteEncryptor;
import org.jasypt.exceptions.EncryptionOperationNotPossibleException;
import org.jasypt.util.binary.BasicBinaryEncryptor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author YX
 * @date 2022/7/29 15:32
 * 加密缓冲区数组为1024,解密缓冲数组为1040,因为加密的时候1024被加密后加入了16个byte,所以步长必须与之对应
 * 加密缓冲区数组为10240,解密缓冲数组为10256,加密的时候10240被加密后加入了16个byte,所以步长必须与之对应
 * 否则无法解密
 */
public class JasyptUtil {

    private static final int encryptBufferSize = 10240;
    private static final int decrypteBufferSize = encryptBufferSize + 16;

    /**
     * 加密文件
     *
     * @param password      密码
     * @param sourceFile    源文件
     * @param encryptedFile 加密后的文件
     */
    public static void encrypt(String password, File sourceFile, File encryptedFile) {
        BasicBinaryEncryptor basicBinaryEncryptor = new BasicBinaryEncryptor();
        basicBinaryEncryptor.setPassword(password);
        // 定义缓冲区
        byte[] buffer = new byte[encryptBufferSize];
        try (
                FileInputStream fis = new FileInputStream(sourceFile);
                FileOutputStream fos = new FileOutputStream(encryptedFile)
        ) {
            while ((fis.read(buffer)) != -1) {
                byte[] encrypt = basicBinaryEncryptor.encrypt(buffer);
                fos.write(encrypt);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 解密文件
     *
     * @param password      密码
     * @param encryptedFile 被加密的文件
     * @param targetFile    解密后的文件
     */
    public static void decrypt(String password, File encryptedFile, File targetFile) {
        BasicBinaryEncryptor basicBinaryEncryptor = new BasicBinaryEncryptor();
        basicBinaryEncryptor.setPassword(password);
        try (FileInputStream fis = new FileInputStream(encryptedFile);
             FileOutputStream fos = new FileOutputStream(targetFile);
        ) {
            byte[] buffer = new byte[decrypteBufferSize];
            while (fis.read(buffer) != -1) {
                byte[] decrypt = basicBinaryEncryptor.decrypt(buffer);
                fos.write(decrypt);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 快速解密方法
     *
     * @param password      密码
     * @param sourceFile    源文件
     * @param encryptedFile 加密文件
     */
    public static void fastEncrypt(String password, File sourceFile, File encryptedFile) {
        CountDownLatch cdl = new CountDownLatch(1);
        long filesize = sourceFile.length();
        AtomicLong index = new AtomicLong(0);

        ConsoleProgressBar cpb = new ConsoleProgressBar(1, 100, 50);
        Thread processThread = new Thread(() -> {
            while (true) {
                ThreadUtil.sleep(100);
                cpb.show(((float) index.get() / filesize) * 100);
            }
        }, "encrypt-process-info");
        processThread.setDaemon(true);
        processThread.start();

        PooledPBEByteEncryptor pooledPBEByteEncryptor = new PooledPBEByteEncryptor();
        pooledPBEByteEncryptor.setPassword(password);
        pooledPBEByteEncryptor.setPoolSize(Runtime.getRuntime().availableProcessors());
        try (
                FileInputStream fis = new FileInputStream(sourceFile);
                FileOutputStream fos = new FileOutputStream(encryptedFile);
        ) {
            byte[] buffer = new byte[encryptBufferSize];
            while (fis.read(buffer) != -1) {
                byte[] decrypt = pooledPBEByteEncryptor.encrypt(buffer);
                fos.write(decrypt);
                index.addAndGet(buffer.length);
            }
            cpb.show(((float) index.get() / filesize) * 100);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 快速解密方法
     *
     * @param password      密码
     * @param encryptedFile 被加密文件
     * @param targetFile    还原文件
     */
    public static void fastDecrypt(String password, File encryptedFile, File targetFile) {
        long filesize = encryptedFile.length();
        AtomicLong index = new AtomicLong(0);
        ConsoleProgressBar cpb = new ConsoleProgressBar(1, 100, 50);
        Thread processThread = new Thread(() -> {
            while (true) {
                ThreadUtil.sleep(100);
                cpb.show(((float) index.get() / filesize) * 100);
            }
        }, "encrypt-process-info");
        processThread.setDaemon(true);
        processThread.start();

        PooledPBEByteEncryptor pooledPBEByteEncryptor = new PooledPBEByteEncryptor();
        pooledPBEByteEncryptor.setPassword(password);
        pooledPBEByteEncryptor.setPoolSize(Runtime.getRuntime().availableProcessors());
        try (
                FileInputStream fis = new FileInputStream(encryptedFile);
                FileOutputStream fos = new FileOutputStream(targetFile);
        ) {
            byte[] buffer = new byte[decrypteBufferSize];
            while (fis.read(buffer) != -1) {
                byte[] decrypt = pooledPBEByteEncryptor.decrypt(buffer);
                fos.write(decrypt);
                index.addAndGet(buffer.length);
            }
            cpb.show(((float) index.get() / filesize) * 100);
        } catch (EncryptionOperationNotPossibleException e) {
            throw new RuntimeException("解密失败,可能是密码错误或此文件不是encrypt加密的文件");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
