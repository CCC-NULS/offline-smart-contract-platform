package io.nuls.contract.service;

import io.nuls.contract.model.vo.ChainInfo;
import io.nuls.core.exception.NulsException;

import java.util.List;

public interface ChainService {

    public ChainInfo getChainInfo(List<Object> params) throws NulsException;
}
