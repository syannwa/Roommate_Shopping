package edu.uga.cs.roommateshopping;

public class Payment {

    String person;
    Double payment;

    public Payment() {
        this.person = "" ;
        this.payment = 0.0;
    }

    public Payment(String person, Double payment) {
        this.person = person;
        this.payment = payment;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public Double getPayment() {
        return payment;
    }

    public void setPayment(Double payment) {
        this.payment = payment;
    }
}
