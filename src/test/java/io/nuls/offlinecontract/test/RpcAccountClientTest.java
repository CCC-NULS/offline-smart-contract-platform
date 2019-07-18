package io.nuls.offlinecontract.test;

import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import io.nuls.contract.model.vo.AccountInfoVo;
import io.nuls.contract.account.model.po.AccountKeyStoreDto;
import io.nuls.core.basic.Page;
import io.nuls.core.crypto.HexUtil;
import io.nuls.core.parse.JSONUtils;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;
import java.util.Map;

public class RpcAccountClientTest {
    private static JsonRpcHttpClient memberClient;
    private static final long test_memberId = 3L;

    private int chainId=2;
    private  int assetId=1;
    private int assetChainId=2;
    private String address="tNULSeBaMvEtDfvZuukDf2mVyfGo3DdiN8KLRG";
    private String password="nuls123456";

    private int pageNumber=1;
    private int pageSize=10;
    @Before
    public void setUp() throws Throwable {
        memberClient = new JsonRpcHttpClient(new URL("http://127.0.0.1/offlineSmartContract"));
    }

    @Test
    public void testRPC()throws Throwable {
        String result = memberClient.invoke("test", new Object[] {test_memberId}, String.class);
        System.out.println(result);
    }

    @Test
    public  void createAccount() throws Throwable{
        Map result = memberClient.invoke("createAccount", new Object[] {chainId,password}, Map.class);
        System.out.println(result.get("address"));
    }

    @Test
    public  void getAccount() throws Throwable{
        AccountInfoVo result = memberClient.invoke("getAccount", new Object[] {chainId,assetChainId,assetId,address}, AccountInfoVo.class);
        System.out.println(result.toString());
    }

    @Test
    public  void getAccountList() throws Throwable{
        Page result = memberClient.invoke("getAccountList", new Object[] {chainId,pageNumber,pageSize}, Page.class);
        System.out.println(result);
    }

    @Test
    public  void exportAccountKeyStore() throws Throwable{
        String filePath="D:\\10.3.2.244\\test";
        Map result = memberClient.invoke("exportAccountKeyStore", new Object[] {chainId,address,password,filePath}, Map.class);
        System.out.println(result.get("path"));
    }

    @Test
    public  void importAccountByKeystore() throws Throwable{
        AccountKeyStoreDto keyStoreDto = new AccountKeyStoreDto();
        keyStoreDto.setAddress("tNULSeBaMvEtDfvZuukDf2mVyfGo3DdiN8KLRG");
        keyStoreDto.setPubKey("03958b790c331954ed367d37bac901de5c2f06ac8368b37d7bd6cd5ae143c1d7e3");
        keyStoreDto.setEncryptedPrivateKey("709c90bb90d2aea2fbfb16361630ecea8dfb5619151bcf11d04b085e92f50aa78063f3d6b4331e44c71b8255922b9047");
        //生成keystore HEX编码
        String keyStoreHex = HexUtil.encode(JSONUtils.obj2json(keyStoreDto).getBytes());
        Map result = memberClient.invoke("importAccountByKeystore", new Object[] {chainId,password,keyStoreHex,true}, Map.class);
        System.out.println(result.get("address"));
    }

    @Test
    public  void exportPriKeyByAddress() throws Throwable{
        Map result = memberClient.invoke("exportPriKeyByAddress", new Object[] {chainId,address,password}, Map.class);
        System.out.println(result.get("privateKey"));
    }

    @Test
    public  void importAccountByPriKey() throws Throwable{
        //上一步的输出值
        String priKey="9ce21dad67e0f0af2599b41b515a7f7018059418bab892a7b68f283d489abc4b";
        Map result = memberClient.invoke("importAccountByPriKey", new Object[] {chainId,priKey,password,true}, Map.class);
        System.out.println(result.get("address"));
    }


}
