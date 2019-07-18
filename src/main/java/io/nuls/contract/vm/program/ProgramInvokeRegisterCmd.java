package io.nuls.contract.vm.program;

import io.nuls.contract.constant.enums.CmdRegisterMode;

import java.util.Map;

public class ProgramInvokeRegisterCmd {
    private String cmdName;
    private Map args;
    private CmdRegisterMode cmdRegisterMode;
    private ProgramNewTx programNewTx;

    public ProgramInvokeRegisterCmd() {
    }

    public ProgramInvokeRegisterCmd(String cmdName, Map args, CmdRegisterMode cmdRegisterMode) {
        this.cmdName = cmdName;
        this.args = args;
        this.cmdRegisterMode = cmdRegisterMode;
    }

    public String getCmdName() {
        return cmdName;
    }

    public void setCmdName(String cmdName) {
        this.cmdName = cmdName;
    }

    public Map getArgs() {
        return args;
    }

    public void setArgs(Map args) {
        this.args = args;
    }

    public CmdRegisterMode getCmdRegisterMode() {
        return cmdRegisterMode;
    }

    public void setCmdRegisterMode(CmdRegisterMode cmdRegisterMode) {
        this.cmdRegisterMode = cmdRegisterMode;
    }

    public ProgramNewTx getProgramNewTx() {
        return programNewTx;
    }

    public void setProgramNewTx(ProgramNewTx programNewTx) {
        this.programNewTx = programNewTx;
    }
}
