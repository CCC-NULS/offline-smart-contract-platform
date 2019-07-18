package io.nuls.contract.account.storage.impl;

import io.nuls.base.data.Address;
import io.nuls.contract.account.model.po.AccountPo;
import io.nuls.contract.account.storage.AccountStorageService;
import io.nuls.contract.constant.AccountConstant;
import io.nuls.contract.model.RpcErrorCode;
import io.nuls.core.exception.NulsRuntimeException;
import io.nuls.core.log.Log;
import io.nuls.core.rockdb.service.RocksDBService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccountStorageServiceImpl implements AccountStorageService {
    @Override
    public boolean saveAccount(AccountPo account) {
        try {
            return RocksDBService.put(AccountConstant.DB_NAME_ACCOUNT, account.getAddressObj().getAddressBytes(), account.serialize());
        } catch (Exception e) {
            Log.error(e.getMessage());
            throw new NulsRuntimeException(RpcErrorCode.DB_SAVE_BATCH_ERROR);
        }
    }

    @Override
    public AccountPo getAccount(byte[] address) {
        byte[] accountBytes = RocksDBService.get(AccountConstant.DB_NAME_ACCOUNT, address);
        if (null == accountBytes) {
            return null;
        }
        AccountPo accountPo = new AccountPo();
        try {
            //将byte数组反序列化为AccountPo返回
            accountPo.parse(accountBytes, 0);
        } catch (Exception e) {
            Log.error(e.getMessage());
            throw new NulsRuntimeException(RpcErrorCode.DESERIALIZE_ERROR);
        }
        return accountPo;
    }

    @Override
    public boolean removeAccount(Address address) {
        if (null == address || address.getAddressBytes() == null || address.getAddressBytes().length <= 0) {
            throw new NulsRuntimeException(RpcErrorCode.PARAMETER_ERROR);
        }
        try {
            return RocksDBService.delete(AccountConstant.DB_NAME_ACCOUNT, address.getAddressBytes());
        } catch (Exception e) {
            Log.error(e.getMessage());
            throw new NulsRuntimeException(RpcErrorCode.DB_DELETE_ERROR);
        }
    }

    @Override
    public boolean updateAccount(AccountPo po) {
        if (null == po) {
            throw new NulsRuntimeException(RpcErrorCode.PARAMETER_ERROR);
        }
        if (null == po.getAddressObj()) {
            po.setAddressObj(new Address(po.getAddress()));
        }
        //校验该账户是否存在
        AccountPo account = getAccount(po.getAddressObj().getAddressBytes());
        if (null == account) {
            throw new NulsRuntimeException(RpcErrorCode.ACCOUNT_NOT_EXIST);
        }
        try {
            //更新账户数据
            return RocksDBService.put(AccountConstant.DB_NAME_ACCOUNT, po.getAddressObj().getAddressBytes(), po.serialize());
        } catch (Exception e) {
            Log.error(e.getMessage());
            throw new NulsRuntimeException(RpcErrorCode.DB_UPDATE_ERROR);
        }
    }


    @Override
    public List<AccountPo> getAccountList() {
        List<AccountPo> accountPoList = new ArrayList<>();
        try {
            List<byte[]> list = RocksDBService.valueList(AccountConstant.DB_NAME_ACCOUNT);
            if (list != null) {
                for (byte[] value : list) {
                    AccountPo accountPo = new AccountPo();
                    //将byte数组反序列化为AccountPo返回
                    accountPo.parse(value, 0);
                    accountPoList.add(accountPo);
                }
            }
        } catch (Exception e) {
            Log.error(e.getMessage());
            throw new NulsRuntimeException(RpcErrorCode.DB_QUERY_ERROR);
        }
        return accountPoList;
    }

}
