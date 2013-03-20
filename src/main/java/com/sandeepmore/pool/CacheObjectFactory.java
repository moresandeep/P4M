package com.sandeepmore.pool;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.BinaryConnectionFactory;
import net.spy.memcached.MemcachedClient;
import org.apache.commons.pool.PoolableObjectFactory;

import java.util.ResourceBundle;

/**
 * A factory for creating {@link MemcachedClient} instances used for caching.
 * <p/>
 *
 * @author Sandeep More
 */
public class CacheObjectFactory implements PoolableObjectFactory<Object> {

    /**
     * Server configuration string
     */
    static String server;

    /**
     * Get the server connection from props bundle
     */
    public CacheObjectFactory() {
        ResourceBundle props = ResourceBundle.getBundle("com.sandeepmore.pooling");
        server = props.getString("memcached.server");
    }

    /**
     * Use the server connection passed in
     *
     * @param server Address of the memcached server including port number (e.g. memcached.something.cfg.use1.cache.amazonaws.com:11211)
     */
    public CacheObjectFactory(String server) {
        CacheObjectFactory.server = server;
    }


    public void activateObject(Object arg0) throws Exception {

    }

    public void destroyObject(Object arg0) throws Exception {

    }

    /**
     * Create a {@link MemcachedClient} that can be served by the pool.
     * <p/>
     *
     * @return MemcachedClient client object
     */
    public Object makeObject() throws Exception {
        MemcachedClient cache = new MemcachedClient(new BinaryConnectionFactory(), AddrUtil.getAddresses(server));
        return cache;
    }

    public void passivateObject(Object arg0) throws Exception {

    }

    public boolean validateObject(Object arg0) {
        return true;
    }

}
