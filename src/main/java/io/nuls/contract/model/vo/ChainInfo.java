package io.nuls.contract.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.nuls.contract.constant.ContractConstant;

import java.util.ArrayList;
import java.util.List;

public class ChainInfo {


    private int chainId;

    private String chainName;

    private AssetInfo defaultAsset;

    private List<AssetInfo> assets;

    private List<String> seeds;

    private int status;

    @JsonIgnore
    private boolean isNew;

    public ChainInfo() {
        assets = new ArrayList<>();
        seeds = new ArrayList<>();
        status = ContractConstant.ENABLE;
    }

    public AssetInfo getAsset(int assetId) {
        for (AssetInfo assetInfo : assets) {
            if (assetInfo.getAssetId() == assetId) {
                return assetInfo;
            }
        }
        return null;
    }

    public AssetInfo removeAsset(int assetId) {
        for (int i = 0; i < assets.size(); i++) {
            AssetInfo assetInfo = assets.get(i);
            if (assetInfo.getAssetId() == assetId) {
                return assets.remove(i);
            }
        }
        return null;
    }

    public int getChainId() {
        return chainId;
    }

    public void setChainId(int chainId) {
        this.chainId = chainId;
    }

    public AssetInfo getDefaultAsset() {
        return defaultAsset;
    }

    public void setDefaultAsset(AssetInfo defaultAsset) {
        this.defaultAsset = defaultAsset;
    }

    public List<AssetInfo> getAssets() {
        return assets;
    }

    public void setAssets(List<AssetInfo> assets) {
        this.assets = assets;
    }

    public List<String> getSeeds() {
        return seeds;
    }

    public void setSeeds(List<String> seeds) {
        this.seeds = seeds;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public String getChainName() {
        return chainName;
    }

    public void setChainName(String chainName) {
        this.chainName = chainName;
    }
}
