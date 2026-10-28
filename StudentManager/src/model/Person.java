package model;

import java.io.Serializable;
import java.time.LocalDate;

public abstract class Person implements Serializable {
    protected String fullName;
    protected String email;
    private String gender;
    private String phone;
    private String address;
    private LocalDate dateOfBirth;

    public Person( String fullName, String email, LocalDate dateOfBirth, String gender, String phone, String address) {
        this.fullName = fullName;
        this.email = email;
        this.gender = gender;
        this.phone = phone;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
    }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public abstract String getRole();
}