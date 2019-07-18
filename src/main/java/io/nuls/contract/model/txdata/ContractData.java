package io.nuls.contract.model.txdata;

import java.math.BigInteger;

public interface ContractData {

    byte[] getContractAddress();

    byte[] getSender();

    byte[] getCode();

    long getGasLimit();

    long getPrice();

    BigInteger getValue();

    String getMethodName();

    String getMethodDesc();

    String[][] getArgs();
}
