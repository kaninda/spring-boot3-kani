package com.kani.springboot3kani;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@SpringBootApplication
public class SpringBoot3KaniApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBoot3KaniApplication.class, args);
	}

	@Bean
	ApplicationListener<ApplicationReadyEvent> applicationReadyEventApplicationListener (PersonService personService){
		return event -> personService.getAll().forEach(System.out::println);
	}
}

@Service
class PersonService {
	private final JdbcTemplate jdbcTemplate;
	private final RowMapper<Person> rowMapper =
			(rs, rowNum) -> new Person(rs.getInt("id"), rs.getString("name"));

	PersonService(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	Collection<Person> getAll() {
		return jdbcTemplate.query("select * from persons", rowMapper);
	}

	Person getById (Integer id){
		return jdbcTemplate.queryForObject("select * from persons where id=?", rowMapper, id);
	}

}

record Person (Integer id, String name){

}
