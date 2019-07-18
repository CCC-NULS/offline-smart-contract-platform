package io.nuls.offlinecontract.test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.nuls.contract.model.RpcResult;

import java.lang.reflect.Type;
import java.util.Map;

public class Test {

    public static void main(String[] args) {
       try{
           ObjectMapper mapper=new ObjectMapper();
         //  String json="{\"jsonrpc\":\"2.0\",\"id\":2103470749,\"result\":{\"address\":\"tNULSeBaMnrs6JKrCy6TQdzYJZkMZJDng7QAsD\",\"alias\":null,\"type\":1,\"txCount\":27,\"totalOut\":70800000,\"totalIn\":1000000060219650,\"consensusLock\":0,\"timeLock\":0,\"balance\":999999989419650,\"totalBalance\":999999989419650,\"totalReward\":1000000060219650,\"tokens\":[\"tNULSeBaNCHAhqG84z2kdeHx6AuFH6Zk6TmDDG,POCMTEST\"]}}\n";
            String json="{\"constructor\":{\"name\":\"<init>\",\"desc\":\"(String tokenAddress, BigDecimal price, int awardingCycle, BigDecimal minimumDepositNULS, int minimumLocked, boolean openConsensus, String packingAddress, String rewardHalvingCycle, String maximumDepositAddressCount) return void\",\"args\":[{\"type\":\"String\",\"name\":\"tokenAddress\",\"required\":true},{\"type\":\"BigDecimal\",\"name\":\"price\",\"required\":true},{\"type\":\"int\",\"name\":\"awardingCycle\",\"required\":true},{\"type\":\"BigDecimal\",\"name\":\"minimumDepositNULS\",\"required\":true},{\"type\":\"int\",\"name\":\"minimumLocked\",\"required\":true},{\"type\":\"boolean\",\"name\":\"openConsensus\",\"required\":true},{\"type\":\"String\",\"name\":\"packingAddress\",\"required\":false},{\"type\":\"String\",\"name\":\"rewardHalvingCycle\",\"required\":false},{\"type\":\"String\",\"name\":\"maximumDepositAddressCount\",\"required\":false}],\"returnArg\":\"void\",\"view\":false,\"event\":false,\"payable\":false},\"isNrc20\":false}";
           JsonNode node= mapper.readTree(json);
           JsonParser parser =mapper.treeAsTokens(node);
           Map result =mapper.treeToValue(node, Map.class);
            Type type=Type.class.cast(Map.class);
            JavaType javaType=mapper.getTypeFactory().constructType(type);
           Map result2=mapper.readValue(parser, javaType);
           System.out.println(result2.toString());
       }catch (Exception e){

       }

    }
}
