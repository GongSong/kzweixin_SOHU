package com.kuaizhan.utils;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 加密工具
 * Created by Mr.Jadyn on 2017/1/11.
 */
public final class EncryptUtil {

    /**
     * sha1加密
     *
     * @param str
     * @return
     */
    public static String sha1(String str) throws NoSuchAlgorithmException {
        MessageDigest md = null;
        String strDes = null;
        byte[] bt = str.getBytes();
        md = MessageDigest.getInstance("SHA-1");
        md.update(bt);
        strDes = bytes2Hex(md.digest()); //to HexString
        return strDes;
    }

    // 首先初始化一个字符数组，用来存放每个16进制字符
    private static final char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
            'e', 'f'};

    /**
     * 获得一个字符串的MD5值
     *
     * @param input 输入的字符串
     * @return 输入字符串的MD5值
     */
    public static String md5(String input) {
        if (input == null)
            return null;

        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            // never happen
            throw new RuntimeException("[md5] no such algorithm", e);
        }

        byte[] inputByteArray;
        try {
            inputByteArray = input.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            // never happen
            throw new RuntimeException("[md5] not support utf-8", e);
        }

        messageDigest.update(inputByteArray);
        byte[] resultByteArray = messageDigest.digest();

        // 字符数组转换成字符串返回
        return byteArrayToHex(resultByteArray);

    }

    /**
     * 获取文件的MD5值
     *
     * @param file
     * @return
     */
    public static String md5(File file) throws NoSuchAlgorithmException, IOException {

        if (!file.isFile()) {
            System.err.println("文件" + file.getAbsolutePath() + "不存在或者不是文件");
            return null;
        }

        FileInputStream in = new FileInputStream(file);

        String result = md5(in);

        in.close();

        return result;

    }

    public static String md5(InputStream in) throws NoSuchAlgorithmException, IOException {

        MessageDigest messagedigest = MessageDigest.getInstance("MD5");

        byte[] buffer = new byte[1024];
        int read = 0;
        while ((read = in.read(buffer)) != -1) {
            messagedigest.update(buffer, 0, read);
        }

        in.close();

        String result = byteArrayToHex(messagedigest.digest());

        return result;
    }

    private static String byteArrayToHex(byte[] byteArray) {
        // new一个字符数组，这个就是用来组成结果字符串的（解释一下：一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方））
        char[] resultCharArray = new char[byteArray.length * 2];
        // 遍历字节数组，通过位运算（位运算效率高），转换成字符放到字符数组中去
        int index = 0;
        for (byte b : byteArray) {
            resultCharArray[index++] = hexDigits[b >>> 4 & 0xf];
            resultCharArray[index++] = hexDigits[b & 0xf];
        }

        // 字符数组组合成字符串返回
        return new String(resultCharArray);

    }

    private static String bytes2Hex(byte[] bts) {
        String des = "";
        String tmp = null;
        for (int i = 0; i < bts.length; i++) {
            tmp = (Integer.toHexString(bts[i] & 0xFF));
            if (tmp.length() == 1) {
                des += "0";
            }
            des += tmp;
        }
        return des;
    }
}
