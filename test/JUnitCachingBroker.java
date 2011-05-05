import java.util.HashMap;
import java.util.Map;

import com.sandeepmore.caching.CachingBroker;
import com.sandeepmore.caching.CachingException;

import junit.framework.TestCase;


/**
 * <p>
 * JUnit test for the Caching API using pooling
 * </P>
 * <p>
 * This test will test the following operations of the API
 * <UL>
 * <LI> SET using the provided value
 * <LI> GET
 * <LI> DELETE
 * <LI> Multiple SET, GET and DELETE operations  
 * </P>
 * @author Sandeep More
 *
 */
public class JUnitCachingBroker extends TestCase {
	
	private CachingBroker cb = new CachingBroker();
	
	private String junitKey = "junit";
	private String junitVal = "Memcached using pooling";
	
	// for multiple set
	private Map<String,String> map = new HashMap<String,String>();
	private int numOfValues = 100;
	private int TTL = 1800;
	
	/**
	 * <p>
	 * Setup some of the values used by the test
	 * </p>
	 */
	protected void setUp() {
		for(int i = 0; i < numOfValues ; i++) {
			map.put(junitKey+i, junitVal+i);
		}		
	}
	
	/**
	 * <p>
	 *  Test the SET operation for a single value.
	 * </p>
	 * 
	 */
	public void testSingleSetOperation() {	
		// Set using given TTL value, overwrites previous values
		try {
			cb.set(junitKey,TTL, junitVal);
		}catch(AssertionError er) {
			fail("Failed to SET a single value from cache using given TTL"+TTL);
		}
	}
	
	/**
	 * <p>
	 * Test the GET operation for a single value.
	 * </p>
	 */
	public void testSingleGetOperation() {		
		try {
			assertEquals("Get value from the cache", 
					     junitVal, cb.get(junitKey));
		}catch (CachingException er) {
			fail("Failed to GET a single value, Caching exception");			
		} catch(AssertionError e) {
			fail("Failed to GET a single value from cache");
		}
		
	}
	
	/**
	 * <p>
	 * Test the DELETE operation 
	 * </p>
	 */
	public void testSingleDeleteOperation() {		
		try {
			cb.delete(junitKey);
			assertEquals("Delete value from the cache", 
					     null, cb.get(junitKey));
		}catch (CachingException er) {
			fail("Failed to DELETE value, Caching exception");			
		} catch(AssertionError e) {
			fail("Failed to DELETE value from cache");
		}
		
	}
	
	/**
	 * <p>
	 * Insert 100 values in cache
	 * </p>
	 */
	public void testMultipleSetOperations() {
		// Set using given TTL value
		try {
			for(int i = 0; i < numOfValues; i++ ) {
				cb.set(junitKey+i, TTL, map.get(junitKey+i));
			}
		}catch(AssertionError er) {
			fail("Failed to SET multiple values in cache using given TTL"+TTL);
		}
	}
	
	/**
	 * <p>
	 * Test GET operation for a multiple value.
	 * </p>
	 */
	public void testMultipleGetOperation() {		
		try {
			for(int i = 0; i < numOfValues; i++ ) {
				String tempStr = junitVal + i;
				assertEquals("Get value from the cache", 
						     tempStr, cb.get(junitKey+i));
			}
		}catch (CachingException er) {
			fail("Failed to GET value, Caching exception");			
		} catch(AssertionError e) {
			fail("Failed to GET value from cache");
		}
		
	}
	
	/**
	 * <p>
	 * Test DELETE operation for a multiple value.
	 * </p>
	 */
	public void testMultipleDeleteOperation() {		
		try {
			for(int i = 0; i < numOfValues; i++ ) {
				cb.delete(junitKey+i);
				assertEquals("Get value from the cache", 
						     null, cb.get(junitKey+i));
			}
		}catch (CachingException er) {
			fail("Failed to GET value, Caching exception");			
		} catch(AssertionError e) {
			fail("Failed to GET value from cache");
		}
		
	}
	
}
