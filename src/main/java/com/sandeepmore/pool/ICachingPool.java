package com.sandeepmore.pool;


public interface ICachingPool {
	
	// Borrows an object from the pool.
	public Object borrowObject() throws Exception;
	
	// Returns an object instance to the pool.
	public void returnObject(Object obj);

    /**
     * Return the server associated with this cache pool
     * @return The server string associated with the ConnectionObjectFactory
     */
    public String getServer();
}
