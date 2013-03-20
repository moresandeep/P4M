package com.sandeepmore.pool;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.pool.impl.GenericObjectPool;

import java.util.ResourceBundle;

/**
 * CashingPool is a singelton class which creates the connection pools
 * The connections objects are of the type net.spy.memcached.MemcachedClient
 * The responsibility of this class is to create {@link #MAXACTIVE} number of connection pools 
 * <p>
 * @author Sandeep More
 *
 */
public class CachingPool implements ICachingPool {

	private static final Log logger = LogFactory.getLog(CachingPool.class);
	/**
	 * Specifies number of objects in the pool 
	 */
	private static int MAXACTIVE; 	// Sets the cap on the number of objects that can be allocated by the pool.
	/**
	 * Specifies the max time in seconds the {@link com.sandeepmore.pool.ICachingPool#borrowObject()} should block before quitting.
	 */
	private static int MAXWAIT; 	// Max time the borrowedObject() should block b4 throwing an exception
    /**
     * Specifies the action to be taken when the pool is exhausted.  Valid values are:
     * GenericObjectPool.WHEN_EXHAUSTED_BLOCK, GenericObjectPool.WHEN_EXHAUSTED_GROW, GenericObjectPool.WHEN_EXHAUSTED_FAIL
     */
    private static byte EXHAUSTED_ACTION = GenericObjectPool.WHEN_EXHAUSTED_FAIL;
	
	private static ICachingPool instance = null; // sessions class instance
	private GenericObjectPool<Object> cachePool;
    private final CacheObjectFactory cof;

    /**
     * Initialize the  Cashing Connection pool.
     * sets the {@link #MAXACTIVE} and {@link #MAXWAIT} property for the connections from the properties file.
     * @param cof Connection settings for caching bucket.
     */
    private CachingPool(CacheObjectFactory cof) {
        this(cof, Integer.parseInt(ResourceBundle.getBundle("com.sandeepmore.pooling").getString("memcached.poolObjects")),
                Integer.parseInt(ResourceBundle.getBundle("com.sandeepmore.pooling").getString("pool.MAXWAIT")),
                ResourceBundle.getBundle("com.sandeepmore.pooling").getString("pool.exhaustedAction"));
    }

    /**
     * Initialize the  Cashing Connection pool.
     * sets the {@link #MAXACTIVE} and {@link #MAXWAIT} property for the connections ignoring the settings in the pooling.properties file.
     * @param cof Connection settings for caching bucket.
     * @param maxactive Specifies number of objects in the pool
     * @param maxwait Specifies the max time in seconds the {@link com.sandeepmore.pool.ICachingPool#borrowObject()} should block before quitting.
     * @param exhaustedAction Specifies the action to be taken when the pool is exhausted.  Valid values are: "block", "grow", "fail"
     */
    private CachingPool(CacheObjectFactory cof, int maxactive, int maxwait, String exhaustedAction) {
        this.cof = cof;
        logger.info("instantiating the Caching pool for server " + cof.server + " maxactive=" + maxactive +
                " maxwait=" + maxwait + ". When exhausted this pool will " + exhaustedAction + ".");

        // create a genericObjectPool using CacheObjectFactory
        // CacheObjectFactory has all the setting for the getting the cache
        cachePool = new GenericObjectPool<Object>(cof);

        // Parse out the exhausted action
        if (exhaustedAction.trim().toLowerCase().equals("block")) {
            EXHAUSTED_ACTION = GenericObjectPool.WHEN_EXHAUSTED_BLOCK;
        }
        if (exhaustedAction.trim().toLowerCase().equals("grow")) {
            EXHAUSTED_ACTION = GenericObjectPool.WHEN_EXHAUSTED_GROW;
        }
        if (exhaustedAction.trim().toLowerCase().equals("fail")) {
            EXHAUSTED_ACTION = GenericObjectPool.WHEN_EXHAUSTED_FAIL;
        }

        MAXACTIVE =  maxactive;
        MAXWAIT   =  maxwait;

        // initialize the pool properties
        cachePool.setMaxActive(MAXACTIVE);
        cachePool.setMaxWait(MAXWAIT);
        cachePool.setWhenExhaustedAction(EXHAUSTED_ACTION); // Fail when the pool is empty
    }


    /**
     * Returns the instance of a singelton CashingPool class
     * <p>
     * @return CashingPool instance
     */
    public static synchronized ICachingPool getInstance() {

        // return the instance
        if (instance == null) {
            logger.debug("Creating a new singelton instance");
            instance = new CachingPool(new CacheObjectFactory());
        }
        return instance;

    }

    /**
     * Sets the singleton instance of CashingPool class using params rather than the properties file. This should only be called
     * once and it should be called before any calls to @link com.dandeepmore.pool.CachingPool#getInstance()
     * <p>
     * @param server Address of the memcached server including port number (e.g. memcached.something.cfg.use1.cache.amazonaws.com:11211)
     * @param maxactive Specifies number of objects in the pool
     * @param maxwait Specifies the max time in seconds the {@link com.sandeepmore.pool.ICachingPool#borrowObject()} should block before quitting.
     * @param exhaustedAction Specifies the action to be taken when the pool is exhausted.  Valid values are: "block", "grow", "fail"
     */
    public static ICachingPool init(String server, int maxactive, int maxwait, String exhaustedAction) {

        // return the instance
        if (instance == null) {
            logger.info("Creating a new singelton instance with server=" + server + " maxactive=" + maxactive + " maxwait=" + maxwait);
            instance = new CachingPool(new CacheObjectFactory(server), maxactive, maxwait, exhaustedAction);
        }
        return instance;
    }

    /**
	 * Obtains an instance from the pool.
	 * <p>
	 * @throws Exception 
	 */
	public Object borrowObject() throws Exception {
        Object obj = null;
		try {
			obj = cachePool.borrowObject();
            return obj;
		} catch(Exception e) {
            if (obj != null) {
                try {
                    cachePool.invalidateObject(obj);
                }
                catch (Exception e2) {
                    logger.warn("Error invalidating a memcache object after an exception", e2);
                }
            }
			throw e;
		}
	}

	/** 
	 * Return an instance to the pool.
     * <p>
	 * @param obj The object to return to the pool.
	 */
	public void returnObject(Object obj) {
		try {
            if (obj != null) {
			    cachePool.returnObject(obj);
            }
		} catch(Exception e) {
			logger.error("Error returning a memcache object to the pool", e);
		}		
	}

    /**
     * Return the server associated with this cache pool
     * @return The server string associated with the ConnectionObjectFactory
     */
    public String getServer() {
        return cof.server;
    }
	
	/**
	 * Just in case anyone tries to create a new instance ... stop them.
	 */
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
	
	
}
