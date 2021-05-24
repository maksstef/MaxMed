package com.example.diploma;

public class JournalNotes {

    private int id;
    private int DoctorId;
    private int PatientId;
    private String date;
    private String issue;
    private String decision;

    public JournalNotes(int id, int doctorId, int patientId, String date, String issue, String decision) {
        this.id = id;
        this.DoctorId = doctorId;
        this.PatientId = patientId;
        this.date = date;
        this.issue = issue;
        this.decision = decision;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public int getPatientId() {
        return PatientId;
    }

    public void setPatientId(int patientId) {
        PatientId = patientId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
