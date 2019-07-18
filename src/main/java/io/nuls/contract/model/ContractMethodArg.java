package io.nuls.contract.model;

public class ContractMethodArg {
    private String type;

    private String name;

    private boolean required;

    public ContractMethodArg() {
    }

    public ContractMethodArg(String type, String name, boolean required) {
        this.type = type;
        this.name = name;
        this.required = required;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }
}
