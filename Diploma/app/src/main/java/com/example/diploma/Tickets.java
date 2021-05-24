package com.example.diploma;

class Tickets {

    private int id;
    private int doctorId;
    private int patientId;
    private String doctorName;
    private String Date;
    private String Time;

    Tickets(int id, int doctorId, int patientId, String doctorName, String date, String time) {
        this.id = id;
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.doctorName = doctorName;
        Date = date;
        Time = time;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        doctorId = doctorId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }
}
