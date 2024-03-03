package com.main.traveltour.configpayment.momo.models;

import com.main.traveltour.configpayment.momo.enums.ConfirmRequestType;

public class ConfirmResponse extends Response {
    private Long amount;
    private Long transId;
    private String requestId;
    private ConfirmRequestType requestType;
}
