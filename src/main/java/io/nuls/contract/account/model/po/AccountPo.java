package io.nuls.contract.account.model.po;

import io.nuls.base.basic.NulsByteBuffer;
import io.nuls.base.basic.NulsOutputStreamBuffer;
import io.nuls.base.data.Address;
import io.nuls.base.data.BaseNulsData;
import io.nuls.contract.account.model.bo.Account;
import io.nuls.contract.model.vo.AccountInfoVo;
import io.nuls.contract.model.RpcErrorCode;
import io.nuls.core.crypto.AESEncrypt;
import io.nuls.core.crypto.ECKey;
import io.nuls.core.crypto.EncryptedData;
import io.nuls.core.exception.CryptoException;
import io.nuls.core.exception.NulsException;
import io.nuls.core.log.Log;
import io.nuls.core.model.FormatValidUtils;
import io.nuls.core.model.ObjectUtils;
import io.nuls.core.parse.SerializeUtils;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;

public class AccountPo extends BaseNulsData {

    private transient Address addressObj;

    private String address;

    private int chainId;

    private Long createTime;

    private String alias;

    private byte[] pubKey;

    private byte[] priKey;

    private byte[] encryptedPriKey;

    private byte[] extend;

    private int status;

    private String remark;

    public AccountPo() {
    }

    public AccountPo(Account account) {
        this.addressObj = account.getAddress();
        this.chainId = account.getChainId();
        this.address = account.getAddress().toString();
        this.alias = account.getAlias();
        this.pubKey = account.getPubKey();
        this.priKey = account.getPriKey();
        this.encryptedPriKey = account.getEncryptedPriKey();
        this.extend = account.getExtend();
        this.remark = account.getRemark();
        this.createTime = account.getCreateTime();
    }

    public Account toAccount() {
        Account account = new Account();
        account.setChainId(this.getChainId());
        try {
            account.setAddress(Address.fromHashs(this.getAddress()));
        } catch (Exception e) {
            Log.error(e);
        }
        account.setAlias(this.getAlias());
        account.setExtend(this.getExtend());
        account.setPriKey(this.getPriKey());
        account.setPubKey(this.getPubKey());
        account.setEncryptedPriKey(this.getEncryptedPriKey());
        if (this.getPriKey() != null && this.getPriKey().length > 1) {
            account.setEcKey(ECKey.fromPrivate(new BigInteger(1, account.getPriKey())));
        } else {
            account.setEcKey(ECKey.fromEncrypted(new EncryptedData(this.getEncryptedPriKey()), this.getPubKey()));
        }
        account.setRemark(this.remark);
        account.setCreateTime(this.getCreateTime());
        return account;
    }

    public AccountInfoVo toAccountInfoVo(){
        AccountInfoVo accountInfoVo =new AccountInfoVo();
        accountInfoVo.setChainId(this.getChainId());
        accountInfoVo.setAddress(this.getAddress());
        accountInfoVo.setCreateTime(this.getCreateTime());
        return accountInfoVo;
    }

    @Override
    public int size() {
        int size = 0;
        //chainId
        size += SerializeUtils.sizeOfUint16();
        size += SerializeUtils.sizeOfString(address);
        size += SerializeUtils.sizeOfString(alias);
        //createTime
        size += SerializeUtils.sizeOfUint48();
        size += SerializeUtils.sizeOfBytes(pubKey);
        size += SerializeUtils.sizeOfBytes(priKey);
        size += SerializeUtils.sizeOfBytes(encryptedPriKey);
        size += SerializeUtils.sizeOfBytes(extend);
        size += SerializeUtils.sizeOfString(remark);
        return size;
    }

    @Override
    protected void serializeToStream(NulsOutputStreamBuffer stream) throws IOException {
        stream.writeUint16(chainId);
        stream.writeString(address);
        stream.writeString(alias);
        stream.writeUint48(createTime);
        stream.writeBytesWithLength(pubKey);
        stream.writeBytesWithLength(priKey);
        stream.writeBytesWithLength(encryptedPriKey);
        stream.writeBytesWithLength(extend);
        stream.writeString(remark);
    }

    @Override
    public void parse(NulsByteBuffer byteBuffer) throws NulsException {
        this.chainId = byteBuffer.readUint16();
        this.address = byteBuffer.readString();
        this.alias = byteBuffer.readString();
        this.createTime = byteBuffer.readUint48();
        this.pubKey = byteBuffer.readByLengthByte();
        this.priKey = byteBuffer.readByLengthByte();
        this.encryptedPriKey = byteBuffer.readByLengthByte();
        this.extend = byteBuffer.readByLengthByte();
        this.remark = byteBuffer.readString();
    }

    public Address getAddressObj() {
        return addressObj;
    }

    public void setAddressObj(Address addressObj) {
        this.addressObj = addressObj;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getChainId() {
        return chainId;
    }

    public void setChainId(int chainId) {
        this.chainId = chainId;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public byte[] getPubKey() {
        return pubKey;
    }

    public void setPubKey(byte[] pubKey) {
        this.pubKey = pubKey;
    }

    public byte[] getPriKey() {
        return priKey;
    }

    public void setPriKey(byte[] priKey) {
        this.priKey = priKey;
    }

    public byte[] getEncryptedPriKey() {
        return encryptedPriKey;
    }

    public void setEncryptedPriKey(byte[] encryptedPriKey) {
        this.encryptedPriKey = encryptedPriKey;
    }

    public byte[] getExtend() {
        return extend;
    }

    public void setExtend(byte[] extend) {
        this.extend = extend;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 账户是否被加密(是否设置过密码)
     * Whether the account is encrypted (Whether the password is set)
     */
    public boolean isEncrypted() {
        return getEncryptedPriKey() != null && getEncryptedPriKey().length > 0;
    }

    /**
     * 验证账户密码是否正确
     * Verify that the account password is correct
     */
    public boolean validatePassword(String password) {
        boolean result = FormatValidUtils.validPassword(password);
        if (!result) {
            return false;
        }
        byte[] unencryptedPrivateKey;
        try {
            unencryptedPrivateKey = AESEncrypt.decrypt(this.getEncryptedPriKey(), password);
        } catch (CryptoException e) {
            return false;
        }
        BigInteger newPriv = new BigInteger(1, unencryptedPrivateKey);
        ECKey key = ECKey.fromPrivate(newPriv);

        return Arrays.equals(key.getPubKey(), getPubKey());
    }

    /**
     * 根据密码获取ECKey
     */
    public ECKey getEcKey(String password) throws NulsException {
        ECKey eckey = null;
        byte[] unencryptedPrivateKey;
        //判断当前账户是否存在私钥，如果不存在私钥这为加密账户
        BigInteger newPriv = null;
        if (this.isEncrypted()) {
            ObjectUtils.canNotEmpty(password, "the password can not be empty");
            if (!validatePassword(password)) {
                throw new NulsException(RpcErrorCode.PASSWORD_IS_WRONG);
            }
            try {
                unencryptedPrivateKey = AESEncrypt.decrypt(this.getEncryptedPriKey(), password);
                newPriv = new BigInteger(1, unencryptedPrivateKey);
            } catch (CryptoException e) {
                throw new NulsException(RpcErrorCode.PASSWORD_IS_WRONG);
            }
        } else {
            newPriv = new BigInteger(1, this.getPriKey());
        }
        eckey = ECKey.fromPrivate(newPriv);
        if (!Arrays.equals(eckey.getPubKey(), getPubKey())) {
            throw new NulsException(RpcErrorCode.PASSWORD_IS_WRONG);
        }
        return eckey;
    }
}
