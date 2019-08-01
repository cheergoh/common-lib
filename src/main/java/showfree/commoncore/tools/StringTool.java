package showfree.commoncore.tools;

import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * 字符串工具
 * @author
 *
 */
public final class StringTool {

    /**
     * 将二进制数据转换成16进制字符串
     * @param buff
     * @return
     */
    public static String byteToHexString(byte[] buff) {
        if(buff == null || buff.length == 0) {
            return "";
        }

        StringBuffer sb = new StringBuffer();
        String hex = null;
        for(int i = 0; i < buff.length; i++) {
            hex = Integer.toHexString(buff[i] & 0xFF);
            if(hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 向左补位字符串
     * @param str
     * @param size
     * @param placeholder
     * @return
     */
    public static String fillLeft(String str, int size, char placeholder) {
        if(str == null) {
            str = "";
        }

        if(str.length() < size) {
            size = size - str.length();
            if(size == 1) {
                str = placeholder + str;
            }else {
                StringBuffer sb = new StringBuffer();
                for(int i = 0; i < size; i++) {
                    sb.append(placeholder);
                }
                str = sb.toString() + str;
            }
        }

        return str;
    }

    /**
     * 向右补位字符串
     * @param str
     * @param size
     * @param placeholder
     * @return
     */
    public static String fillRight(String str, int size, char placeholder) {
        if(str == null) {
            str = "";
        }

        if(str.length() < size) {
            size = size - str.length();
            if(size == 1) {
                str += placeholder;
            }else {
                StringBuffer sb = new StringBuffer();
                for(int i = 0; i < size; i++) {
                    sb.append(placeholder);
                }
                str += sb.toString();
            }
        }

        return str;
    }

    /**
     * 将16进制字符串转换成二进制数据
     * @param hex
     * @return
     */
    public static byte[] hexStringToBytes(String hex) {
        if(hex == null || hex.isEmpty()) {
            return null;
        }

        byte[] buff = new byte[hex.length() / 2];
        int high, low = 0;
        for(int i = 0; i < buff.length; i++) {
            high = Integer.parseInt(hex.substring(i*2, i*2+1), 16);
            low = Integer.parseInt(hex.substring(i*2+1, i*2+2), 16);
            buff[i] = (byte)(high * 16 + low);
        }
        return buff;
    }

    /**
     * 判断指定字符串是否为空（null或0长度）
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    /**
     * 对指定字符串进行md5加密
     * @param str
     * @return
     */
    public static String md5(String str) {
        if(isEmpty(str)) {
            return "";
        }

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            return new BigInteger(1, md.digest()).toString(16);
        }catch(Exception ex) {
            return "";
        }
    }

    /**
     * 将下划线命名字符串转换成驼峰命名，比如: shop_code -> shopCode
     * @param name
     * @return
     */
    public static String toHumpName(String name) {
        if(StringTool.isEmpty(name)) {
            return name;
        }

        int index = 0;
        while((index = name.indexOf("_")) >= 0 && index < name.length() - 1) {
            name = name.substring(0, index) + name.substring(index + 1, index + 2).toUpperCase() + name.substring(index + 2);
        }

        return name;
    }

    private StringTool() {}
}

