package lk.devstory.devstory;

import lk.devstory.devstory.repository.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = UserRepository.class)
public class DevStoryServicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(DevStoryServicesApplication.class, args);
	}

}
