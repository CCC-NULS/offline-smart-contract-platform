package io.nuls.contract.service.impl;

import io.nuls.contract.model.RpcErrorCode;
import io.nuls.contract.model.vo.TransactionInfo;
import io.nuls.contract.service.TransactionService;
import io.nuls.core.exception.NulsException;
import io.nuls.core.exception.NulsRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private HttpClient httpClient;

    @Override
    public boolean broadcastTx(int chainId, String txHex) throws NulsException {
        boolean isSuccess=false;
        Map result= null;
        try {
            result = httpClient.getRpcHttpClient().invoke("broadcastTx",new Object[]{chainId,txHex}, Map.class);
        } catch (Throwable e) {
            throw new NulsException(RpcErrorCode.NULS_SERVICE_ERROR,e.getMessage());
        }
        boolean  successStr=(boolean) result.get("value");
        if(successStr){
            isSuccess=true;
        }else{
            throw new NulsRuntimeException(RpcErrorCode.NULS_SERVICE_ERROR,result.get("msg").toString());
        }
        return isSuccess;
    }

    public TransactionInfo getTx(int chainId, String txHash)throws NulsException {
        TransactionInfo info= null;
        try {
            info = httpClient.getRpcHttpClient().invoke("getTx",new Object[]{chainId,txHash}, TransactionInfo.class);
        } catch (Throwable e) {
            throw new NulsException(RpcErrorCode.NULS_SERVICE_ERROR,e.getMessage());
        }
        return info;
    }


}
