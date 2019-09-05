package io.nuls.contract.model;

import io.nuls.core.constant.ErrorCode;

public interface RpcErrorCode {


    ErrorCode  DB_TABLE_CREATE_ERROR= ErrorCode.init("10001");
    ErrorCode  DB_SAVE_BATCH_ERROR= ErrorCode.init("10002");
    ErrorCode  DB_UPDATE_ERROR= ErrorCode.init("10003");
    ErrorCode  DB_QUERY_ERROR= ErrorCode.init("10004");
    ErrorCode DB_DELETE_ERROR = ErrorCode.init("10005");

    ErrorCode ADDRESS_ERROR= ErrorCode.init("20001");
    ErrorCode PASSWORD_IS_WRONG= ErrorCode.init("20002");
    ErrorCode PRIVATE_KEY_WRONG= ErrorCode.init("20003");
    ErrorCode VALIADE_PW_ERROR= ErrorCode.init("20004");
    ErrorCode ACCOUNT_EXIST= ErrorCode.init("20005");
    ErrorCode ACCOUNT_NOT_EXIST= ErrorCode.init("20006");
    ErrorCode ACCOUNT_IS_ALREADY_ENCRYPTED_AND_LOCKED= ErrorCode.init("20007");
    ErrorCode ACCOUNT_IS_ALREADY_ENCRYPTED= ErrorCode.init("20008");
    ErrorCode INSUFFICIENT_BALANCE= ErrorCode.init("20009");


    ErrorCode GET_CONSTRUSTOR_PARAMETER = ErrorCode.init("30001");
    ErrorCode BROADCAST_TX_ERROR= ErrorCode.init("30002");
    ErrorCode CONTRACT_TX_CREATE_ERROR= ErrorCode.init("30003");
    ErrorCode CONTRACT_VALIDATION_FAILED= ErrorCode.init("30004");
    ErrorCode GET_CONTRACT_INFO_FAILED= ErrorCode.init("30005");
    ErrorCode VALIADE_CONTRACT_CALL_ERROR= ErrorCode.init("30006");
    ErrorCode VALIADE_CONTRACT_DELETE_ERROR= ErrorCode.init("30007");

    ErrorCode PARAMETER_ERROR= ErrorCode.init("40001");
    ErrorCode NULL_PARAMETER = ErrorCode.init("40002");
    ErrorCode DESERIALIZE_ERROR= ErrorCode.init("40003");
    ErrorCode SIGNATURE_ERROR= ErrorCode.init("40004");
    ErrorCode IO_ERROR= ErrorCode.init("40005");
    ErrorCode PARSE_JSON_FAILD= ErrorCode.init("40006");
    ErrorCode FILE_OPERATION_FAILD= ErrorCode.init("40007");
    ErrorCode DATA_PARSE_ERROR = ErrorCode.init("40008");
    ErrorCode SYSTEM_ERROR = ErrorCode.init("40009");

    //api module exception
    ErrorCode NULS_SERVICE_ERROR = ErrorCode.init("50001");

}
