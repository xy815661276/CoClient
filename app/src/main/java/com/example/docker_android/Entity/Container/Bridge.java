/**
  * Copyright 2020 bejson.com 
  */
package com.example.docker_android.Entity.Container;

public class Bridge {

    private String IPAMConfig;
    private String Links;
    private String Aliases;
    private String NetworkID;
    private String EndpointID;
    private String Gateway;
    private String IPAddress;
    private int IPPrefixLen;
    private String IPv6Gateway;
    private String GlobalIPv6Address;
    private int GlobalIPv6PrefixLen;
    private String MacAddress;
    private String DriverOpts;
    public void setIPAMConfig(String IPAMConfig) {
         this.IPAMConfig = IPAMConfig;
     }
     public String getIPAMConfig() {
         return IPAMConfig;
     }

    public void setLinks(String Links) {
         this.Links = Links;
     }
     public String getLinks() {
         return Links;
     }

    public void setAliases(String Aliases) {
         this.Aliases = Aliases;
     }
     public String getAliases() {
         return Aliases;
     }

    public void setNetworkID(String NetworkID) {
         this.NetworkID = NetworkID;
     }
     public String getNetworkID() {
         return NetworkID;
     }

    public void setEndpointID(String EndpointID) {
         this.EndpointID = EndpointID;
     }
     public String getEndpointID() {
         return EndpointID;
     }

    public void setGateway(String Gateway) {
         this.Gateway = Gateway;
     }
     public String getGateway() {
         return Gateway;
     }

    public void setIPAddress(String IPAddress) {
         this.IPAddress = IPAddress;
     }
     public String getIPAddress() {
         return IPAddress;
     }

    public void setIPPrefixLen(int IPPrefixLen) {
         this.IPPrefixLen = IPPrefixLen;
     }
     public int getIPPrefixLen() {
         return IPPrefixLen;
     }

    public void setIPv6Gateway(String IPv6Gateway) {
         this.IPv6Gateway = IPv6Gateway;
     }
     public String getIPv6Gateway() {
         return IPv6Gateway;
     }

    public void setGlobalIPv6Address(String GlobalIPv6Address) {
         this.GlobalIPv6Address = GlobalIPv6Address;
     }
     public String getGlobalIPv6Address() {
         return GlobalIPv6Address;
     }

    public void setGlobalIPv6PrefixLen(int GlobalIPv6PrefixLen) {
         this.GlobalIPv6PrefixLen = GlobalIPv6PrefixLen;
     }
     public int getGlobalIPv6PrefixLen() {
         return GlobalIPv6PrefixLen;
     }

    public void setMacAddress(String MacAddress) {
         this.MacAddress = MacAddress;
     }
     public String getMacAddress() {
         return MacAddress;
     }

    public void setDriverOpts(String DriverOpts) {
         this.DriverOpts = DriverOpts;
     }
     public String getDriverOpts() {
         return DriverOpts;
     }

}