package com.redishash.aop;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.redishash.annotation.RedisHDel;
import com.redishash.annotation.RedisHGet;
import com.redishash.annotation.RedisHPut;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Aspect
public class RedisHashAspect {
	
	//public Map<String,Object> cacheMap = Maps.newConcurrentMap();
	
	/*@Resource
	RedisTemplate redisTemplate;
	*/
	@Resource
	IHashCache cacher;
	
	@Pointcut("@annotation(com.redishash.annotation.RedisHGet)")
	public void cutpointHGet() {
		
	}
	@Pointcut("@annotation(com.redishash.annotation.RedisHPut)")
	public void cutpointHPut() {
		
	}
	@Pointcut("@annotation(com.redishash.annotation.RedisHDel)")
	public void cutpointHDel() {
		
	}
	
	@Around("cutpointHGet()")
	public Object cache(ProceedingJoinPoint point) {
        try {
       
            Method method = ((MethodSignature) point.getSignature()).getMethod();
            // 获取RedisCache注解
            RedisHGet cache = method.getAnnotation( RedisHGet.class);
            if (cache != null && cache.read()) {
            	
                // 查询操作
                String key = cache.key();
                String hashKey = cache.hashKey(); //parseKey(cache.hashKey(), method, point.getArgs());
                Object obj = cacher.locateObject(key, hashKey);
                if (obj == null) {
                	log.info("未找到该对象，执行方法: {}",method.getName());
                    obj = point.proceed(point.getArgs());
                    if (obj != null) {
                    	log.info("执行方法:{} 完成, 写入缓存key:{},hashKey:{},val:{}",
                    			method.getName(),key,hashKey,obj);
                    	cacher.putObject(key,hashKey,obj);
                    	
         
                    }
                }
                return obj;
            }
        } catch (Throwable ex) {
            log.error("<====== RedisHashCache 执行异常: {} ======>", ex);
        }
        return null;
    }
	
	@After("cutpointHPut()")
	public void update(JoinPoint point) {
		Method method = ((MethodSignature) point.getSignature()).getMethod();
		RedisHPut cache = method.getAnnotation(RedisHPut.class);
		String key = cache.key();
		String hashKey = cache.hashKey(); //parseKey(evict.hashKey(), method, point.getArgs());
		cacher.putObject(key,hashKey,point.getArgs()[0]);
		// baseHashRedisTemplate.remove(key, hashKey);
	}

	@After("cutpointHDel()")
	public void evict(JoinPoint point) {
		log.info("cutpointHDel :{}",point);
		
		Method method = ((MethodSignature) point.getSignature()).getMethod();
		RedisHDel cache = method.getAnnotation(RedisHDel.class);
		String key = cache.key();
		String hashKey = cache.hashKey(); //parseKey(evict.hashKey(), method, point.getArgs());
		cacher.evictObject(key,hashKey);
		// baseHashRedisTemplate.remove(key, hashKey);
	}





}
