package io.yx.encrypt;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.io.FileUtil;

import java.io.File;
import java.util.Objects;
import java.util.Scanner;

/**
 * @author YX
 * @date 2022/8/1 9:48
 */
public class Enc {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("use java -cp encrypt.jar io.yx.encrypt.io.yx.encrypt.Enc sourceFile encryptedFile");
            System.exit(1);
        }

        File sourceFile = new File(args[0]);
        if (!FileUtil.exist(sourceFile)) {
            System.err.println("文件不存在");
            System.exit(1);
        }

        System.out.println("password:");
        String pass1 = scanner.nextLine();
        System.out.println("retype password:");
        String pass2 = scanner.nextLine();
        if (!Objects.equals(pass1, pass2)) {
            System.out.println("Sorry, passwords do not match");
            System.exit(1);
        }

        File encryptedFile = FileUtil.touch(args[1]);
        JasyptUtil.fastEncrypt(pass1, sourceFile, encryptedFile);
        System.out.println("加密成功:" + encryptedFile.getAbsolutePath());
    }
}
