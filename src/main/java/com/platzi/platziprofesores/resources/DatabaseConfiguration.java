package com.platzi.platziprofesores.resources;

import java.util.Properties;

import javax.sql.DataSource;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class DatabaseConfiguration {
	
	@Bean
	public LocalSessionFactoryBean sessionFactory() {
		LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
		sessionFactoryBean.setDataSource(dataSource());
		sessionFactoryBean.setPackagesToScan("com.platzi.platziprofesores.model");
		sessionFactoryBean.setHibernateProperties(getProperties());
		return sessionFactoryBean;
	}
	
	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource managerDataSource= new DriverManagerDataSource();
		managerDataSource.setDriverClassName("com.mysql.jdbc.Driver");
		//managerDataSource.setUsername("platziprofesores");
		//managerDataSource.setUrl("jdbc:mysql://localhost:3306/platziprofesores?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC");
		//managerDataSource.setPassword("platziprofesores");
		//managerDataSource.setConnectionProperties(getProperties());
		managerDataSource.setUsername("bfd149d4b8e8f2");
		managerDataSource.setUrl("jdbc:mysql://us-cdbr-iron-east-01.cleardb.net/heroku_9d53eb853c1a346");
		managerDataSource.setPassword("9e3e44c4");
		return managerDataSource;
	}
	
	public Properties getProperties() {
		Properties properties= new Properties();
		properties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
		properties.put("show_sql", "true");
		return properties;
	}
	
	@Bean
	public HibernateTransactionManager transactionManager() {
		HibernateTransactionManager transactionManager= new HibernateTransactionManager();
		transactionManager.setSessionFactory(sessionFactory().getObject());
		
		return transactionManager;
	}
}
