package io.nuls.contract.autoconfig;

import io.nuls.base.basic.AddressTool;
import io.nuls.contract.account.utils.AddressPrefixDatas;
import io.nuls.contract.constant.AccountConstant;
import io.nuls.contract.model.RpcErrorCode;
import io.nuls.core.exception.NulsException;
import io.nuls.core.log.Log;
import io.nuls.core.parse.I18nUtils;
import io.nuls.core.rockdb.constant.DBErrorCode;
import io.nuls.core.rockdb.service.RocksDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.File;

@Component
public class ApiModuleInitTool {

    /**
     * 导出keystore备份文件目录
     */
    public static String ACCOUNTKEYSTORE_FOLDER_NAME = "keystore/backup";

    @Autowired
    private ApiModuleInfoConfig infoConfig;

    @Autowired
    private AddressPrefixDatas addressPrefixDatas;

    public void init(String[] args) throws Exception{
        if(args!=null && args.length>0){
            infoConfig.setDefaultJarFilePath(args[0]);
        }
        loadLanguage();
        initDB();
        AddressTool.init(addressPrefixDatas);
    }


    private void loadLanguage(){
        I18nUtils.loadLanguage(this.getClass(), "languages",infoConfig.getLanguage());
    }

    /**
     * 初始化数据库
     * Initialization database
     */
    private void initDB() throws Exception {
        String dataPath=infoConfig.getDataPath();
        //读取配置文件，数据存储根目录，初始化打开该目录下所有表连接并放入缓存
        RocksDBService.init(dataPath + File.separator + "account");
        //初始化表
        try {
            //If tables do not exist, create tables.
            if (!RocksDBService.existTable(AccountConstant.DB_NAME_ACCOUNT)) {
                RocksDBService.createTable(AccountConstant.DB_NAME_ACCOUNT);
            }
        } catch (Exception e) {
            if (!DBErrorCode.DB_TABLE_EXIST.equals(e.getMessage())) {
                Log.error(e.getMessage());
                throw new NulsException(RpcErrorCode.DB_TABLE_CREATE_ERROR);
            } else {
                Log.info(e.getMessage());
            }
        }
    }
}
