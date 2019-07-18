package io.nuls.contract.utils;

public class DBUtil {

    //交易关系表分片数量
    static int TX_RELATION_SHARDING_COUNT = 128;

    public static int getShardNumber(String value) {
        return Math.abs(value.hashCode()) % TX_RELATION_SHARDING_COUNT;
    }

    public static String getAssetKey(int chainId, int assetId) {
        return chainId + "-" + assetId;
    }

    public static String getAccountAssetKey(String address, int chainId, int assetId) {
        return address + "-" + chainId + "-" + assetId;
    }

    public static String getDepositKey(String hash, String key) {
        return hash + "-" + key;
    }
}
