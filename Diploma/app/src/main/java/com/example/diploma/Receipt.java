package com.example.diploma;

public class Receipt {

    int Id;
    int PatientId;
    String DrugName;
    String Date;

    public Receipt(int id, int patientId, String drugName, String date) {
        Id = id;
        PatientId = patientId;
        DrugName = drugName;
        Date = date;
    }
}
