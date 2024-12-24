package code.ecommerceproject;

import org.mariadb.jdbc.MariaDbDataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import java.sql.Connection;
import java.sql.SQLException;



@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class EcommerceProjectApplication {
    public static void main(String[] args){
        SpringApplication.run(EcommerceProjectApplication.class, args);
    }

}
