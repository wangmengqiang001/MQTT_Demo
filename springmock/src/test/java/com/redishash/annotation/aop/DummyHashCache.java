package com.redishash.annotation.aop;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.redishash.aop.IHashCache;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DummyHashCache implements IHashCache {
	
	private Map<String,Object> mapCache = Maps.newConcurrentMap();

	@Override
	public void putObject(String key, String hashKey, Object obj) {
		// TODO Auto-generated method stub
		log.info("在进行修改后，更新cache中key={},hashKey={}中的值",key,hashKey);
		if(!mapCache.containsKey(key)) {
			Map<String,Object> hkeys = Maps.newConcurrentMap();
			hkeys.put(hashKey, obj);
			
			mapCache.put(key, hkeys);
		}else {
			Map<String,Object> hkeys = (Map<String,Object>)mapCache.get(key);
			hkeys.put(hashKey, obj);
		}
		
		
	}

	@Override
	public Object locateObject(String key, String hashKey) {
		log.info("在执行查询前，查看cache中是否有key={},hashKey={}的对象 ",key,hashKey);
		if(!mapCache.containsKey(key)){
			return null;
		}else {
			Map<String, Object> hkeys = (Map<String, Object>) mapCache.get(key);
			return hkeys.get(hashKey);
		}
		
	}

	@Override
	public void evictObject(String key, String hashKey) {
		log.info("删除 object by key: {}, hashKey: {}",key,hashKey);
		mapCache.computeIfPresent(key, (k,v) -> {
			Map<String,Object> hKeys = (Map<String, Object>) v;
			hKeys.remove(hashKey);
			return hKeys.size() > 0?v:null;});
		
		
	}

}
