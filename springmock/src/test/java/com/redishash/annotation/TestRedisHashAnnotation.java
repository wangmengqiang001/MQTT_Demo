package com.redishash.annotation;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.stereotype.Component;

import com.redishash.aop.IHashCache;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest(classes=com.testmock.springmock.SpringmockApplication.class)
 class TestRedisHashAnnotation {
	@Autowired
	InnerRedis inner;
	@MockBean
	IHashCache hashCache;
	
	@Component
	static class InnerRedis{
		
		@RedisHGet(hashKey = "", key = "")
		public String findByKey(String key) {
			return key+"_" + System.currentTimeMillis();
		}
		
		@RedisHPut(hashKey = "", key = "")
		public void updateBykey(String key, String value) {
			
		}
		
		@RedisHDel(hashKey = "", key = "")
		public void delByKey(String key) {
			
		}
		
	}

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
		log.info("annotations : {}",annotations.toString());
		
		RedisHDel annoHDel = methodDel.getAnnotation(RedisHDel.class);
		assertNotNull(annoHDel);
		
	}

}
