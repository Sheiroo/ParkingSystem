package parkingsystem;

import java.sql.*;
import java.util.*;
import java.sql.Date;
import java.sql.DriverManager;

public class CustomerController {
    
    public static void entry(ResultSet Resultset_for_customer, Customers[] ticket,int user_input, String plateNumber, int phoneNumber){
            //System.out.println(Parking.print_totalAvailableSpots(ticket));
        try{
            Scanner input = new Scanner(System.in);


            if(ticket[user_input-1].isSpotAvailable() == false){//check if this spot is available
                ticket[user_input-1].setPlate_Number_of_car(plateNumber);
                ticket[user_input-1].setPhone_of_ticketOwner(phoneNumber);
                ticket[user_input-1].setMillisecond_when_enter(System.currentTimeMillis());
                Resultset_for_customer.last();
                ticket[user_input - 1] = new Customers(Resultset_for_customer.getInt(1)+1, ticket[user_input-1].getPlate_Number_of_car(), user_input, ticket[user_input-1].getPhone_of_ticketOwner(), new Date(ticket[user_input-1].getMillisecond_when_enter()));

                customerMethods_inDatabase.addCustomerInDatebase(Resultset_for_customer, ticket[user_input-1]);

            }
            else{
                System.out.println("This spot is not available\n");
            }
        }
        catch(Exception exception){
            System.out.println(exception.getMessage());
        }
    }

static String pay_theBill(ResultSet Resultset_for_customer, Customers[] ticket, int customer_spot){
        try{
            Resultset_for_customer.beforeFirst();
            while(Resultset_for_customer.next()){
                if(Resultset_for_customer.getInt(3) == customer_spot && Resultset_for_customer.getBoolean(8) == false){//check if this id is exist in parking
                    long millisecond_when_exit = System.currentTimeMillis();
                    Resultset_for_customer.updateBoolean(8, true);
                    Resultset_for_customer.updateLong(6, millisecond_when_exit);
                    Resultset_for_customer.updateLong(7, ParkingSystem.calculateExitCost(millisecond_when_exit - Resultset_for_customer.getLong(5)));
                    Resultset_for_customer.updateRow();
                    if(ticket[customer_spot-1].isSpotAvailable()==true){
                        ticket[customer_spot-1].doSpotAvailable();
                    }
                    break;
                }
                else{
                    if(Resultset_for_customer.isLast()){
                        System.out.println("this spot is empty");
                        return "this spot is empty";
                    }
                }
            } 
            return "The cost of your ticket : " + Resultset_for_customer.getLong(7) + " $";
        }
        catch(Exception exception){
            System.out.println(exception.getMessage());
        }
        return "";
    }
    
}
