package parkingsystem;

import java.sql.*;
import java.util.*;
import java.sql.Date;
import java.sql.DriverManager;

public class customerMethods_inDatabase {
    
    public static void addCustomerInDatebase(ResultSet Resultset_for_customer, Customers ticket){
        try{
            Resultset_for_customer.moveToInsertRow();
            Resultset_for_customer.updateInt(1, ticket.getId_of_ticket());
            Resultset_for_customer.updateString(2, ticket.getPlate_Number_of_car());
            Resultset_for_customer.updateInt(3, ticket.getSpot_number_in_parking());
            Resultset_for_customer.updateLong(9, ticket.getPhone_of_ticketOwner());
            Resultset_for_customer.updateDate(4, ticket.getDate_of_entry());
            Resultset_for_customer.updateLong(5, System.currentTimeMillis());
            Resultset_for_customer.insertRow();
        }
        catch(Exception exception){
            System.out.println(exception.getMessage());
        }
    }
    
    public static void customer_exit_fromDatabase(ResultSet Resultset_for_customer, int customer_spot){
        try{
            Resultset_for_customer.beforeFirst();
            while(Resultset_for_customer.next()){
                if(Resultset_for_customer.getInt(3) == customer_spot && Resultset_for_customer.getBoolean(8) == false){//check if this id is exist in parking
                    long millisecond_when_exit = System.currentTimeMillis();
                    Resultset_for_customer.updateBoolean(8, true);
                    Resultset_for_customer.updateLong(6, millisecond_when_exit);
                    //Resultset_for_customer.updateLong(7, calculateExitCost(millisecond_when_exit - Resultset_for_customer.getLong(5)));
                    Resultset_for_customer.updateRow();
                    System.out.println("cost: " + Resultset_for_customer.getLong(7) + " $");

                }
                else{
                    if(Resultset_for_customer.isLast()){
                        System.out.println("this id is not exist");
                        break;
                    }
                }
            } 
        }
        catch(Exception exception){
            System.out.println(exception.getMessage());
        }
    }
}
