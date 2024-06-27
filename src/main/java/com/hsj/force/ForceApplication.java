package com.hsj.force;

import com.hsj.force.common.Constants;
import com.hsj.force.domain.entity.TUser;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.web.exchanges.InMemoryHttpExchangeRepository;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@EnableJpaAuditing
@RequiredArgsConstructor
@SpringBootApplication
public class ForceApplication {

	private final HttpSession session;

	public static void main(String[] args) {
		SpringApplication.run(ForceApplication.class, args);
	}

	@Bean
	public InMemoryHttpExchangeRepository httpExchangeRepository() {
		return new InMemoryHttpExchangeRepository();
	}

	@Bean
	public AuditorAware<String> auditorProvider() {
		return new AuditorAware<String>() {
			@Override
			public Optional<String> getCurrentAuditor() {
				TUser loginMember = (TUser) session.getAttribute(Constants.LOGIN_MEMBER);
				if(loginMember == null) return Optional.empty();
        		return Optional.ofNullable(loginMember.getUserId());
			}
		};
	}

}
