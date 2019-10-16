package com.develeno.prestashopandroidclient;

public class Response {
    private int responseCode;
    private String value;

    public Response(int responseCode2, String value2) {
        this.responseCode = responseCode2;
        this.value = value2;
    }

    public int getResponseCode() {
        return this.responseCode;
    }

    public void setResponseCode(int responseCode2) {
        this.responseCode = responseCode2;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value2) {
        this.value = value2;
    }
}
