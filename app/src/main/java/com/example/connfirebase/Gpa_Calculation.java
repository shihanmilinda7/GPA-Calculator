package com.example.connfirebase;

public class Gpa_Calculation {
    double val;

    public Double calculation(String result, double credit) {
        if (result.equals("A+")) {
            val = credit * 4.3;
        } else if (result.equals("A")) {
            val = credit * 4.0;
        } else if (result.equals("A-")) {
            val = credit * 3.7;
        } else if (result.equals("B+")) {
            val = credit * 3.3;
        } else if (result.equals("B")) {
            val = credit * 3.0;
        } else if (result.equals("B-")) {
            val = credit * 2.7;
        } else if (result.equals("C+")) {
            val = credit * 2.3;
        } else if (result.equals("C")) {
            val = credit * 2.0;
        } else if (result.equals("C-")) {
            val = credit * 1.7;
        } else if (result.equals("D+")) {
            val = credit * 1.3;
        } else if (result.equals("D")) {
            val = credit * 1.0;
        } else if (result.equals("D-")) {
            val = credit * 0.7;
        } else {
            val = credit * 0.0;
        }
        return val;
    }
}
