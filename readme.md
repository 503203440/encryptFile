### 使用jasyptor加解密文件

编译项目

```shell
mvn clean package -DskipTests=true
```

加密文件

```shell
java -cp encrypt.jar io.yx.encrypt.Enc [源文件全路径] [加密后文件路径]
```

解密文件

```shell
java -cp encrypt.jar io.yx.encrypt.Dec [待解密文件路径]
```



