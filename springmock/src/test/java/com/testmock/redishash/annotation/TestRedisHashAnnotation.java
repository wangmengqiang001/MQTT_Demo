package com.testmock.redishash.annotation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;
import org.redishash.annotation.EnableRedisHashCache;
import org.redishash.annotation.RedisHDel;
import org.redishash.annotation.RedisHGet;
import org.redishash.annotation.RedisHPut;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest(classes=com.testmock.springmock.SpringmockApplication.class)
@Profile("test")
@EnableRedisHashCache
public class TestRedisHashAnnotation {
	@Autowired
	InnerRedis inner;

	@Component
	static class InnerRedis{
		
		@RedisHGet(hashKey = "#key", cache = "a")
		public String findByKey(String key) {
			return key+"_" + System.currentTimeMillis();
		}
		
		@RedisHPut(hashKey = "#key", cache = "a")
		public void updateBykey(String key, String value) {
			
		}
		
		@RedisHDel(hashKey = "#key", cache = "a")
		public void delByKey(String key) {
			
		}
		
	}

	@Test
	void testHGet() throws NoSuchMethodException, SecurityException {

		assertNotNull(inner);
	
		//if(AopUtils.isAopProxy(inner)) { //在添加ASpect中的 @After,@Around后不能直接取得方法的注解(注入的是proxy)
		assertTrue(AopUtils.isAopProxy(inner),"不是AOP Proxy");
		Method methodGet = inner.getClass().getMethod("findByKey", String.class);
		assertTrue(methodGet.toGenericString().contains("$$EnhancerBySpringCGLIB$$"));
		assertTrue( AopUtils.isCglibProxy(inner));
		
		Class<?> classTarget =AopUtils.getTargetClass((InnerRedis)inner);
		Method methodTarget = classTarget.getMethod("findByKey", String.class);
		
		//
		assertAnnotation(methodTarget, RedisHGet.class);
		log.info("---is AOP Proxy------");
		

	}


	private <T extends Annotation> void  assertAnnotation(Method methodTarget, Class<T> clsType) {
		assertNotNull(methodTarget);
		Annotation[] annotations = methodTarget.getAnnotations();
		
		StringBuilder sb = new StringBuilder();
		
		assertNotNull(annotations);
		
		for (Annotation annotation : annotations) {
			sb.append(annotation.toString());
			sb.append("\n");
		}
		log.info("\nmethod[{}] with anntations:[{}]",methodTarget.getName(),sb);
	

		assertTrue(annotations.length > 0 );
				
		T annoHSet =  methodTarget.getAnnotation( clsType);
		assertNotNull(annoHSet);
		log.info("found anntation:[{}]",annoHSet);
	}
	
	
	@Test
	void testHPut() throws NoSuchMethodException, SecurityException {

		assertNotNull(inner);

		//if(AopUtils.isAopProxy(inner)) { //在添加ASpect中的 @After,@Around后不能直接取得方法的注解(注入的是proxy)

		assertTrue(AopUtils.isAopProxy(inner),"不是AOP Proxy");
		
		Method methodPut = inner.getClass().getMethod("updateBykey", String.class, String.class);
		assertNotNull(methodPut);
		assertTrue(methodPut.toGenericString().contains("$$EnhancerBySpringCGLIB$$"));
		assertTrue( AopUtils.isCglibProxy(inner));

		Class<?> classTarget =AopUtils.getTargetClass((InnerRedis)inner);
		Method methodTarget = classTarget.getMethod("updateBykey", String.class, String.class);

		//
		assertAnnotation(methodTarget, RedisHPut.class);

		


	}
	@Test
	void testHDel() throws NoSuchMethodException, SecurityException {
		
		assertNotNull(inner);
		
		//if(AopUtils.isAopProxy(inner)) { //在添加ASpect中的 @After,@Around后不能直接取得方法的注解(注入的是proxy)

			assertTrue(AopUtils.isAopProxy(inner),"不是AOP Proxy");
			Method methodDel = inner.getClass().getMethod("delByKey", String.class);
			assertNotNull(methodDel);
			assertTrue(methodDel.toGenericString().contains("$$EnhancerBySpringCGLIB$$"));
			assertTrue( AopUtils.isCglibProxy(inner));

			Class<?> classTarget =AopUtils.getTargetClass((InnerRedis)inner);
			Method methodTarget = classTarget.getMethod("delByKey", String.class);

			//
			assertAnnotation(methodTarget, RedisHDel.class);

		
	
		
		
	}
	@Test
	void testHDelInvoke() {
		
		assertNotNull(inner);		
		
		inner.delByKey("abc");

		
	}
	@Test
	void testHGelInvoke() throws InterruptedException {
		
		assertNotNull(inner);
	
		
		String value = inner.findByKey("abc");
		log.info("key: abc, value:{}",value);
		assertNotNull(value);
		log.info("sleeping....");
		Thread.sleep(2000);
		log.info("wakeup");
		
		String val = inner.findByKey("abc");
		assertNotNull(val);
		log.info("key: abc, value:{}",val);
		assertEquals(value,val);
		
		
	}
	@Test
	void testHPutInvoke() {
		
		assertNotNull(inner);

		
		inner.updateBykey("abc", "xxx");
		
	}

}
