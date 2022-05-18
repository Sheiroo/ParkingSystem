package parkingsystem;

import java.sql.*;
import java.util.*;
import java.sql.Date;
import java.sql.DriverManager;

public class Parking{
    private int spot_number_in_parking;
    private boolean state_of_spot;

    Parking(){
        state_of_spot = false;
    }

    public void setSpot_number_in_parking(int spot_number_in_parking){
        this.spot_number_in_parking = spot_number_in_parking;
    }

    public int getSpot_number_in_parking(){
        return spot_number_in_parking;
    }

    public boolean isSpotAvailable(){
        return state_of_spot;
    }

    public void doSpotAvailable(){
        state_of_spot = false;
    }

    public void doSpotUnavailable(){
        state_of_spot = true;
    }

    public static String print_totalAvailableSpots(Customers[] spots){
        String totalSpot = "";// to make new line in gui lable
        for (int index_of_ticketsArray = 0; index_of_ticketsArray < spots.length; index_of_ticketsArray++) {
            if (spots[index_of_ticketsArray].isSpotAvailable() == false) {//check if this spot is available
                totalSpot += (index_of_ticketsArray+1) + "  ";
                //System.out.print((index_of_ticketsArray+1) + "  ");
            }
            if((index_of_ticketsArray+1) % 10 == 0){
                totalSpot += "\n";
            }
        }
        totalSpot += "";
        return totalSpot;
    }

    public static String parkedCars_Report(Customers[] ticket){
        String report = "";
        report += "\t id    plateNum    spotNum    phoneNum    dateOfEntry";
        //System.out.println("id\tplate_num\tspot_num\tphone_num\tdate_of_entry\n");
        for (int index_of_ticketsArray = 0; index_of_ticketsArray < ticket.length; index_of_ticketsArray++) {
            if (ticket[index_of_ticketsArray].isSpotAvailable() == true) {
                report += "\n\t " + ticket[index_of_ticketsArray].getId_of_ticket() + "     " + ticket[index_of_ticketsArray].getPlate_Number_of_car() +
                         "      " + ticket[index_of_ticketsArray].getSpot_number_in_parking() + "         " +ticket[index_of_ticketsArray].getPhone_of_ticketOwner()
                         + "      " + ticket[index_of_ticketsArray].getDate_of_entry() + "\n";
            }
        }
        return report;
    }
}
