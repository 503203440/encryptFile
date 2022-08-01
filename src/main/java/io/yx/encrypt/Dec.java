package io.yx.encrypt;

import cn.hutool.core.io.FileUtil;

import java.io.File;
import java.util.Scanner;

/**
 * @author YX
 * @date 2022/8/1 9:48
 */
public class Dec {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        if (args.length != 1) {
            System.out.println("use java -cp encrypt.jar io.yx.encrypt.io.yx.encrypt.Dec encryptedFile");
            System.exit(1);
        }
        System.out.println("password:");
        String password = scanner.nextLine();

        File encryptedFile = new File(args[0]);
        String filename = FileUtil.getName(encryptedFile);
        File tempFile = FileUtil.touch(System.getProperty("java.io.tmpdir") + filename);

        JasyptUtil.fastDecrypt(password, encryptedFile, tempFile);
        FileUtil.move(tempFile, encryptedFile, true);
        System.out.println("解密成功:" + encryptedFile.getAbsolutePath());
    }
}
