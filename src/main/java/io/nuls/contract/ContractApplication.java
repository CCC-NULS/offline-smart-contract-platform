package io.nuls.contract;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ContractApplication {
    public static void main(String[] args) {
        System.out.println("--------------start offline smart contract application--------------");

        SpringApplication springApplication=new SpringApplication(ContractApplication.class);
        springApplication.addListeners(new ApplicationStartup());
        springApplication.run(args);

    }
}
