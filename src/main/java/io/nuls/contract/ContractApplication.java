package io.nuls.contract;

import io.nuls.contract.constant.ConfigurerConstant;
import io.nuls.core.log.Log;
import io.nuls.core.model.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
public class ContractApplication extends SpringBootServletInitializer implements EmbeddedServletContainerCustomizer {
    public static void main(String[] args) {
        System.setProperty("java.awt.headless", "false");
        if(args!=null && args.length>0){
            Log.info("The default path of the smart contract jar file ："+args[0]);
            if(args.length>2){
                System.setProperty("server.port",args[1]);
                if(!Boolean.valueOf(args[2])){
                    //第三个boolean参数表示是否启动客户端，若为false则不启动
                    Log.info("Do not launch offline smart contract client");
                    return;
                }
            }else{
                System.setProperty("server.port",ConfigurerConstant.DEFAULTPORT);
            }
        }else{
            Log.error("The default path of the smart contract jar file is not set");
            System.setProperty("server.port",ConfigurerConstant.DEFAULTPORT);
        }
        Log.info("The service port of the offline smart contract client ："+System.getProperty("server.port"));
        SpringApplication springApplication=new SpringApplication(ContractApplication.class);
        springApplication.addListeners(new ApplicationStartup(args));
        springApplication.run(args);
    }

    @Override
    public void customize(ConfigurableEmbeddedServletContainer container) {
        String port = System.getProperty("server.port");
        container.setPort(Integer.valueOf(port));
    }
}
