package com.redishash.annotation.aop;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DummyHashCacheTest {
	
	private DummyHashCache hashCache;
	
	@BeforeEach
	void init() {
		hashCache=new DummyHashCache();
	}
	

	@Test
	void testPutObject() {
		//fail("Not yet implemented");
		hashCache.putObject("a", "abc", "object_value_a_abc");
		
		hashCache.putObject("a", "b", "object_value_a_b");
		
		hashCache.putObject("b", "a", "object_value_b_a");
		hashCache.putObject("ab","abc","hello_world");
		
		
	}

	@Test
	void testLocateObject() {
		
		
		
		String val = (String) hashCache.locateObject("a", "a");
		assertNull(val);
		
		
	}

	@Test
	void testEvictObject() {
		hashCache.evictObject("a", "a"); //no empty
		
	}

}
