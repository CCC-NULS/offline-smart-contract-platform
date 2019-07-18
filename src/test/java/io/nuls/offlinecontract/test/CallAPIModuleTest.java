package io.nuls.offlinecontract.test;

import com.googlecode.jsonrpc4j.JsonRpcClientException;
import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import io.nuls.contract.model.vo.AccountInfoVo;

import java.net.URL;

public class CallAPIModuleTest {
    private static JsonRpcHttpClient memberClient;
    private static final long test_memberId = 1L;

    public CallAPIModuleTest() throws Throwable {
        memberClient = new JsonRpcHttpClient(new URL("http://127.0.0.1:18003"));
    }

    public static void main(String[] args) {
        try {
            CallAPIModuleTest test = new CallAPIModuleTest();
            test.testRPC();
        }catch (Exception e){
            e.printStackTrace();
        }catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void testRPC()throws Throwable {
        try{

        AccountInfoVo result = memberClient.invoke("getAccount", new Object[] {2,"tNULSeBaMnrs6JKrCy6TQdzYJZkMZJDng7QAsD"}, AccountInfoVo.class);

       // BalanceInfo result2 = memberClient.invoke("getAccountBalance", new Object[] {2,1,"tNULSeBaMnrs6JKrCy6TQdzYJZkMZJDng7QAsD"}, BalanceInfo.class);
    //    BalanceInfo result3 = memberClient.invoke("getAccountBalance", new Object[] {2,1,"tNULSeBaMnrs6JKrCy6TQdzYJZkMZJDng7QAsD"}, BalanceInfo.class);
        System.out.println(result.toString());
    //    System.out.println(result3.toString());

        }catch (JsonRpcClientException e){
            System.out.println(e.getCode()+"-----"+e.getData()+"----"+e.getMessage());
        }
    }
}
