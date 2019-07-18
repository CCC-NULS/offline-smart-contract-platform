package io.nuls.contract.model;

import java.util.List;

public class ContractMethod {
    private String name;
    private String desc;
    private String returnType;
    private boolean view;
    private boolean payable;
    private List<ContractMethodArg> params;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public boolean isView() {
        return view;
    }

    public void setView(boolean view) {
        this.view = view;
    }

    public boolean isPayable() {
        return payable;
    }

    public void setPayable(boolean payable) {
        this.payable = payable;
    }

    public List<ContractMethodArg> getParams() {
        return params;
    }

    public void setParams(List<ContractMethodArg> params) {
        this.params = params;
    }
}
