package parkingsystem;

import java.sql.*;
import java.util.*;
import java.sql.Date;
import java.sql.DriverManager;

public class Tickets extends Parking {
    private int id_of_ticket;
    private long millisecond_when_enter;
    private Date date_of__entry;

    public int getId_of_ticket(){
        return id_of_ticket;
    }

    public void setId_of_ticket(int id_of_ticket){
        this.id_of_ticket = id_of_ticket;
    }

    public long getMillisecond_when_enter(){
        return millisecond_when_enter;
    }

    public void setMillisecond_when_enter(long millisecond_when_enter){
        this.millisecond_when_enter = millisecond_when_enter;
    }

    public Date getDate_of_entry(){
        return date_of__entry;
    }

    public void setDate_of_entry(Date date_of__entry){
        this.date_of__entry = date_of__entry;
    }

    public static String print(Customers ticket) {
        String ticketReport = "\tTicket\nThe Id of the ticket is :" + ticket.getId_of_ticket() + 
                              "\nThe plate number of the car is : " + ticket.getPlate_Number_of_car() + "\nThe spot number of the car is : " 
                              + ticket.getSpot_number_in_parking() + "\nThe phone number is : " + ticket.getPhone_of_ticketOwner() 
                              + "\nDate is : " + ticket.getDate_of_entry().toString();
        
        return ticketReport;
    }
}
