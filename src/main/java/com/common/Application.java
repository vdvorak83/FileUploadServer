package com.common;

import com.common.domain.FileInfo;
import com.common.persistence.FileInfoRepository;
import com.common.props.StorageProperties;
import com.common.service.impl.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
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

//
//        // save a couple of customers
//        repository.save(new FileInfo("Alice", "Smith", "sdas",12.22));
//        repository.save(new FileInfo("Alice", "Smith", "sdas",12.223333));
//
        // fetch all customers
//        System.out.println("Customers found with findAll():");
//        System.out.println("-------------------------------");
//        for (FileInfo customer : repository.findAll()) {
//            System.out.println(customer);
//        }
//        System.out.println();

        return (args) -> {
            repository.deleteAll();
            storageService.deleteAll();
            storageService.init();
        };
    }
}
