package io.nuls.offlinecontract.test;

import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import io.nuls.contract.account.model.bo.ContractInfo;
import io.nuls.contract.model.vo.ContractInfoVo;
import io.nuls.core.parse.JSONUtils;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URL;
import java.util.Map;


public class RpcContractClientTest {
    private static JsonRpcHttpClient memberClient;
    private int chainId=2;
    private int assetChainId=2;
    private  int assetId=1;

    private String sender="tNULSeBaMvEtDfvZuukDf2mVyfGo3DdiN8KLRG";

    private String contractAddress_nrc = "tNULSeBaN55LK44ZAKhUL57Zsp1QtrY1yvjidT";

    //jar测试使用的合约地址
    //private String contractAddress="tNULSeBaN15fihAZMefPMNYJyPuvYHBA1iEuka";

    //IDEA环境测试使用的合约地址  tNULSeBaN2DRmmBXYfD8AbqSzqsGVDZrsdgwYj2
    private String contractAddress="tNULSeBaN51kPDEKmw1wTGgWwJ23bNg1W1i9Se";

    private String password="nuls123456";
    private String alias="contractalias";
    private long gasLimit=100000;

    private long price=25;
    private  String remark="test";
    @Before
    public void setUp() throws Throwable {
        memberClient = new JsonRpcHttpClient(new URL("http://127.0.0.1/offlineSmartContract"));
    }

    @Test
    public void createContract()throws Throwable {
      //  String contractCode=getContractCode("D:\\BlockChain-nuls\\Pocm-contract-beta\\pocmContract-version-beta1\\target\\pocmContract-test2.jar");
     //   Object[] args = new Object[]{contractAddress_nrc,12000, 2, 0.5, 2, false, "tNULSeBaMtEPLXxUgyfnBt9bpb5Xv84dyJV98p", "", ""};
       String contractCode= getContractCode();
      //  String remark = "POCM contract test - POCM_共识合约";
       Object[] args = new Object[]{"pocManager", "POCM", 100000000, 8, 5000, 5, 200, 5, true, "tNULSeBaMtEPLXxUgyfnBt9bpb5Xv84dyJV98p", "", "", "", ""};
        Map result=memberClient.invoke("createContract",new Object[]{chainId,assetChainId,assetId,sender,password,contractCode,alias,args,gasLimit,price,remark},Map.class);
        System.out.println(result);
    }

    @Test
    public void getContractConstructor()throws Throwable {
       // String contractCode=getContractCode("D:\\BlockChain-nuls\\Pocm-contract-beta\\pocmContract-version-beta1\\target\\pocmContract-test2.jar");
        String contractCode= getContractCode();
        BigInteger value= BigInteger.ZERO;
        Object[] args=new Object[]{};
        Map result=memberClient.invoke("getContractConstructor",new Object[]{chainId,contractCode},Map.class);
        System.out.println(result);
    }

    @Test
    public void getContract()throws Throwable {
        ContractInfoVo result=memberClient.invoke("getContract",new Object[]{chainId,contractAddress}, ContractInfoVo.class);
        System.out.println(result);
    }

    @Test
    public void callContractMethodNoArgs()throws Throwable {
        String methodName="depositForOwn";
        BigInteger value= BigInteger.valueOf(50000000);
        System.out.println( JSONUtils.obj2json(value).toString());
        Object[] args=new Object[]{};
        Map result=memberClient.invoke("callContract",new Object[]{chainId,assetChainId,assetId,sender,password,contractAddress,value,
                methodName,"",args,gasLimit,price,remark},Map.class);
        System.out.println(result);
    }

    @Test
    public void invokeContractViewMethod()throws Throwable {
        String methodName="totalDepositDetail";
        BigInteger value= BigInteger.ZERO;
        Object[] args=new Object[]{};
        Map result=memberClient.invoke("invokeContractViewMethod",new Object[]{chainId,contractAddress,methodName,"",args},Map.class);
        System.out.println(result);
    }

    @Test
    public void getContractMethodArgsTypes()throws Throwable {
      //  String methodName="totalDepositDetail";
        String methodName="depositForOther";
        Map result=memberClient.invoke("getContractMethodArgsTypes",new Object[]{chainId,contractAddress,methodName},Map.class);
        System.out.println(result);
    }

    @Test
    public void callContractMethodHavingArgs()throws Throwable {
        String methodName="quit";
        BigInteger value= BigInteger.ZERO;
        Object[] args=new Object[]{"1"};
        Map result=memberClient.invoke("callContract",new Object[]{chainId,assetChainId,assetId,sender,password,contractAddress,value,
                methodName,"",args,gasLimit,price,remark},Map.class);
        System.out.println(result);

    }
    @Test
    public void deleteContract()throws Throwable {
        Map result=memberClient.invoke("deleteContract",new Object[]{chainId,assetChainId,assetId,sender,password,contractAddress,remark},Map.class);
        System.out.println(result);
    }

    private String getContractCode(){
        String hexEncode="";
        try {
            InputStream jarFile = new FileInputStream(RpcContractClientTest.class.getResource("/pocmContract-test.jar").getFile());
            byte[] contractCode= IOUtils.toByteArray(jarFile);
            hexEncode= Hex.encodeHexString(contractCode);
        }catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(hexEncode);
        return hexEncode;
    }

    private String getContractCode(String sourcePath){
        String hexEncode="";
        try {
            InputStream jarFile = new FileInputStream(sourcePath);
            byte[] contractCode= IOUtils.toByteArray(jarFile);
            hexEncode= Hex.encodeHexString(contractCode);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return hexEncode;
    }

}
