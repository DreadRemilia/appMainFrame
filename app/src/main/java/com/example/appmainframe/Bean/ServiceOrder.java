package com.example.appmainframe.Bean;

import java.util.Date;

public class ServiceOrder {
    private String serviceNo;
    private String serviceName;
    private String serviceType;
    private Date serviceStart;
    private Date serviceEnd;
    private String serviceProvider;
    private String serviceCustomer;
    private String serviceCity;

    public void setServiceStart(Date serviceStart) {
        this.serviceStart = serviceStart;
    }

    public void setServiceEnd(Date serviceEnd) {
        this.serviceEnd = serviceEnd;
    }

    public Date getServiceStart() {
        return serviceStart;
    }

    public Date getServiceEnd() {
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
