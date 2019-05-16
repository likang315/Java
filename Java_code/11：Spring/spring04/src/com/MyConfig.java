package com;

import com.sp01.Student;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 用这个类来初始化spring容器, 新加的一种方法
 *  * @author Administrator
 *
 */

@Configuration
@ComponentScan(basePackageClasses=MyConfig.class)
public class MyConfig {

	@Bean
	public Student newcellphone()
	{
		return new Student("lisi",23);
	}
}
