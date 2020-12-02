package edu.uga.cs.roommateshopping;

import java.util.ArrayList;

/*
 * This class defines all variables of the user that will be added to the database
 */
public class User {
    public String firstName;
    public String lastName;
    public String email;
    public String room;
    public ArrayList<Item> purchasedList;

    // Default constructor required for calls to DataSnapshot
    public User() {
    }

    public User(String firstName, String lastName, String email, String room) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.room = room;
        this.purchasedList = new ArrayList<Item>();
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getRoom() {
        return room;
    }
}