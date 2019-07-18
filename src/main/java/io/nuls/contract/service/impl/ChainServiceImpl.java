package io.nuls.contract.service.impl;

import io.nuls.contract.model.RpcErrorCode;
import io.nuls.contract.model.vo.ChainInfo;
import io.nuls.contract.service.ChainService;
import io.nuls.core.exception.NulsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service
public class ChainServiceImpl implements ChainService {

    @Autowired
    private HttpClient httpClient;

    @Override
    public ChainInfo getChainInfo(List<Object> params) throws NulsException{
        Object[] args=new Object[]{};
        if(params!=null){
            args=params.toArray();
        }
        ChainInfo result= null;
        try {
            result = httpClient.getRpcHttpClient().invoke("getChainInfo",args, ChainInfo.class);
        } catch (Throwable e) {
            throw new NulsException(RpcErrorCode.NULS_SERVICE_ERROR,e.getMessage());
        }
        return result;
    }
}
