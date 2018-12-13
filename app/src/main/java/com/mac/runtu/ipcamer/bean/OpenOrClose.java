package com.mac.runtu.ipcamer.bean;

public class OpenOrClose {

    private String category;
    private String equipmentCode;
    private String macAddress;
    private String password;

    public String getCategory()
    {
        return this.category;
    }

    public String getEquipmentCode()
    {
        return this.equipmentCode;
    }

    public String getMacAddress()
    {
        return this.macAddress;
    }

    public String getPassword()
    {
        return this.password;
    }

    public void setCategory(String paramString)
    {
        this.category = paramString;
    }

    public void setEquipmentCode(String paramString)
    {
        this.equipmentCode = paramString;
    }

    public void setMacAddress(String paramString)
    {
        this.macAddress = paramString;
    }

    public void setPassword(String paramString)
    {
        this.password = paramString;
    }
}
