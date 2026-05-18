package com.ecommerce.model;

public class Address {
    private int     addressId;
    private int     userId;
    private String  recipient;
    private String  phone;
    private String  street;
    private String  district;
    private String  city;
    private boolean isDefault;

    public Address() {}

    public Address(int userId, String recipient, String phone,
                   String street, String district, String city) {
        this.userId    = userId;
        this.recipient = recipient;
        this.phone     = phone;
        this.street    = street;
        this.district  = district;
        this.city      = city;
    }

    public int     getAddressId()            { return addressId; }
    public void    setAddressId(int v)       { this.addressId = v; }
    public int     getUserId()               { return userId; }
    public void    setUserId(int v)          { this.userId = v; }
    public String  getRecipient()            { return recipient; }
    public void    setRecipient(String v)    { this.recipient = v; }
    public String  getPhone()               { return phone; }
    public void    setPhone(String v)        { this.phone = v; }
    public String  getStreet()              { return street; }
    public void    setStreet(String v)       { this.street = v; }
    public String  getDistrict()            { return district; }
    public void    setDistrict(String v)     { this.district = v; }
    public String  getCity()               { return city; }
    public void    setCity(String v)         { this.city = v; }
    public boolean isDefault()              { return isDefault; }
    public void    setDefault(boolean v)    { this.isDefault = v; }

    public String getFullAddress() {
        return street + ", " + district + ", " + city;
    }

    @Override
    public String toString() {
        return String.format("[%d] %s (%s) - %s%s",
                addressId, recipient, phone, getFullAddress(),
                isDefault ? " [Mặc định]" : "");
    }
}
