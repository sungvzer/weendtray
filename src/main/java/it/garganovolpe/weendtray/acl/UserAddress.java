package it.garganovolpe.weendtray.acl;

/**
 * The UserAddress class represents the address information associated with a
 * user.
 * It can include details such as street, city, postal code, and country.
 */
public class UserAddress {
    private String address;
    private String city;
    private String country;
    private String postalCode;
    private String state;

    public UserAddress(String address, String city, String postalCode, String country, String state) {
        this.address = address;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
        this.state = state;
    }

    public String toString() {
        return "%s - %s %s (%s) - %s".formatted(address, postalCode, city, state, country);
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
