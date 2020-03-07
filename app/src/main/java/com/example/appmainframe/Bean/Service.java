package com.example.appmainframe.Bean;

public class Service {
    private String serviceName;
    private String serviceSex;

    public Service(String serviceName,String serviceSex){
        serviceName = this.serviceName;
        serviceSex = this.serviceSex;
    }
    @Override
    public String toString() {
        return super.toString();
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setServiceSex(String serviceSex) {
        this.serviceSex = serviceSex;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getServiceSex() {
        return serviceSex;
    }
}
