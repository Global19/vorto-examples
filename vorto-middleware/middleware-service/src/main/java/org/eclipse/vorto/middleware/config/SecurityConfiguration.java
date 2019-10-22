package org.eclipse.vorto.middleware.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Value("${cors}")
	private String crossOrigin;

	@Value("${admin.password}")
	private String adminPassword = "";

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.httpBasic().and().authorizeRequests()
				.antMatchers(HttpMethod.GET, "/api/1/**", "/endpoint/**").permitAll()
				.antMatchers(HttpMethod.PUT, "/api/1/**").authenticated().and().csrf().disable();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser("admin").password(adminPassword).roles("ADMIN");
	}

	@Bean
 	public FilterRegistrationBean corsFilter() {
 		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
 		CorsConfiguration config = new CorsConfiguration();
 		config.setAllowCredentials(true);
 		config.addAllowedOrigin(crossOrigin);
 		config.addAllowedHeader("*");
 		config.addAllowedMethod("*");
 		source.registerCorsConfiguration("/**", config);
 		FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
 		bean.setOrder(0);
 		return bean;
 	}
}
