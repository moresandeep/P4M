package com.sandeepmore.pool;

import java.util.ResourceBundle;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.BinaryConnectionFactory;
import net.spy.memcached.MemcachedClient;

import org.apache.commons.pool.PoolableObjectFactory;

/**
 * A factory for creating {@link MemcachedClient} instances used for caching.
 * <p>
 * @author Sandeep More
 *
 */
public class CacheObjectFactory implements PoolableObjectFactory {
	
	/**
	 * Server configuration string
	 */
	static String server;
	private ResourceBundle props;
	
	/**
	 * Get the server connection from props bundle
	 */
	public CacheObjectFactory() {
		props = ResourceBundle.getBundle("pooling");
		server = props.getString("memcached.server");
	}
	
	@Override
	public void activateObject(Object arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroyObject(Object arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Create a {@link MemcachedClient} that can be served by the pool.
	 * <p>
	 * @return MemcachedClient client object
	 */
	public Object makeObject() throws Exception {
		// TODO Auto-generated method stub
		MemcachedClient cache = new MemcachedClient(new BinaryConnectionFactory(), AddrUtil.getAddresses(server));
		return cache;
	}

	@Override
	public void passivateObject(Object arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean validateObject(Object arg0) {
		// TODO Auto-generated method stub
		return true;
	}

}
