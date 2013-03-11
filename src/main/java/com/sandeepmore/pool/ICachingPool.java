package com.sandeepmore.pool;


public interface ICachingPool {
	
	// Borrows an object from the pool.
	public Object borrowObject();
	
	// Returns an object instance to the pool.
	public void returnObject(Object obj);

}
