package com.example.diploma;

public class HistoryForDocNotes {

    private int id;
    private int PatientId;
    private String date;
    private String docName;
    private String department;

    public HistoryForDocNotes(int id, int patientId, String date, String docName, String department) {
        this.id = id;
        PatientId = patientId;
        this.date = date;
        this.docName = docName;
        this.department = department;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPatientId() {
        return PatientId;
    }

    public void setPatientId(int patientId) {
        PatientId = patientId;
    }
}
