import cn.hutool.core.io.FileUtil;
import org.jasypt.encryption.pbe.PooledPBEByteEncryptor;
import org.jasypt.salt.SaltGenerator;
import org.jasypt.salt.ZeroSaltGenerator;
import org.jasypt.util.binary.BasicBinaryEncryptor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author YX
 * @date 2022/7/29 15:32
 * 加密缓冲区数组为1024,解密缓冲数组为1040,因为加密的时候1024被加密后加入了16个byte,所以步长必须与之对应
 * 否则无法解密
 */
public class JasyptUtil {

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
        byte[] buffer = new byte[1024];
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
            byte[] buffer = new byte[1040];
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
        PooledPBEByteEncryptor pooledPBEByteEncryptor = new PooledPBEByteEncryptor();
        pooledPBEByteEncryptor.setPassword(password);
        pooledPBEByteEncryptor.setPoolSize(Runtime.getRuntime().availableProcessors());
        try (
                FileInputStream fis = new FileInputStream(sourceFile);
                FileOutputStream fos = new FileOutputStream(encryptedFile);
        ) {
            byte[] buffer = new byte[1024];
            while (fis.read(buffer) != -1) {
                byte[] decrypt = pooledPBEByteEncryptor.encrypt(buffer);
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
     * @param encryptedFile 被加密文件
     * @param targetFile    还原文件
     */
    public static void fastDecrypt(String password, File encryptedFile, File targetFile) {
        PooledPBEByteEncryptor pooledPBEByteEncryptor = new PooledPBEByteEncryptor();
        pooledPBEByteEncryptor.setPassword(password);
        pooledPBEByteEncryptor.setPoolSize(Runtime.getRuntime().availableProcessors());
        try (
                FileInputStream fis = new FileInputStream(encryptedFile);
                FileOutputStream fos = new FileOutputStream(targetFile);
        ) {
            byte[] buffer = new byte[1040];
            while (fis.read(buffer) != -1) {
                byte[] decrypt = pooledPBEByteEncryptor.decrypt(buffer);
                fos.write(decrypt);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
