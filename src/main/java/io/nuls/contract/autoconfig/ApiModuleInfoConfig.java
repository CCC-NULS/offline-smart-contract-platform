package io.nuls.contract.autoconfig;

import io.nuls.contract.constant.ContractConstant;
import io.nuls.core.log.Log;
import io.nuls.core.model.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class ApiModuleInfoConfig  implements InitializingBean {

    @Value("${nuls.api.module.service.address}")
    private String apiModuleAddress;

    private String prevApiModuleAddress;

    private boolean isCreateNewClient=false;

    @Value("${language}")
    private String language;

    @Value("${dataPath:default}")
    private String dataPath;

    @Value("${keystorePath:default}")
    private String keystorePath;

    @Value("${log.path:default}")
    private String logPath;

    @Value("${log.level}")
    private String logLevel;

    @Value("${chainId}")
    private String chainId;

    @Value("${assetId}")
    private String assetId;

    @Value("${assetChainId}")
    private String assetChainId;

    private String defaultJarFilePath;

    @Value("${version}")
    private String version;

    public  String  getApiModuleAddress() {
        if(StringUtils.isNotBlank(this.apiModuleAddress) && !this.apiModuleAddress.toLowerCase().startsWith("http")){
            this.apiModuleAddress = "http://" + this.apiModuleAddress;
        }
        return this.apiModuleAddress;
    }

    public void  setApiModuleAddress(String address){
        this.prevApiModuleAddress=apiModuleAddress;
        this.isCreateNewClient=true;
        if(StringUtils.isNotBlank(address) && !address.toLowerCase().startsWith("http")){
            this.apiModuleAddress = "http://" + address;
        }else{
            this.apiModuleAddress =address;
        }
    }

    public String getPrevApiModuleAddress() {
        return prevApiModuleAddress;
    }

    public boolean isCreateNewClient() {
        return isCreateNewClient;
    }

    public void setCreateNewClient(boolean createNewClient) {
        isCreateNewClient = createNewClient;
    }

    public String getLogPath() {
        //若不peoperties文件不配置此参数，则设置缺省值
        if(logPath.equals("default")){
            logPath= System.getProperty("user.home")+File.separator+ ContractConstant.DATA_FILE_NAME+ File.separator+"logs";
        }
        return logPath;
    }

    public void setLogPath(String logPath) {
        this.logPath = logPath;
    }

    public String getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }

    public String getChainId() {
        return chainId;
    }

    public void setChainId(String chainId) {
        this.chainId = chainId;
    }

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public String getDataPath() {
        //若不peoperties文件不配置此参数，则设置缺省值
        if( dataPath.equals("default")){
            dataPath= System.getProperty("user.home")+File.separator+ ContractConstant.DATA_FILE_NAME+ File.separator+"data";
        }
        return dataPath;
    }

    public void setDataPath(String dataPath) {
        this.dataPath = dataPath;
    }

    public String getKeystorePath() {
        //若不peoperties文件不配置此参数，则设置缺省值
        if(keystorePath.equals("default")){
            keystorePath= System.getProperty("user.home")+File.separator+ ContractConstant.DATA_FILE_NAME+ File.separator+"keystore"+ File.separator+"backup";
        }
        return keystorePath;
    }

    public void setKeystorePath(String keystorePath) {
        this.keystorePath = keystorePath;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getAssetChainId() {
        return assetChainId;
    }

    public void setAssetChainId(String assetChainId) {
        this.assetChainId = assetChainId;
    }

    public String getDefaultJarFilePath() {
        if(StringUtils.isBlank(defaultJarFilePath)){
            defaultJarFilePath=System.getProperty("user.home")+File.separator+ContractConstant.PACKING_FILE_PATH;
        }
        return defaultJarFilePath;
    }

    public void setDefaultJarFilePath(String defaultJarFilePath) {
        this.defaultJarFilePath = defaultJarFilePath;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.setProperty("log.level",this.getLogLevel());
        System.setProperty("log.path",this.getLogPath());
    }
}
