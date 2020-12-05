package edu.uga.cs.roommateshopping;

import java.text.DecimalFormat;

public class Payment {

    String person;
    String payment;

    DecimalFormat df = new DecimalFormat("###.##");

    public Payment() {
        this.person = "" ;
        this.payment = df.format(0.00);
    }

    public Payment(String person, Double payment) {
        this.person = person;
        this.payment = df.format(payment);
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(Double payment) {
        this.payment = df.format(payment);
    }
}
