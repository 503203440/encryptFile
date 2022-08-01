package io.yx.encrypt;

import java.io.File;
import java.util.Scanner;

/**
 * @author YX
 * @date 2022/7/29 11:16
 */
public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("输入密码");
        String password = scanner.nextLine();

        String sourceFilePath = "C:/Users/YX/Pictures/y8r66k.jpg";
        String encryptedFilePath = "C:/Users/YX/Desktop/ENCRYPTED-y8r66k.jpg";

        // 加密
//        io.yx.encrypt.JasyptUtil.encrypt(password, new File(sourceFilePath), new File(encryptedFilePath));
        JasyptUtil.fastEncrypt(password, new File(sourceFilePath), new File(encryptedFilePath));

        // 解密
//        io.yx.encrypt.JasyptUtil.decrypt(password,new File(encryptedFilePath),new File("C:/Users/YX/Desktop/y8r66k.jpg"));
        JasyptUtil.fastDecrypt(password, new File(encryptedFilePath), new File("C:/Users/YX/Desktop/y8r66k.jpg"));
    }


}
