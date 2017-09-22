package com.common;

import com.common.persistence.FileInfoRepository;
import com.common.props.StorageProperties;
import com.common.service.impl.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
@ComponentScan(basePackages = {"com.common"})
public class Application  implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


    @Autowired
    private FileInfoRepository repository;

    @Override
    public void run(String... args) throws Exception {

        System.out.println("\n===============================================");
        System.out.println(Application.class.getSimpleName() + " started...");
        System.out.println("===============================================\n");
    }

    @Bean
    CommandLineRunner init(StorageService storageService) {

        return (args) -> {

            // TODO: 22/09/17 extract to property
            repository.deleteAll();
            storageService.deleteAll();
            storageService.init();
        };
    }
}
