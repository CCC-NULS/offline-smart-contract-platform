package io.nuls.contract.service.impl;

import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import io.nuls.contract.autoconfig.ApiModuleInfoConfig;
import io.nuls.core.log.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URL;

@Service
public class HttpClient {

    private JsonRpcHttpClient rpcHttpClient;

    @Autowired
    public HttpClient(ApiModuleInfoConfig infoConfig){
        try{
            String accessPath=infoConfig.getApiModuleAddress();
            Log.info("api module service path: "+accessPath);
            rpcHttpClient=new JsonRpcHttpClient(new URL(accessPath));
        }catch (Exception e){
            Log.error(e);
        }

    }

    public JsonRpcHttpClient getRpcHttpClient() {
        return rpcHttpClient;
    }
}
