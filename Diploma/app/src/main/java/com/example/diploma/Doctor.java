package com.example.diploma;

public class Doctor {

    int DoctorId;
    int UserId;
    public String Building;
    public String Department;

    public Doctor(int doctorId, int userId, String building, String department) {
        DoctorId = doctorId;
        UserId = userId;
        Building = building;
        Department = department;
    }

    public int getDoctorId() {
        return DoctorId;
    }

    public void setDoctorId(int doctorId) {
        DoctorId = doctorId;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public String getBuilding() {
        return Building;
    }

    public void setBuilding(String building) {
        Building = building;
    }

    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String department) {
        Department = department;
    }
}
