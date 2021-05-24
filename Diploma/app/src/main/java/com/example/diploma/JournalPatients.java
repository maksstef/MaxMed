package com.example.diploma;

public class JournalPatients {

    private int PatientId;
    private String PatientName;
    private String DateOfBirth;

    public JournalPatients(int patientId, String patientName, String dateOfBirth) {
        PatientId = patientId;
        PatientName = patientName;
        DateOfBirth = dateOfBirth;
    }

    public String getDateOfBirth() {
        return DateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        DateOfBirth = dateOfBirth;
    }

    public String getPatientName() {
        return PatientName;
    }

    public void setPatientName(String patientName) {
        PatientName = patientName;
    }

    public int getPatientId() {
        return PatientId;
    }

    public void setPatientId(int patientId) {
        PatientId = patientId;
    }



}
