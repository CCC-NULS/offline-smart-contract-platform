package io.nuls.contract.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.googlecode.jsonrpc4j.JsonRpcClientException;
import io.nuls.contract.account.model.bo.ContractInfo;
import io.nuls.contract.model.ContractMethodArg;
import io.nuls.contract.model.ContractResultInfo;
import io.nuls.contract.model.RpcErrorCode;
import io.nuls.contract.model.deserialization.ContractResultDataDto;
import io.nuls.contract.service.ContractService;
import io.nuls.core.exception.NulsException;
import io.nuls.core.exception.NulsRuntimeException;
import io.nuls.core.log.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ContractServiceImpl implements ContractService {

    @Autowired
    private HttpClient httpClient;

    @Override
    public String[] getContractConstructorArgsTypes(int chainId, String contractCode) throws NulsException {
        String[] types=null;
        Map result= null;
        try {
            result = httpClient.getRpcHttpClient().invoke("getContractConstructor",new Object[]{chainId,contractCode}, Map.class);
        }catch (Throwable e) {
            throw new NulsException(RpcErrorCode.NULS_SERVICE_ERROR, parseErrorMsg(e));
        }
        if(result!=null){
            Map constructor = (Map)result.get("constructor");
            List<Map> args = (List<Map>) constructor.get("args");
            int size = args.size();
            String[] argTypes = new String[size];
            int i = 0;
            for(Map arg : args) {
                argTypes[i++] = arg.get("type").toString();
            }
            types=argTypes;
        }
        return types;
    }

    @Override
    public Map getContractConstructor(int chainId, String contractCode) throws NulsException {
        Map<String,Object> map=new HashMap<String,Object>();
        Map result= null;
        try {
            result = httpClient.getRpcHttpClient().invoke("getContractConstructor",new Object[]{chainId,contractCode}, Map.class);
        } catch (Throwable e) {
            throw new NulsException(RpcErrorCode.NULS_SERVICE_ERROR, parseErrorMsg(e));
        }
        if(result!=null){
            Map constructor = (Map)result.get("constructor");
            boolean isNrc20= (boolean) result.get("nrc20");
            List<Map> args = (List<Map>) constructor.get("args");
            int size = args.size();
            ContractMethodArg[] argTypes = new ContractMethodArg[size];
            int i = 0;
            for(Map arg : args) {
                ContractMethodArg methodArg=new ContractMethodArg();
                methodArg.setName(arg.get("name").toString());
                methodArg.setType(arg.get("type").toString());
                methodArg.setRequired((Boolean) arg.get("required"));
                argTypes[i++] = methodArg;
            }
            map.put("constructor",argTypes);
            map.put("isNrc20",isNrc20);
        }
        return map;
    }

    @Override
    public boolean validateContractCreate(int chainId, String sender, long gasLimit, long price, String contractCode, Object[] args) throws NulsException {
        boolean isSuccess=false;
        Map result= null;
        try {
            result = httpClient.getRpcHttpClient().invoke("validateContractCreate",new Object[]{chainId,sender,gasLimit,price,contractCode,args}, Map.class);
        } catch (Throwable e) {
            throw new NulsException(RpcErrorCode.NULS_SERVICE_ERROR, parseErrorMsg(e));
        }
        if(result!=null){
            boolean  successStr=(boolean) result.get("success");
            if(successStr){
                isSuccess=true;
            }else{
                throw new NulsException(RpcErrorCode.NULS_SERVICE_ERROR,result.get("msg").toString());
            }
        }
        return isSuccess;
    }

    @Override
    public int imputedContractCreateGas(int chainId, String sender, String contractCode, Object[] args) throws NulsException {
        int gasLimit=0;
        try{
            Map result=null;
            result= httpClient.getRpcHttpClient().invoke("imputedContractCreateGas",new Object[]{chainId,sender,contractCode,args}, Map.class);
            if(result!=null){
                gasLimit=(int)result.get("gasLimit");
            }
        }catch (Throwable e){
            Log.error("call api-moudle: imputedContractCreateGas error",e.getMessage());
            throw new NulsException(RpcErrorCode.NULS_SERVICE_ERROR, parseErrorMsg(e));
        }
        return gasLimit;
    }

    @Override
    public String[] getContractMethodArgsTypes(int chainId, String contractAddress, String methodname) throws NulsException {
        String[] argsTypes=null;
        List result= null;
        try {
            result = httpClient.getRpcHttpClient().invoke("getContractMethodArgsTypes",new Object[]{chainId,contractAddress,methodname}, List.class);
        }  catch (Throwable e) {
            throw new NulsException(RpcErrorCode.NULS_SERVICE_ERROR, parseErrorMsg(e));
        }
        if(result!=null){
            argsTypes =new String[result.size()];
            result.toArray(argsTypes);
        }
        return argsTypes;
    }

    @Override
    public boolean validateContractCall(int chainId, String sender, BigInteger value, long gasLimit, long price, String contractAddress, String methodName, String methodDesc, Object[] args) throws NulsException {
        boolean isSuccess=false;
        try {
            Map  result = httpClient.getRpcHttpClient().invoke("validateContractCall",new Object[]{chainId,sender,value,gasLimit,price,contractAddress,methodName,methodDesc,args}, Map.class);
            if(result!=null){
                boolean  successStr=(boolean) result.get("success");
                if(successStr){
                    isSuccess=true;
                }else{
                    throw new NulsException(RpcErrorCode.NULS_SERVICE_ERROR,result.get("msg").toString());
                }
            }
        } catch (Throwable e) {
            throw new NulsException(RpcErrorCode.NULS_SERVICE_ERROR, parseErrorMsg(e));
        }
        return isSuccess;
    }

    @Override
    public int imputedContractCallGas(int chainId, String sender, BigInteger value, String contractAddress, String methodName, String methodDesc, Object[] args) throws NulsException {
        int gasLimit=0;
        Map result=null;
        try{
            result= httpClient.getRpcHttpClient().invoke("imputedContractCallGas",new Object[]{chainId,sender,value,contractAddress,methodName,methodDesc,args}, Map.class);
            if(result!=null){
                if(result.containsKey("gasLimit")){
                    gasLimit=(int) result.get("gasLimit");
                }else {
                    throw new NulsRuntimeException(RpcErrorCode.NULS_SERVICE_ERROR,result.get("msg").toString());
                }
            }
        }catch (Throwable e){
            Log.error("call api-moudle: imputedContractCreateGas error",e.getMessage());
            throw new NulsException(RpcErrorCode.NULS_SERVICE_ERROR, parseErrorMsg(e));
        }
        return gasLimit;
    }

    @Override
    public boolean validateContractDelete(int chainId, String sender, String contractAddress) throws NulsException {
        boolean isSuccess=false;
        Map result= null;
        try {
            result = httpClient.getRpcHttpClient().invoke("validateContractDelete",new Object[]{chainId,sender,contractAddress}, Map.class);
        } catch (Throwable e) {
            throw new NulsException(RpcErrorCode.NULS_SERVICE_ERROR, parseErrorMsg(e));
        }
        if(result!=null){
            boolean  successStr=(boolean) result.get("success");
            if(successStr){
                isSuccess=true;
            }else{
                throw new NulsException(RpcErrorCode.NULS_SERVICE_ERROR,result.get("msg").toString());
            }
        }
        return isSuccess;
    }

    @Override
    public String invokeView(int chainId, Object contractAddress, Object methodName, Object methodDesc, Object args) throws NulsException {
        String invokeResult=null;
        Map result = null;
        try {
            result = httpClient.getRpcHttpClient().invoke("invokeView",new Object[]{chainId,contractAddress,methodName,methodDesc,args}, Map.class);
        } catch (Throwable e) {
            throw new NulsException(RpcErrorCode.NULS_SERVICE_ERROR, parseErrorMsg(e));
        }
        if(result!=null){
            if(result.containsKey("result")){
                invokeResult=(String) result.get("result");
            }else {
                throw new NulsRuntimeException(RpcErrorCode.NULS_SERVICE_ERROR,result.get("msg").toString());
            }
        }
        return invokeResult;
    }

    @Override
    public ContractInfo getContract(int chainId, String contractAddress) throws NulsException {
        ContractInfo contractInfo = null;
        try {
            contractInfo = httpClient.getRpcHttpClient().invoke("getContract",new Object[]{chainId,contractAddress}, ContractInfo.class);
        } catch (Throwable e) {
            throw new NulsException(RpcErrorCode.NULS_SERVICE_ERROR, parseErrorMsg(e));
        }
        return contractInfo;
    }

    @Override
    public ContractResultDataDto getContractTxResult(int chainId, String txHash) throws NulsException {
        ContractResultDataDto contractResultInfo = null;
        try {
            contractResultInfo = httpClient.getRpcHttpClient().invoke("getContractTxResult",new Object[]{chainId,txHash}, ContractResultDataDto.class);
        } catch (Throwable e) {
            throw new NulsException(RpcErrorCode.NULS_SERVICE_ERROR, parseErrorMsg(e));
        }
        return contractResultInfo;
    }

    private String parseErrorMsg(Throwable e) {
        String msg = e.getMessage();
        if(e instanceof JsonRpcClientException) {
            JsonNode data = ((JsonRpcClientException) e).getData();
            if(data != null) {
                msg = data.asText();
            }
        }
        return msg;
    }

}
