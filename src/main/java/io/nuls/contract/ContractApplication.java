package io.nuls.contract;

import io.nuls.core.log.Log;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ContractApplication {
    public static void main(String[] args) {
        System.setProperty("java.awt.headless", "false");
        if(args!=null && args.length>0){
            Log.info("The default path of the smart contract jar file ："+args[0]);
        }else{
            Log.error("The default path of the smart contract jar file is not set");
        }
        SpringApplication springApplication=new SpringApplication(ContractApplication.class);
        springApplication.addListeners(new ApplicationStartup(args));
        springApplication.run(args);
    }
}
