package parkingsystem;

import java.sql.*;
import java.util.*;
import java.sql.Date;
import java.sql.DriverManager;

public class Customers extends Tickets {
    private String Plate_Number_of_car;
    private long phone_of_ticketOwner;

    Customers(){
    }

    Customers(int id_of_ticket, String Plate_Number_of_car, int spot_number_in_parking, long phone_of_ticketOwner, Date transactionDate) {
        this.setId_of_ticket(id_of_ticket);
        this.Plate_Number_of_car = Plate_Number_of_car;
        this.setSpot_number_in_parking(spot_number_in_parking);
        this.phone_of_ticketOwner = phone_of_ticketOwner;
        this.setDate_of_entry(transactionDate);
        this.doSpotUnavailable();
    }

    public String getPlate_Number_of_car(){
        return Plate_Number_of_car;
    }

    public void setPlate_Number_of_car(String Plate_Number_of_car){
        this.Plate_Number_of_car = Plate_Number_of_car;
    }

    public long getPhone_of_ticketOwner(){
        return phone_of_ticketOwner;
    }

    public void setPhone_of_ticketOwner(int phone_of_ticketOwner){
        this.phone_of_ticketOwner = phone_of_ticketOwner;
    }
}
