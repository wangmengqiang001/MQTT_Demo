package com.redishash.annotation.aop;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.redishash.annotation.RedisHDel;
import com.redishash.annotation.RedisHGet;
import com.redishash.annotation.RedisHPut;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest(classes=com.testmock.springmock.SpringmockApplication.class)
class RedisHashCacheTest {

	@Autowired
	InnerRedis inner;
	
	@Builder
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class InnderData{
		private String deviceId;
		private String metrics;
		private int value;
		private List<String> props;
	}
	@Component
	static class InnerRedis{
		
		@RedisHGet(hashKey = "#deviceId", cache = "energy:iot:object_model:info_example")
		public InnderData findByKey(String deviceId) {
			InnderData data = InnderData.builder().deviceId(deviceId)
					.metrics(deviceId+"_" + System.currentTimeMillis()).value(100).build();
			return data;
		}
		
		@RedisHPut(hashKey = "#innerData.deviceId", cache = "energy:iot:object_model:info_example")
		public void updateBykey(InnderData innerData) {
			
			innerData.setMetrics(innerData.getDeviceId()+"_" + System.currentTimeMillis());
			
		}
		
		@RedisHDel(hashKey = "#innerData.deviceId", cache = "energy:iot:object_model:info_example")
		public void delByKey(InnderData innerData) {
			
		}
		
	}
	
	@Test
	void testPutObject() {
		String[] listProps = {"directon","speed","shape"};
		List<String> pros = Lists.newArrayList(listProps);
		InnderData data = InnderData.builder().deviceId("abc")
				.metrics("abcd"+"_" + System.currentTimeMillis()).value(200)
				.props(Lists.newArrayList(listProps)).build();
		
		inner.updateBykey(data);
	}

	@Test
	void testLocateObject() throws InterruptedException {
	assertNotNull(inner);
	
		
		InnderData value = inner.findByKey("abc");
		log.info("key: abc, value:{}",value);
		assertNotNull(value);
		log.info("sleeping....");
		Thread.sleep(2000);
		log.info("wakeup");
		
		InnderData val = inner.findByKey("abc");
		assertNotNull(val);
		log.info("key: abc, value:{}",val);
		assertEquals(value,val);
	}

	@Test
	void testEvictObject() {
		//fail("Not yet implemented");
		InnderData data = InnderData.builder().deviceId("abc").build();
		inner.delByKey(data);
	}
	
	
	

}
