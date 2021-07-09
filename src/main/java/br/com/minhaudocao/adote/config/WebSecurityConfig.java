package br.com.minhaudocao.adote.config;

import br.com.minhaudocao.adote.filter.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    DataSource dataSource;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource);
    }

    @Bean
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return super.userDetailsServiceBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }



    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable().authorizeRequests()
                .antMatchers("/api/deleteFoto").hasAnyRole("USER", "INSTITUICAO", "ADMIN")
                .antMatchers("/api/endereco/add").permitAll()
                .antMatchers("/api/evento/{id}").permitAll()
                .antMatchers("/api/evento/add").hasAnyRole("INSTITUICAO", "ADMIN")
                .antMatchers("/api/evento/all").permitAll()
                .antMatchers("/api/formulario/{id}").hasAnyRole("USER", "INSTITUICAO", "ADMIN")
                .antMatchers("/api/formulario/add").hasAnyRole("INSTITUICAO", "ADMIN")
                .antMatchers("/api/formulario/all").hasAnyRole("USER", "INSTITUICAO", "ADMIN")
                .antMatchers("/api/instituicao/{id}").permitAll()
                .antMatchers("/api/instituicao/add").permitAll()
                .antMatchers("/api/instituicao/all").permitAll()
                .antMatchers("/api/pessoa/{id}").permitAll()
                .antMatchers("/api/pessoa/add").permitAll()
                .antMatchers("/api/pessoa/all").hasRole("ADMIN")
                .antMatchers("/api/pet/{id}").permitAll()
                .antMatchers("/api/pet/add").hasAnyRole("INSTITUICAO", "ADMIN")
                .antMatchers("/api/pet/all").permitAll()
                .antMatchers("/api/authenticate").permitAll()
                .antMatchers("/api/uploadFoto").hasAnyRole("USER", "INSTITUICAO", "ADMIN")
                .anyRequest().hasRole("ADMIN")
//                .anyRequest().permitAll()
                .and().exceptionHandling().and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

}
