package com.redishash.aop;

public interface IHashCache {
	void putObject(String key, String hashKey, Object obj) ;
	Object locateObject(String key, String hashKey);
	void evictObject(String key,String hashKey);

}
