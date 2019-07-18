package io.nuls.contract.account.model.po;

import io.nuls.contract.account.model.bo.AccountKeyStore;
import io.nuls.core.crypto.HexUtil;

import java.util.Map;

public class AccountKeyStoreDto {
    /**
     * 账户地址
     */
    private String address;
    /**
     * 加密后的私钥
     */
    private String encryptedPrivateKey;
    /**
     * 公钥
     */
    private String pubKey;
    /**
     * 私钥
     */
    private String prikey;

    public AccountKeyStoreDto() {

    }

    public AccountKeyStoreDto(AccountKeyStore accountKeyStore) {
        this.address = accountKeyStore.getAddress();
        this.encryptedPrivateKey = null == accountKeyStore.getEncryptedPrivateKey() ? null : accountKeyStore.getEncryptedPrivateKey();
        this.pubKey = HexUtil.encode(accountKeyStore.getPubKey());
        this.prikey = null == accountKeyStore.getPrikey() ? null : HexUtil.encode(accountKeyStore.getPrikey());
    }

    public AccountKeyStore toAccountKeyStore() {
        AccountKeyStore accountKeyStore = new AccountKeyStore();
        accountKeyStore.setAddress(this.address);
        accountKeyStore.setEncryptedPrivateKey(this.encryptedPrivateKey);
        if (null == this.prikey || "null".toUpperCase().equals(this.prikey.trim().toUpperCase()) || "".equals(prikey.trim())) {
            accountKeyStore.setPrikey(null);
        } else {
            try {
                accountKeyStore.setPrikey(HexUtil.decode(this.prikey.trim()));
            } catch (Exception e) {
                accountKeyStore.setPrikey(null);
            }
        }
        accountKeyStore.setPubKey(HexUtil.decode(this.pubKey));
        return accountKeyStore;
    }

    public AccountKeyStoreDto(Map<String, Object> map) {
        this.address = (String) map.get("address");
        this.encryptedPrivateKey = null == map.get("encryptedPrivateKey") ? null : (String) map.get("encryptedPrivateKey");
        this.pubKey = null == map.get("pubKey") ? null : (String) map.get("pubKey");
        this.prikey = null == map.get("prikey") ? null : (String) map.get("prikey");
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEncryptedPrivateKey() {
        return encryptedPrivateKey;
    }

    public void setEncryptedPrivateKey(String encryptedPrivateKey) {
        this.encryptedPrivateKey = encryptedPrivateKey;
    }

    public String getPubKey() {
        return pubKey;
    }

    public void setPubKey(String pubKey) {
        this.pubKey = pubKey;
    }

    public String getPrikey() {
        return prikey;
    }

    public void setPrikey(String prikey) {
        this.prikey = prikey;
    }
}
