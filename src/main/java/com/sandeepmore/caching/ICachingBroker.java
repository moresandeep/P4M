package com.sandeepmore.caching;

/**
 * ICachingBroker defines a simple caching interface. The required methods are
 * {@link #set(String, int, Object)}, {@link #get(String)}, {@link #delete(String)}
 * Add methods if you wish to.
 * <br><b>Creation date:</b> April 4, 2011 10:28 AM<br>
 * @author Sandeep More</dl>
 */
public interface ICachingBroker {
	
	// Constants for cache Time To Live
	public static final int MINS_15 = 900;
	public static final int MINS_30 = 1800;
	public static final int HOURS_1 = 3600;
	public static final int HOURS_2 = 7200;
	public static final int HOURS_4 = 14400;
	public static final int HOURS_12 = 43200;
	public static final int DAYS_1 = 86400;
	public static final int DAYS_7 = 604800;
	
	/**
	 * Set an object in the cache  
	 * regardless of any existing value. New value overwrites the old value
	 * <p>
	 * @param key key used to lookup
	 * @param exp Time to live in seconds
	 * @param obj Object associated with the key
	 */
	public void set (String key, int exp, Object obj);
	
	/**
	 * Get with a single key.
	 * @param key key used to lookup
	 */
	public Object get (String key) throws CachingException;
	
	/**
	 * Delete the given key from the cache.
	 * @param key key used to lookup
	 * @throws CachingException
	 */
	public void delete (String key) throws CachingException;

    /**
     * Return the server string associated with this Caching Broker.
     * @return The string used to initialize the memcache server.
     */
    public String getServer();
}