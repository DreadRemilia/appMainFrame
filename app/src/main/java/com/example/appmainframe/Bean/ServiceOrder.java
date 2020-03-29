package com.example.appmainframe.Bean;

import java.util.Date;

public class ServiceOrder {
    private String serviceNo;
    private String serviceName;
    private String serviceType;
    private String serviceStart;
    private String serviceEnd;
    private String serviceProvider;
    private String serviceCustomer;
    private String serviceCity;
    private String serviceState;
    private String servicePrice;
    private String serviceAddress;
    private String serviceMarks;
    private String msg;

    public void setServiceMarks(String serviceMarks) {
        this.serviceMarks = serviceMarks;
    }

    public String getServiceMarks() {
        return serviceMarks;
    }

    public void setServiceAddress(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    public String getServiceAddress() {
        return serviceAddress;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    public String getMsg() {
        return msg;
    }

    public void setServicePrice(String servicePrice) {
        this.servicePrice = servicePrice;
    }

    public String getServicePrice() {
        return servicePrice;
    }

    public void setServiceState(String serviceState) {
        this.serviceState = serviceState;
    }

    public String getServiceState() {
        return serviceState;
    }

    public void setServiceStart(String serviceStart) {
        this.serviceStart = serviceStart;
    }

    public void setServiceEnd(String serviceEnd) {
        this.serviceEnd = serviceEnd;
    }

    public String getServiceStart() {
        return serviceStart;
    }

    public String getServiceEnd() {
        return serviceEnd;
    }



    public void setServiceCity(String serviceCity) {
        this.serviceCity = serviceCity;
    }

    public String getServiceCity() {
        return serviceCity;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public void setServiceNo(String serviceNo) {
        this.serviceNo = serviceNo;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }


    public void setServiceProvider(String serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public void setServiceCustomer(String serviceCustomer) {
        this.serviceCustomer = serviceCustomer;
    }

    public String getServiceNo() {
        return serviceNo;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getServiceType() {
        return serviceType;
    }

    public String getServiceProvider() {
        return serviceProvider;
    }

    public String getServiceCustomer() {
        return serviceCustomer;
    }
}
