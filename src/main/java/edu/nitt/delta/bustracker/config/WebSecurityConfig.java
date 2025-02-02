package edu.nitt.delta.bustracker.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import edu.nitt.delta.bustracker.model.Role;
import edu.nitt.delta.bustracker.service.BusTrackerUserDetailsService;
import edu.nitt.delta.bustracker.utils.JwtTokenUtil;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private BusTrackerUserDetailsService busTrackerUserDetailsService;

    @Autowired
    private PasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private DriverAuthFilter busTrackerFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
            .disable()
            .authorizeRequests()
            .antMatchers(HttpMethod.POST, "/login").permitAll()
            .antMatchers(HttpMethod.GET, "/location").permitAll()
            .antMatchers(HttpMethod.GET, "/vehicle/**").permitAll()
            .antMatchers(HttpMethod.GET, "/driver/**").permitAll()
            .antMatchers(HttpMethod.POST, "/driver").hasRole(Role.ADMIN.toString())
            .antMatchers(HttpMethod.POST, "/vehicle").hasRole(Role.ADMIN.toString())
            .anyRequest().hasAnyRole(Role.ADMIN.toString(), Role.DRIVER.toString());
            
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        
        http.addFilterBefore(
            busTrackerFilter,
            UsernamePasswordAuthenticationFilter.class
        );
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(busTrackerUserDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override 
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public BusTrackerUserDetailsService BusTrackerUserDetailsServiceBean() {
        return new BusTrackerUserDetailsService();
    }

    @Bean
    public JwtTokenUtil jwtTokenUtilBean() {
        return new JwtTokenUtil();
    }
}
