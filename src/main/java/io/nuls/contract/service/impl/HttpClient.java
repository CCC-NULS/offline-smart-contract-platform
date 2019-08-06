package io.nuls.contract.service.impl;

import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import io.nuls.contract.autoconfig.ApiModuleInfoConfig;
import io.nuls.contract.utils.StringUtils;
import io.nuls.core.log.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URL;

@Service
public class HttpClient {

    private JsonRpcHttpClient rpcHttpClient;

    @Autowired
    private  ApiModuleInfoConfig infoConfig;

    @Autowired
    public HttpClient(ApiModuleInfoConfig infoConfig){
        try{
            System.out.println(infoConfig.isCreateNewClient());
            String accessPath=infoConfig.getApiModuleAddress();
            Log.info("init api module service path: "+accessPath);
            rpcHttpClient=new JsonRpcHttpClient(new URL(accessPath));
        }catch (Exception e){
            Log.error(e);
        }
    }

    public JsonRpcHttpClient getRpcHttpClient() {
        try{
            String prevUrl = infoConfig.getPrevApiModuleAddress();
            String nowUrl=infoConfig.getApiModuleAddress();
            if(infoConfig.isCreateNewClient()&&StringUtils.isNotBlank(prevUrl)&&!prevUrl.equals(nowUrl)){
                Log.info("now api module service path: "+nowUrl);
                rpcHttpClient=new JsonRpcHttpClient(new URL(nowUrl));
                infoConfig.setCreateNewClient(false);
            }
        }catch (Exception e){
            Log.error(e);
        }
        return rpcHttpClient;
    }
}
