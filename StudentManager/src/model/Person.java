package model;

import java.time.LocalDate;

public abstract class Person {
    protected String fullName;
    protected String email;
    protected LocalDate dateOfBirth;
    protected String gender;
    protected String phone;
    protected String address;

    public Person(String fullName, String email, LocalDate dateOfBirth, String gender, String phone, String address) {
        this.fullName = fullName;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.phone = phone;
        this.address = address;
    }

    // --- Getters và Setters ---
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

    // Phương thức trừu tượng thể hiện tính đa hình (Polymorphism)
    public abstract String getRole();
}