package io.nuls.contract.model.txdata;

import io.nuls.base.basic.NulsByteBuffer;
import io.nuls.base.basic.NulsOutputStreamBuffer;
import io.nuls.base.data.Address;
import io.nuls.base.data.BaseNulsData;
import io.nuls.core.exception.NulsException;

import java.io.IOException;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

public class DeleteContractData extends BaseNulsData implements ContractData {

    private byte[] sender;
    private byte[] contractAddress;

    @Override
    public int size() {
        int size = 0;
        size += Address.ADDRESS_LENGTH;
        size += Address.ADDRESS_LENGTH;
        return size;
    }

    @Override
    protected void serializeToStream(NulsOutputStreamBuffer stream) throws IOException {
        stream.write(sender);
        stream.write(contractAddress);
    }

    @Override
    public void parse(NulsByteBuffer byteBuffer) throws NulsException {
        this.sender = byteBuffer.readBytes(Address.ADDRESS_LENGTH);
        this.contractAddress = byteBuffer.readBytes(Address.ADDRESS_LENGTH);
    }

    @Override
    public long getGasLimit() {
        return 0L;
    }

    @Override
    public byte[] getSender() {
        return sender;
    }

    @Override
    public byte[] getCode() {
        return new byte[0];
    }

    @Override
    public long getPrice() {
        return 0L;
    }

    @Override
    public BigInteger getValue() {
        return BigInteger.ZERO;
    }

    @Override
    public String getMethodName() {
        return null;
    }

    @Override
    public String getMethodDesc() {
        return null;
    }

    @Override
    public String[][] getArgs() {
        return new String[0][];
    }

    public void setSender(byte[] sender) {
        this.sender = sender;
    }

    @Override
    public byte[] getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(byte[] contractAddress) {
        this.contractAddress = contractAddress;
    }


    public Set<byte[]> getAddresses() {
        Set<byte[]> addressSet = new HashSet<>();
        addressSet.add(contractAddress);
        return addressSet;
    }
}
