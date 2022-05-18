package parkingsystem;

import java.sql.*;
import java.util.*;
import java.sql.Date;
import java.sql.DriverManager;
import javax.swing.*;
import java.awt.*;

public class ParkingSystem {

    
    public static long calculateExitCost(long the_milliseconds_difference_between_entering_and_exiting){
        long exit_cost = calculate_TimeSpent_inParking(the_milliseconds_difference_between_entering_and_exiting)*15;
        return exit_cost;
    }
    
    public static long calculate_TimeSpent_inParking(long the_milliseconds_difference_between_entering_and_exiting){
        long hours_spent = 0,minutes_spent = 0,seconds_spent = 0; 
        if(the_milliseconds_difference_between_entering_and_exiting >= 360000)
        {
            hours_spent = the_milliseconds_difference_between_entering_and_exiting / 3600000;
            if((the_milliseconds_difference_between_entering_and_exiting - 3600000) > 60000)
            {
                minutes_spent = (the_milliseconds_difference_between_entering_and_exiting - (3600000*hours_spent)) / 60000;
                seconds_spent = (the_milliseconds_difference_between_entering_and_exiting - ((3600000*hours_spent) + (minutes_spent*60000))) / 1000;
            }
            else
            {
                seconds_spent = (the_milliseconds_difference_between_entering_and_exiting - (3600000*hours_spent)) / 1000;
            }
        }
        else
        {
            if(the_milliseconds_difference_between_entering_and_exiting > 60000)
            {
                minutes_spent = the_milliseconds_difference_between_entering_and_exiting / 60000;
                seconds_spent = (the_milliseconds_difference_between_entering_and_exiting - 60000) / 1000;
            }
            else
            {
                seconds_spent = the_milliseconds_difference_between_entering_and_exiting / 1000;
            }
        }
        if((minutes_spent > 1) || (seconds_spent > 1)){
            hours_spent += 1;
        }
        return hours_spent;
    }
    
    public static void main(String[] args) {
        
        
        
        Connection Connetion_Object;
        
        Statement Statement_for_admin;
        Statement Statement_for_customer;
        
        ResultSet Resultset_for_admin;
        ResultSet Resultset_for_customer;
        
        String admin_query = "SELECT * FROM admin";
        String customer_query = "SELECT * FROM customer";
        
        try{
            //view bf = new view();
            
            Connetion_Object = DriverManager.getConnection("jdbc:mysql://localhost:3306/parking?zeroDateTimeBehavior=CONVERT_TO_NULL", "root", "root1234");
            
            Statement_for_admin = Connetion_Object.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            Statement_for_customer = Connetion_Object.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            
            Resultset_for_admin = Statement_for_admin.executeQuery(admin_query);
            Resultset_for_customer = Statement_for_customer.executeQuery(customer_query);
            
            int num_of_tickets = 30;
            int num_of_admins = 10;
            
            Scanner input = new Scanner(System.in);
            
            Customers[] tickets = new Customers[num_of_tickets];
            for (int index_of_customerArray = 0; index_of_customerArray < num_of_tickets; index_of_customerArray++) {
                tickets[index_of_customerArray] = new Customers();
            }
            
            Admins[] admin = new Admins[num_of_admins];
            for (int index_of_adminArray = 0; index_of_adminArray < 10; index_of_adminArray++) {
                admin[index_of_adminArray] = new Admins();
            }
            
            int index_of_adminsArray = 0;
            int id_of_ticket = 0;

            firstFrame frame = new firstFrame(admin, tickets, Resultset_for_customer, Resultset_for_admin);
            
            //take data from customer table
            Resultset_for_customer.beforeFirst();
            while(Resultset_for_customer.next()){
                
                if(Resultset_for_customer.getBoolean("State") == false){
                    tickets[Resultset_for_customer.getInt(3)-1] = new Customers(Resultset_for_customer.getInt(1), Resultset_for_customer.getString(2), 
                                                                        Resultset_for_customer.getInt(3),Resultset_for_customer.getLong(9), Resultset_for_customer.getDate(4));
                    tickets[Resultset_for_customer.getInt(3)-1].setMillisecond_when_enter(Resultset_for_customer.getLong(5));
                }
                else {
                    id_of_ticket = Resultset_for_customer.getInt(1); 
                }     
            }
            
            Resultset_for_customer.beforeFirst();
            
            String adminName = "";
            String adminPassword = "";
            
            //take data from admin table
            while(Resultset_for_admin.next()){
                if(admin[index_of_adminsArray].getAdminPassword() == null){
                    admin[index_of_adminsArray].setAdminName(Resultset_for_admin.getString(1));
                    admin[index_of_adminsArray].setAdminPassword(Resultset_for_admin.getString(2));
                    }
                
                index_of_adminsArray++;
            }
            Resultset_for_admin.beforeFirst();
            
            String choiceOfContinuityOfProgram;
            do{
                System.out.println("Customer Press 1 \nAdmin Press 2");
                int user_input = input.nextInt();
                
                switch(user_input){
                    case 1:{
                        System.out.println("Press 1 to print a ticket\nPress 2 to pay for parking");
                        user_input = input.nextInt();
                        
                        switch(user_input){
                            case 1:{
                                System.out.println("choose a num of an available spot");
                                System.out.println(Parking.print_totalAvailableSpots(tickets));
                                System.out.println("\nIf you will stay more than one hour choose a spot from 20 to 30");
                                user_input = input.nextInt();
                                System.out.println("Enter the plate Number of the car");
                                String plateNumber = input.next();
                                System.out.println("Enter the phone Number");
                                int phoneNumber = input.nextInt();
                                //print available spots to choose from them
                                String spots = Parking.print_totalAvailableSpots(tickets);
                                CustomerController.entry(Resultset_for_customer, tickets, user_input, plateNumber, phoneNumber);
                                System.out.println(Tickets.print(tickets[user_input-1]));
                                
                                break;
                            }
                            
                            case 2:{
                                System.out.print("enter your spot:");
                                user_input = input.nextInt();  
                                CustomerController.pay_theBill(Resultset_for_customer, tickets, user_input);
                                break;
                            }
                            default:{
                                 break;
                            }
                        }
                        break;
                    }
                    
                    case 2:{
                        System.out.print("enter your name : ");
                        adminName = input.next();
                        System.out.print("enter your password : ");
                        adminPassword = input.next();
                        
                        if(AdminController.LogIn(Resultset_for_admin, adminName, adminPassword) == true){
                            System.out.println("\nEnter the number of operation you want");
                            System.out.println("1-View total spots in parking\n2-Add, update ,delete users with different roles\n3-View shifts reports with payment\n4-View parked cars report");
                            user_input = input.nextInt();
                            
                            switch (user_input) {
                                case 1:{
                                    System.out.println("total available spot");
                                    System.out.println(Parking.print_totalAvailableSpots(tickets));
                                    break;
                                }
                                
                                case 2:{
                                    System.out.println("Enter the number of operation you want\n1-Add user\n2-Update User\n3-Delete user");
                                    user_input = input.nextInt();
                                    switch(user_input){
                                        case 1:{
                                            System.out.print("enter new name : ");
                                            adminName = input.next();
                                            System.out.print("enter new password : ");
                                            adminPassword = input.next();
                                            //adminController.addAdminInDatebase(Resultset_for_admin, new Admins(adminName, adminPassword));
                                            AdminController.addAdmin(Resultset_for_admin, admin, new Admins(adminName, adminPassword));
                                            
                                            Resultset_for_admin.beforeFirst();
                                            
                                            break;
                                        }
                                        
                                        case 2:{
                                            System.out.print("enter new password : ");
                                            adminPassword = input.next();
                                            
                                            AdminController.updateAdminPassword(Resultset_for_admin, admin, new Admins(adminName, adminPassword));
                                            
                                            Resultset_for_admin.beforeFirst();
                                            
                                            break;
                                        }
                                        
                                        case 3:{
                                            System.out.print("enter your name : ");
                                            adminName = input.next();
                                            
                                            AdminController.deleteAdmin(Resultset_for_admin, adminName);
                                            
                                            Resultset_for_admin.beforeFirst();
                                            
                                            break;
                                        }
                                    }
                                    break;
                                }
                                
                                case 3:{
                                    System.out.println("payment report: " + AdminController.Payment_Report(Resultset_for_customer));
                                    break;
                                }
                                
                                case 4:{
                                    System.out.println(Parking.parkedCars_Report(tickets));
                                    
                                    break;
                                }
                                
                                default:{
                                    break;
                                }
                            }
                        }
                        else{
                            System.out.println("acount is not existe");    
                        }
                        break;
                   }

                   default:{
                        break;
                   }
                }
                
                System.out.println("\nif you want another operation enter yes else enter no:");
                
                choiceOfContinuityOfProgram = input.next();
                if(choiceOfContinuityOfProgram.equals("no")){
                    Resultset_for_customer.close();
                    Resultset_for_admin.close();
                    System.exit(0);
                }
                
            }while(choiceOfContinuityOfProgram.equals("yes"));
            

        }
        catch(Exception exception){
            System.out.println(exception.getMessage());
        }
    }
}
