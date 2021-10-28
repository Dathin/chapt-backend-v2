package me.pedrocaires.chapt.springconfig;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.pedrocaires.chapt.exception.UnexpectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.Properties;

@Configuration
public class BeanConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(BeanConfig.class);

	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}

	@Bean
	public InetSocketAddress inetSocketAddress() {
		return new InetSocketAddress("localhost", 8087);
	}

	@Bean
	public HikariDataSource hikariDataSource() {
		Properties properties = getProperties();
		var hikariConfig = new HikariConfig();
		hikariConfig.setDataSourceClassName("org.postgresql.ds.PGSimpleDataSource");
		hikariConfig.setPoolName("chapt");
		hikariConfig.setJdbcUrl(properties.getProperty("jdbcUrl"));
		hikariConfig.addDataSourceProperty("databaseName", properties.getProperty("databaseName"));
		hikariConfig.setUsername(properties.getProperty("user"));
		hikariConfig.setPassword(properties.getProperty("password"));
		return new HikariDataSource(hikariConfig);
	}

	@Bean
	public JdbcTemplate jdbcTemplate(DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}

	private Properties getProperties() {
		var environment = System.getenv("ENV");
		var applicationPropsPath = environment == null ? "application.properties"
				: String.format("application-%s.properties", environment);
		try (InputStream input = BeanConfig.class.getClassLoader().getResourceAsStream(applicationPropsPath)) {
			var properties = new Properties();
			properties.load(input);
			return properties;
		}
		catch (Exception ex) {
			LOGGER.error("Read properties exception was", ex);
			throw new UnexpectedException("Failed to read properties");
		}
	}

}
