package com.sandeepmore.caching;

import com.sandeepmore.pool.CachingPool;
import com.sandeepmore.pool.ICachingPool;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.OperationTimeoutException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.NoSuchElementException;

/**
 * API Used to Set, Get and Delete values from the cache pool.
 * feel free to add other methods if you wish to.
 * Throws a {@link CachingException} if the Get and Delete operations fails.
 * <p/>
 *
 * @author Sandeep More
 *         <p> Example usage </p>
 *         <pre>
 *                 {@code
 *                  	private CachingBroker cb = new CachingBroker();
 *
 *                  	cb.set(key ,TTL, value); // set
 *                  	cb.get(key);             // get
 *                  	cb.delete(key);	         // delete
 *                 }
 *                 </pre>
 */
public class CachingBroker implements ICachingBroker {

    private static final Log logger = LogFactory.getLog(CachingBroker.class);
    private ICachingPool deligatePool = null;

    public CachingBroker() {
        this.deligatePool = CachingPool.getInstance(); // get the session instance
        if (this.deligatePool == null) {
            logger.error("Retured a null instance for session caching pool");
        }
    }

    public CachingBroker(final ICachingPool deligatePool) {
        this.deligatePool = deligatePool;
    }

    /**
     * Set an object in the cache regardless of any existing value.
     * If the set fails exception will be logged silently.
     * <p>
     * @param key The key under which this object should be stored.
     * @param exp Time To Live value for the object in seconds.
     * @param obj Object value to be stored in the cache.
     */
    public void set(String key, int exp, Object obj) {
        MemcachedClient client = null;
        try {
            // borrow an object from the pool to work on
            logger.debug("SET:Borrowing object from the pool");
            client = (MemcachedClient) deligatePool.borrowObject();
            //do the set operation
            client.set(key, exp, obj);
        } catch (NoSuchElementException ex) {
            // The pool is full
            logger.error("Session pool full");
            ex.printStackTrace();
        } catch (Exception ex) {
            logger.error("Set session cache failed for key:" + key);
            ex.printStackTrace();
        } finally {
            //return the borrowed object back to pool
            deligatePool.returnObject(client);
            logger.debug("SET:Returned object to the pool");
        }
    }

    /**
     * Get the object from the cache with a single key.
     * If the key is not found a null value is returned.
     * <p>
     * @param key Key of the object to be retrieved from the cache.
     * @throws CachingException
     * @return The result from the cache (null if there is none).
     */
    public Object get(String key) throws CachingException {
        MemcachedClient client = null;
        Object getValue = null;
        try {
            // borrow an object from the pool to work on
            logger.debug("GET:Borrowing object from the pool");
            client = (MemcachedClient) deligatePool.borrowObject();
            //do the get operation
            getValue = client.get(key);
            return getValue;
        } catch (NoSuchElementException ex) {
            // The pool is full, return null.
            logger.warn("Session pool full, get for key:" + key + " returning null ");
            return null;
        } catch (OperationTimeoutException ex) {
            logger.error("Timed out while waiting for the memcache connection to: " + deligatePool.getServer());
            throw new CachingException(ex);
        } catch (Exception ex) {
            logger.error("Get for key:" + key + " caused an unplanned exception ", ex);
            throw new CachingException(ex);
        } finally {
            //return the borrowed object back to pool
            deligatePool.returnObject(client);
            logger.debug("GET:Returned object to the pool");
        }

    }

    /**
     * Delete the given key from the session cache.
     * <p>
     * @param key Key of the object to be deleted from the cache.
     * @throws CachingException
     */
    public void delete(String key) throws CachingException {
        MemcachedClient client = null;
        try {
            // borrow an object from the pool to work on
            logger.debug("DELETE:Borrowing object from the pool");
            client = (MemcachedClient) deligatePool.borrowObject();
            //do the delete operation
            client.delete(key);
        } catch (NoSuchElementException ex) {
            // The pool is full
            logger.error("Session pool full, delete for key:" + key + " unsuccessful");
            ex.printStackTrace();
        } catch (Exception ex) {
            // The pool is full
            logger.error("Delete for key:" + key + " unsuccessful");
            ex.printStackTrace();
            throw new CachingException(ex);
        } finally {
            //return the borrowed object back to pool
            deligatePool.returnObject(client);
            logger.debug("DELETE:Returned object to the pool");
        }
    }

    /**
     * Return the server string associated with this Caching Broker.
     * @return The string used to initialize the memcache server.
     */
    public String getServer() {
        return deligatePool.getServer();
    }
}
