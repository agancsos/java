package com.rest2.services;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import com.rest2.models.Rest2Repository;
import com.rest2.models.Rest2Item;
import com.rest2.models.BasicItem;

@Configuration
class LoadDatabase {

	public LoadDatabase() {

	}

	@Bean
	@Autowired
	CommandLineRunner initDatabase() {
		Rest2Repository repository = new Rest2Repository();
		return args -> {
			repository.save(new BasicItem("{\"fullName\":\"Test1\"}"));
	  		repository.save(new BasicItem("{\"fullName\":\"Test2\"}"));
		};
  	}

	public static void initDatabase2(Rest2Repository repository) {
		repository.save(new BasicItem("{\"fullName\":\"Test1\"}"));
		repository.save(new BasicItem("{\"fullName\":\"Test2\"}"));
	} 
}
