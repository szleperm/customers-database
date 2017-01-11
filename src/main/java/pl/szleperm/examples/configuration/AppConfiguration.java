package pl.szleperm.examples.configuration;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories("pl.szleperm.examples.repository")
@EnableWebMvc
@ComponentScan("pl.szleperm.examples")
public class AppConfiguration extends WebMvcConfigurerAdapter{

	@Bean
	public DataSource dataSource() {
		EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
		EmbeddedDatabase db = builder.setType(EmbeddedDatabaseType.HSQL).build();
		return db;
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setDatabase(Database.HSQL);
		vendorAdapter.setGenerateDdl(true);
		factoryBean.setDataSource(dataSource());
		factoryBean.setPackagesToScan("pl.szleperm.examples");
		factoryBean.setJpaVendorAdapter(vendorAdapter);
		return factoryBean;
	}

	@Bean
	public PlatformTransactionManager transactionManager() {
		JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
		jpaTransactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
		return jpaTransactionManager;
	}

	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslator() {
		return new PersistenceExceptionTranslationPostProcessor();
	}
	@Bean
	@Lazy(false)
	public ResourceDatabasePopulator populateDatabase() throws SQLException {
		ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
		populator.addScript(new ClassPathResource("CUSTOMER.sql"));
		Connection connection = null;
		try {
			connection = DataSourceUtils.getConnection(dataSource());
			populator.populate(connection);
		} finally {
			if (connection != null) {
				DataSourceUtils.releaseConnection(connection, dataSource());
			}
		}
		return populator;
	}
	@Override
	public void addResourceHandlers(final ResourceHandlerRegistry registry) {
	    registry.addResourceHandler("/**").addResourceLocations("/");
	}
}
