package com.redishash.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface RedisHPut {

	 /**
    * 键名
    */
   String key();

   /**
    * Hash键名（支持Spring EL表达式）
    */
   String hashKey();

   /**
    * 是否为查询操作
    * 如果为写入数据库的操作，该值需置为 false
    */

}
