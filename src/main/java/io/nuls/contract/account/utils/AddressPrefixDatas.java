package io.nuls.contract.account.utils;

import io.nuls.base.basic.AddressPrefixInf;
import io.nuls.contract.account.model.bo.BalanceInfo;
import io.nuls.contract.model.RpcErrorCode;
import io.nuls.contract.service.AccountService;
import io.nuls.contract.service.impl.HttpClient;
import io.nuls.core.exception.NulsException;
import io.nuls.core.log.Log;
import io.nuls.core.parse.JSONUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AddressPrefixDatas implements AddressPrefixInf {

    @Autowired
    private AccountService accountService;

    /**
     * chainId-地址映射表
     */
    private static Map<Integer, String> ADDRESS_PREFIX_MAP = new HashMap<Integer, String>();

    @Override
    public Map<Integer, String> syncAddressPrefix() {
        try {
            Map<Integer, String> address_prefix_map = accountService.getPrefixFromAccountModule();
            ADDRESS_PREFIX_MAP.putAll(address_prefix_map);
        } catch (NulsException e) {
            Log.error("get address prefix from api module failed",e.getMessage());
        }

        return ADDRESS_PREFIX_MAP;
    }
}
