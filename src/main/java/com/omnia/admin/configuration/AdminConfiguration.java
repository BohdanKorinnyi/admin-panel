package com.omnia.admin.configuration;

import lombok.extern.log4j.Log4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import static com.omnia.admin.AdminApplication.configurationFile;

@Log4j
@Configuration
@EnableSwagger2
public class AdminConfiguration {
    public static final String WRITE_PREFIX = "write_";
    public static final String DB_NAME_KEY = "db_name";
    public static final String DB_USER_KEY = "db_user";
    public static final String DB_PASSWORD_KEY = "db_password";
    public static final String DB_ADDRESS_KEY = "db_address";
    public static final String DB_PORT_KEY = "db_port";
    public static final String MYSQL_HOST = "jdbc:mysql://";
    public static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
    public static final String MYSQL_CONNECTION_PROPERTIES = "?useSSL=false&createDatabaseIfNotExist=true&autoReconnect=true";
    public static final String COLUMN = ":";
    public static final String SLASH = "/";

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }

    @Bean
    public DataSource dataSource() {
        Properties properties = new Properties();
        try (InputStream inputStream = new FileInputStream(configurationFile)) {
            properties.load(inputStream);
            String url = getDbUrl(properties.getProperty(DB_ADDRESS_KEY),
                    properties.getProperty(DB_PORT_KEY),
                    properties.getProperty(DB_NAME_KEY));

            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setDriverClassName(MYSQL_DRIVER);
            dataSource.setUrl(url);
            dataSource.setUsername(properties.getProperty(DB_USER_KEY));
            dataSource.setPassword(properties.getProperty(DB_PASSWORD_KEY));
            return dataSource;
        } catch (Exception e) {
            log.error("Bad confgiuration to database");
        }
        throw new RuntimeException("Wrong read only dataSource configurations");
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    private static String getDbUrl(String address, String port, String name) {
        return MYSQL_HOST.concat(address)
                .concat(COLUMN)
                .concat(port)
                .concat(SLASH)
                .concat(name)
                .concat(MYSQL_CONNECTION_PROPERTIES);
    }
}
