package io.nuls.contract;

import com.googlecode.jsonrpc4j.spring.JsonServiceExporter;
import io.nuls.contract.autoconfig.ApiModuleInitTool;
import io.nuls.contract.rpc.exception.DefineErrorResolver;
import io.nuls.contract.web.view.WebViewBootstrap;
import io.nuls.core.log.Log;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.Map;

public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {

   private  String[] args;

    public ApplicationStartup(String[] args){
        this.args=args;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        ApiModuleInitTool dataInitTool=contextRefreshedEvent.getApplicationContext().getBean(ApiModuleInitTool.class);
        try {
            Log.info("----------run Application Startup Listener-------------");
            dataInitTool.init(this.args);
            Map<String, JsonServiceExporter> beansOfType = contextRefreshedEvent.getApplicationContext().getBeansOfType(JsonServiceExporter.class);
            for(JsonServiceExporter bean : beansOfType.values()) {
                bean.setErrorResolver(DefineErrorResolver.INSTANCE);
                bean.setShouldLogInvocationErrors(false);
                bean.afterPropertiesSet();
            }
            new Thread(new WebViewBootstrap()).start();
            Log.info("---------- started -------------");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
