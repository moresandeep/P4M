Thanks for downloading Pooling framework for Memcached API.

Things you should know:
1) Import the project in Eclipse
2) Following jars are needed for the project to compile
    - commons-logging.jar 		(http://commons.apache.org/logging/download_logging.cgi)
    - commons-pool-1.5.6.jar 		(http://commons.apache.org/pool/download_pool.cgi)
    - junit.jar			(http://sourceforge.net/projects/junit/)
    - memcached-2.5.jar		(http://code.google.com/p/spymemcached/downloads/list)
3) I have included JUnit test, just in case !
4) The settings to connect to the Membase/Memcache server are in the pooling.properties file (memcached.server)
5) Pool size can be set in the same file (memcached.poolObjects)
6) API currently implements SET,GET and DELETE methods

usage:
	private CachingBroker cb = new CachingBroker();
	
	cb.set("key" ,TTL, "value"); // set
	cb.get("key");             // get
	cb.delete("key");	         // delete
	
Happy Coding
/srm 