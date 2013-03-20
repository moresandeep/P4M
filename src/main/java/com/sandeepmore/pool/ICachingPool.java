package com.sandeepmore.pool;


public interface ICachingPool {

    /**
     * Obtains an instance from the pool.
     * <p/>
     *
     * @throws Exception
     */
    public Object borrowObject() throws Exception;

    /**
     * Return an instance to the pool.
     * <p/>
     *
     * @param obj The object to return to the pool.
     */
    public void returnObject(Object obj);

    /**
     * Return the server associated with this cache pool
     *
     * @return The server string associated with the ConnectionObjectFactory
     */
    public String getServer();
}