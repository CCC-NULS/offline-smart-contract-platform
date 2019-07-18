package io.nuls.contract.rpc.resource;

import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;
import io.nuls.contract.model.vo.AccountInfoVo;

import java.util.Map;

@JsonRpcService("/account")
public interface TestResource {

    public Map test(@JsonRpcParam(value = "id") long id);

    public AccountInfoVo test2(@JsonRpcParam(value = "id") long id);

}
