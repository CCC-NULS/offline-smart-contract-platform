package io.nuls.contract.account.model.bo;

public class AccountKeyStore {

    private String address;
    private String encryptedPrivateKey;
    private byte[] prikey;
    private String alias;
    private byte[] pubKey;

    public AccountKeyStore() {
    }

    public AccountKeyStore(String address, String encryptedPrivateKey) {
        this.address = address;
        this.encryptedPrivateKey = encryptedPrivateKey;
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

    public byte[] getPrikey() {
        return prikey;
    }

    public void setPrikey(byte[] prikey) {
        this.prikey = prikey;
    }
}
