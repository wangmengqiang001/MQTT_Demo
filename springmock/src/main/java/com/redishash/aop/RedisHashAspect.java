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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
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
	
	
	@Resource
	@Qualifier("redisHashCache")
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
                String cacheName = cache.cache();
                String hashKey = parseKey(cache.hashKey(), method, point.getArgs());
                Object obj = cacher.locateObject(cacheName, hashKey);
                if (obj == null) {
                	log.debug("未找到该对象，执行方法: {}",method.getName());
                    obj = point.proceed(point.getArgs());
                    if (obj != null) {
                    	log.debug("执行方法:{} 完成, 写入缓存cacheName:{},hashKey:{},val:{}",
                    			method.getName(),cacheName,hashKey,obj);
                    	cacher.putObject(cacheName,hashKey,obj);
                    	
         
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
		String cacheName = cache.cache();
		String hashKey = parseKey(cache.hashKey(), method, point.getArgs());
		cacher.putObject(cacheName,hashKey,point.getArgs()[0]);
		// baseHashRedisTemplate.remove(key, hashKey);
	}

	@After("cutpointHDel()")
	public void evict(JoinPoint point) {
		log.info("cutpointHDel :{}",point);
		
		Method method = ((MethodSignature) point.getSignature()).getMethod();
		RedisHDel cache = method.getAnnotation(RedisHDel.class);
		String cacheName = cache.cache();
		String hashKey = parseKey(cache.hashKey(), method, point.getArgs());
		cacher.evictObject(cacheName,hashKey);
		// baseHashRedisTemplate.remove(key, hashKey);
	}



	  /**
     * 获取缓存的key
     * key 定义在注解上，支持SPEL表达式
     *
     * @param key
     * @param method
     * @param args
     * @return
     */
    private String parseKey(String key, Method method, Object[] args) {
        // 获取被拦截方法参数名列表(使用Spring支持类库)
        LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
        String[] paraNameArr = u.getParameterNames(method);
        // 使用SPEL进行key的解析
        ExpressionParser parser = new SpelExpressionParser();
        // SPEL上下文
        StandardEvaluationContext context = new StandardEvaluationContext();
        // 把方法参数放入SPEL上下文中
        for (int i = 0; i < paraNameArr.length; i++) {
            context.setVariable(paraNameArr[i], args[i]);
        }
        return parser.parseExpression(key).getValue(context, String.class);
    }


}
