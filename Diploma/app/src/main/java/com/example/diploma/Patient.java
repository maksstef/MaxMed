package com.example.diploma;

public class Patient {

    int PatientId;
    int UserId;
    public String BirthDate;
    public String Address;
    public String Phone;
    public String Email;

    public Patient(int patientId, int userId, String birthDate, String address, String phone, String email) {
        PatientId = patientId;
        UserId = userId;
        BirthDate = birthDate;
        Address = address;
        Phone = phone;
        Email = email;
    }

    public int getPatientId() {
        return PatientId;
    }

    public void setPatientId(int patientId) {
        PatientId = patientId;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public String getBirthDate() {
        return BirthDate;
    }

    public void setBirthDate(String birthDate) {
        BirthDate = birthDate;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

}
