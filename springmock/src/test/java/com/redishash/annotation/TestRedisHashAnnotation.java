package com.redishash.annotation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

import com.redishash.aop.DummyHashCache;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest(classes=com.testmock.springmock.SpringmockApplication.class)
public class TestRedisHashAnnotation {
	@Autowired
	InnerRedis inner;
	
/*	@MockBean
	IHashCache hashCache;*/
	
	@Autowired
	DummyHashCache dummyCache;
	
	
	@Component
	public static class InnerRedis{
		
		@RedisHGet(hashKey = "a", key = "a")
		public String findByKey(String key) {
			return key+"_" + System.currentTimeMillis();
		}
		
		@RedisHPut(hashKey = "b", key = "a")
		public void updateBykey(String key, String value) {
			
		}
		
		@RedisHDel(hashKey = "n", key = "a")
		public void delByKey(String key) {
			
		}
		
	}
/*
	@Test
	void testHGet() throws NoSuchMethodException, SecurityException {
		
		assertNotNull(inner);
		Method methodGet = inner.getClass().getMethod("findByKey", String.class);
		assertNotNull(methodGet);
		Annotation[] annotations = methodGet.getAnnotations();
		
		assertNotNull(annotations);
		RedisHGet annoHGet = methodGet.getAnnotation(RedisHGet.class);
		assertNotNull(annoHGet);
		
	}
	@Test
	void testHPut() throws NoSuchMethodException, SecurityException {
		
		assertNotNull(inner);
		Method methodPut = inner.getClass().getMethod("updateBykey", String.class, String.class);
		assertNotNull(methodPut);
		Annotation[] annotations = methodPut.getAnnotations();
		
		assertNotNull(annotations);
		RedisHPut annoHPut = methodPut.getAnnotation(RedisHPut.class);
		assertNotNull(annoHPut);
		
	}
	@Test
	void testHDel() throws NoSuchMethodException, SecurityException {
		
		assertNotNull(inner);		
		Method methodDel = inner.getClass().getMethod("delByKey", String.class);
		assertNotNull(methodDel);
		Annotation[] annotations = methodDel.getAnnotations();
		
		assertNotNull(annotations);
		assertTrue(annotations.length > 0 );
		
		log.info("annotations : {}",annotations.length);
		
		RedisHDel annoHDel = methodDel.getAnnotation(RedisHDel.class);
		assertNotNull(annoHDel);
		
		
	}*/
	@Test
	void testHDelInvoke() {
		
		assertNotNull(inner);
		//.evictObject("a", "niu")
		//when(hashCache.evictObject("a", "niu")).thenThrow(new Exception("Hello"));
		//Mockito.doNothing().when(hashCache).evictObject("a", "niu");
		
		inner.delByKey("a");
		
	
		
		
	}
	@Test
	void testHGelInvoke() throws InterruptedException {
		
		assertNotNull(inner);
		//.evictObject("a", "niu")
		//when(hashCache.evictObject("a", "niu")).thenThrow(new Exception("Hello"));
		//Mockito.doNothing().when(hashCache).evictObject("a", "niu");
		
		String value = inner.findByKey("abc");
		log.info("key: abc, value:{}",value);
		assertNotNull(value);
		Thread.sleep(2000);
		
		String val = inner.findByKey("abc");
		assertNotNull(val);
		log.info("key: abc, value:{}",val);
		assertEquals(value,val);
	
		
		
	}
	@Test
	void testHPutInvoke() {
		
		assertNotNull(inner);
		//.evictObject("a", "niu")
		//when(hashCache.evictObject("a", "niu")).thenThrow(new Exception("Hello"));
		//Mockito.doNothing().when(hashCache).evictObject("a", "niu");
		
		inner.updateBykey("a", "xxx");
		
	}

}
