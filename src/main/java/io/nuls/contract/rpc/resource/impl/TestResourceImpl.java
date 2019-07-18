package io.nuls.contract.rpc.resource.impl;

import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import io.nuls.contract.model.vo.AccountInfoVo;
import io.nuls.contract.model.RpcErrorCode;
import io.nuls.contract.model.RpcResultError;
import io.nuls.contract.rpc.resource.TestResource;
import io.nuls.contract.service.AccountKeyStoreService;
import io.nuls.contract.service.AccountService;
import io.nuls.core.exception.NulsException;
import io.nuls.core.exception.NulsRuntimeException;
import io.nuls.core.log.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@AutoJsonRpcServiceImpl
public class TestResourceImpl implements TestResource {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountKeyStoreService accountKeyStoreService;

    @Override
    public Map test(@JsonRpcParam(value = "id") long id) {
        Map<String,String> map = new HashMap<String,String>();
            if(id==1){
                map.put("address","test-result"+id);
                return map;
            }else if(id==2){
                throw new NulsRuntimeException(RpcErrorCode.PARAMETER_ERROR);
            }
        try{
            throw  new NulsException(RpcErrorCode.PARAMETER_ERROR);
        }catch (Throwable e){
            if(e.getClass().equals(NulsException.class)){
                NulsException exception=(NulsException)e;
                System.out.println("-------code------"+exception.getCode());
            }
            RpcResultError error = new RpcResultError(RpcErrorCode.ACCOUNT_NOT_EXIST.getCode(),RpcErrorCode.ACCOUNT_NOT_EXIST.getMsg());
            Log.info("-------"+e.getMessage());
            map.put("address",error.getCode()+"---"+error.getMessage());
            return   map;
        }
    }

    @Override
    public AccountInfoVo test2(@JsonRpcParam(value = "id") long id) {
        AccountInfoVo accountInfo= new AccountInfoVo();
        accountInfo.setChainId(2);
        accountInfo.setAddress("12312312312312");
        return accountInfo;
    }
}
