//package com.roryharford.user.security;
//
//import javax.sql.DataSource;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
//import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//
//	@Autowired
//	private BCryptPasswordEncoder bCryptPasswordEncoder;
//
//	@Autowired
//	private DataSource datasource;
//
//	private final String USERS_QUERY = "select email, password, active from user where email=?";
//	//look back at this
//	private final String ROLES_QUERY = "select u.email, r.role from user u inner join user_role ur on (u.id = ur.user_id) inner join role r on (ur.role_id = r.role_id) where u.email=?";
//
//	@Override
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.jdbcAuthentication().usersByUsernameQuery(USERS_QUERY).authoritiesByUsernameQuery(ROLES_QUERY)
//				.dataSource(datasource).passwordEncoder(bCryptPasswordEncoder);
//	}
//	
//	 public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
//	        Object userName = event.getAuthentication().getPrincipal();
//	        Object credentials = event.getAuthentication().getCredentials();
//	        System.out.println("Failed login using USERNAME [" + userName + "]");
//	   //     LOG.debug("Failed login using PASSWORD [" + credentials + "]");
//	    }
//
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		http.authorizeRequests()
////		.antMatchers("/css/**").permitAll()
//		.antMatchers("/").permitAll()
//		.antMatchers("/login").permitAll()
//		.antMatchers("/registerPage").permitAll()
//		.antMatchers("/register").permitAll()
//		.antMatchers("/homepage").hasAuthority("USER").anyRequest()
//		.authenticated()
//		.and().csrf().disable()
//		.formLogin().loginPage("/login").failureUrl("/login?error=true")
//		.defaultSuccessUrl("/homepage")
//		.usernameParameter("email")
//		.passwordParameter("password")
//		.and().logout()
//		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//		.logoutSuccessUrl("/")
//		.and().rememberMe()
//		.tokenRepository(persistentTokenRepository())
//		.tokenValiditySeconds(60 * 60)
//		.and().exceptionHandling().accessDeniedPage("/access_denied");
//	}
//
//	@Bean
//	public PersistentTokenRepository persistentTokenRepository() {
//		JdbcTokenRepositoryImpl db = new JdbcTokenRepositoryImpl();
//		db.setDataSource(datasource);
//
//		return db;
//	}
//}