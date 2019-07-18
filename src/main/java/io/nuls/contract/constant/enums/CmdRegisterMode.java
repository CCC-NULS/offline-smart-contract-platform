package io.nuls.contract.constant.enums;

import java.util.HashMap;
import java.util.Map;

public enum CmdRegisterMode {
    // 0 - 创建新交易, 1 - 数据查询
    NEW_TX(0),
    QUERY_DATA(1);

    private int mode;
    private static Map<Integer, CmdRegisterMode> map;

    private CmdRegisterMode(int mode) {
        this.mode = mode;
        putMode(mode, this);
    }

    public int mode() {
        return mode;
    }

    private static CmdRegisterMode putMode(int mode, CmdRegisterMode modeEnum) {
        if(map == null) {
            map = new HashMap<>(8);
        }
        return map.put(mode, modeEnum);
    }

    public static CmdRegisterMode getMode(int mode) {
        CmdRegisterMode cmdRegisterMode = map.get(mode);
        if(cmdRegisterMode == null) {
            throw new RuntimeException(String.format("not support cmd register mode - [%s] ", mode));
        }
        return cmdRegisterMode;
    }
}
