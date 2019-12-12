package io.nuls.contract.utils;

import com.alibaba.fastjson.JSON;
import io.nuls.base.data.BlockExtendsData;
import io.nuls.base.data.BlockHeader;
import io.nuls.core.exception.NulsRuntimeException;
import io.nuls.core.log.Log;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static io.nuls.core.model.StringUtils.isBlank;

public class ContractUtil {
    public static String[][] twoDimensionalArray(Object[] args, String[] types) {
        if (args == null) {
            return null;
        } else {
            int length = args.length;
            String[][] two = new String[length][];
            Object arg;
            for (int i = 0; i < length; i++) {
                arg = args[i];
                if (arg == null) {
                    two[i] = new String[0];
                    continue;
                }
                if (arg instanceof String) {
                    String argStr = (String) arg;
                    // 非String类型参数，若传参是空字符串，则赋值为空一维数组，避免数字类型转化异常 -> 空字符串转化为数字
                    if (types != null && isBlank(argStr) && !"String".equalsIgnoreCase(types[i])) {
                        two[i] = new String[0];
                    } else {
                        two[i] = new String[]{argStr};
                    }
                } else if (arg.getClass().isArray()) {
                    int len = Array.getLength(arg);
                    String[] result = new String[len];
                    for (int k = 0; k < len; k++) {
                        result[k] = valueOf(Array.get(arg, k));
                    }
                    two[i] = result;
                } else if (arg instanceof ArrayList) {
                    ArrayList resultArg = (ArrayList) arg;
                    int size = resultArg.size();
                    String[] result = new String[size];
                    for (int k = 0; k < size; k++) {
                        result[k] = valueOf(resultArg.get(k));
                    }
                    two[i] = result;
                }  else {
                    two[i] = new String[]{valueOf(arg)};
                }
            }
            return two;
        }
    }


    public static String[][] twoDimensionalArray(Object[] args) {
        return twoDimensionalArray(args, null);
    }

    public static String valueOf(Object obj) {
        return (obj == null) ? null : obj.toString();
    }


    public static byte[] getStateRoot(BlockHeader blockHeader) {
        if (blockHeader == null || blockHeader.getExtend() == null) {
            return null;
        }
        byte[] stateRoot = blockHeader.getStateRoot();
        if (stateRoot != null && stateRoot.length > 0) {
            return stateRoot;
        }
        try {
            byte[] extend = blockHeader.getExtend();
            BlockExtendsData extendsData = new BlockExtendsData();
            extendsData.parse(extend, 0);
            stateRoot = extendsData.getStateRoot();
            blockHeader.setStateRoot(stateRoot);
            return stateRoot;
        } catch (Exception e) {
            Log.error("parse stateRoot error.", e);
        }
        return null;
    }

    public static String bigInteger2String(BigInteger bigInteger) {
        if (bigInteger == null) {
            return null;
        }
        return bigInteger.toString();
    }

    public static String simplifyErrorMsg(String errorMsg) {
        String resultMsg = "contract error - ";
        if (isBlank(errorMsg)) {
            return resultMsg;
        }
        if (errorMsg.contains("Exception:")) {
            String[] msgs = errorMsg.split("Exception:", 2);
            return resultMsg + msgs[1].trim();
        }
        return resultMsg + errorMsg;
    }

    public static String getContractCode(File file){
        String hexEncode="";
        try {
            InputStream jarFile = new FileInputStream(file);
            byte[] contractCode= IOUtils.toByteArray(jarFile);
            hexEncode= Hex.encodeHexString(contractCode);
        }catch (Exception e) {
            Log.error("parse contract code hex error.");
            throw new NulsRuntimeException(e);
        }
        return hexEncode;
    }

    public static Object[] convertArgsToObjectArray(Object[] args,String[] types){
        List<Object> newArgs= new ArrayList<>();
        try{
            for(int i=0;i<types.length;i++){
                if(types[i].contains("[]") && args[i] instanceof String){
                    newArgs.add(JSON.parseArray(String.valueOf(args[i]),Object.class));
                }else{
                    newArgs.add(args[i]);
                }
            }
        }catch (Exception e){
            Log.error("parse args error.", e);
            throw new NulsRuntimeException(e);
        }
        return newArgs.toArray();
    }

}
