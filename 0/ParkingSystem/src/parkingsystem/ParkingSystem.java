package parkingsystem;

import java.sql.*;
import java.util.*;
import java.sql.Date;
import java.sql.DriverManager;

public class ParkingSystem {
   
    public static long calculate_TimeSpent_inParking(long the_milliseconds_difference_between_entering_and_exiting){
        long hours_spent=0,minutes_spent=0,seconds_spent=0; 
        if(the_milliseconds_difference_between_entering_and_exiting>=360000)
        {
            hours_spent=the_milliseconds_difference_between_entering_and_exiting/3600000;
            if(the_milliseconds_difference_between_entering_and_exiting-3600000>60000)
            {
                minutes_spent=(the_milliseconds_difference_between_entering_and_exiting-(3600000*hours_spent))/60000;
                seconds_spent=(the_milliseconds_difference_between_entering_and_exiting-((3600000*hours_spent)+(minutes_spent*60000)))/1000;
            }
            else
            {
                seconds_spent=(the_milliseconds_difference_between_entering_and_exiting-(3600000*hours_spent))/1000;
            }
        }
        else
        {
            if(the_milliseconds_difference_between_entering_and_exiting>60000)
            {
                minutes_spent=the_milliseconds_difference_between_entering_and_exiting/60000;
                seconds_spent=(the_milliseconds_difference_between_entering_and_exiting-60000)/1000;
            }
            else
            {
                seconds_spent=the_milliseconds_difference_between_entering_and_exiting/1000;
            }
        }
        if(minutes_spent>1||seconds_spent>1){
            hours_spent+=1;
        }
        return hours_spent;
    }
    public static long calculateExitCost(long the_milliseconds_difference_between_entering_and_exiting){
        
        long exit_cost=calculate_TimeSpent_inParking(the_milliseconds_difference_between_entering_and_exiting)*15;
        return exit_cost;
        
    }       
    
    public static void main(String[] args) {
        Connection Connetion_Object;
        
        Statement Statement_for_admin;
        Statement Statement_for_customer;
        
        ResultSet Resultset_for_admin;
        ResultSet Resultset_for_customer;
        
        String admin_query="SELECT * FROM admin";
        String customer_query="SELECT * FROM customer";
        
        try{
            Connetion_Object=DriverManager.getConnection("jdbc:mysql://localhost:3306/parking?zeroDateTimeBehavior=CONVERT_TO_NULL","root","root1234");
            
            Statement_for_admin=Connetion_Object.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            Statement_for_customer=Connetion_Object.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            Resultset_for_admin=Statement_for_admin.executeQuery(admin_query);
            Resultset_for_customer=Statement_for_customer.executeQuery(customer_query);
            
            int num_of_tickets=30;
            int num_of_admins=10;
            Scanner input=new Scanner(System.in);
            
            customer[] tickets = new customer[num_of_tickets];
            for (int index_of_customerArray = 0; index_of_customerArray < num_of_tickets; index_of_customerArray++) {
                 tickets[index_of_customerArray] = new customer();
            }
            
            admin[] admins=new admin[num_of_admins];
            for (int index_of_adminArray = 0; index_of_adminArray < 10; index_of_adminArray++) {
                 admins[index_of_adminArray] = new admin();
            }
            
            int index_of_adminsArray=0;
            int id_of_ticket=0;
            String plate_number_of_car="";
            int spot_number__in_parking = 0;
            long phone_of_ticketOwner;
            long millisecond_when_enter;
            Date date_of_entry;
            
            String choiceOfContinuityOfProgram;

            while(Resultset_for_customer.next()){
                
                if(Resultset_for_customer.getBoolean("State")==false){
                    
                    id_of_ticket=Resultset_for_customer.getInt(1);
                    plate_number_of_car=Resultset_for_customer.getString(2);
                    spot_number__in_parking=Resultset_for_customer.getInt(3);
                    date_of_entry=Resultset_for_customer.getDate(4);
                    phone_of_ticketOwner=Resultset_for_customer.getLong(9);
                    millisecond_when_enter=Resultset_for_customer.getLong(5);
                    
                    tickets[spot_number__in_parking-1]=new customer(id_of_ticket,plate_number_of_car, spot_number__in_parking,phone_of_ticketOwner,date_of_entry);
                    tickets[spot_number__in_parking-1].setMillisecond_when_enter(millisecond_when_enter);
                }
                else {
                   id_of_ticket=Resultset_for_customer.getInt(1); 
                }
                        
            }
            Resultset_for_customer.beforeFirst();
            
            
            String adminName="";
            String adminPassword="";
            
            while(Resultset_for_admin.next()){
                adminName=Resultset_for_admin.getString(1);
                adminPassword=Resultset_for_admin.getString(2);
                if(admins[index_of_adminsArray].getAdminPassword()==null){
                    admins[index_of_adminsArray].setAdminName(adminName);
                    admins[index_of_adminsArray].setAdminPassword(adminPassword);
                }
                index_of_adminsArray++;
            }
            Resultset_for_admin.beforeFirst();
            
            do{
                System.out.println("Customer Press 1 \nAdmin Press 2");
                int user_input;
                user_input=input.nextInt();
                switch(user_input){
                    case 1:{
                        System.out.println("Press 1 to print a ticket\nPress 2 to pay for parking");
                        user_input=input.nextInt();
                        switch(user_input){
                            case 1:{
                                System.out.println("choose a num of an available spot");
                                admin.print_totalAvailableSpots(tickets);
                                customer.entry(Resultset_for_customer, tickets, ++id_of_ticket);
                                break;
                            }
                            
                            case 2:{
                                customer.pay_theBill(Resultset_for_customer, tickets);
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
                        adminName=input.next();
                        System.out.print("enter your password : ");
                        adminPassword=input.next();
                        boolean isAdminExist=false;
                        while(Resultset_for_admin.next()){
                            if(adminName.equals(Resultset_for_admin.getString(1)) && adminPassword.equals(Resultset_for_admin.getString(2))){
                                isAdminExist=true;
                                break;
                            }
                        }
                        Resultset_for_admin.beforeFirst();
                        if(admin.LogIn(Resultset_for_admin, adminName, adminPassword)==true){
                            System.out.println("\nEnter the number of operation you want");
                            System.out.println("1-View total spots in parking\n2-Add ,update , delete users with different roles\n3-View shifts reports with payment\n4-View parked cars report");
                            user_input = input.nextInt();
                            switch (user_input) {
                                case 1:{
                                    admin.print_totalAvailableSpots(tickets);
                                    
                                    break;
                                }
                                case 2:{
                                    System.out.println("Enter the number of operation you want\n1-Add user\n2-Update User\n3-Delete user");
                                    user_input=input.nextInt();
                                    switch(user_input){
                                        case 1:{
                                            System.out.print("enter new name : ");
                                            adminName = input.next();
                                            System.out.print("enter new password : ");
                                            adminPassword = input.next();
                                            
                                            admin.addAdmin(Resultset_for_admin, admins, new admin(adminName, adminPassword), ++index_of_adminsArray);
                                            
                                            Resultset_for_admin.beforeFirst();
                                            
                                            break;
                                        }
                                        case 2:{
                                            System.out.print("enter new password : ");
                                            adminPassword=input.next();
                                            
                                            admin.updateAdminPassword(Resultset_for_admin, admins, new admin(adminName, adminPassword));
                                            
                                            Resultset_for_admin.beforeFirst();
                                            break;
                                        }
                                        case 3:{
                                            System.out.print("enter your name : ");
                                            adminName = input.next();
                                            
                                            admin.deleteAdmin(Resultset_for_admin, adminName);
                                            
                                            Resultset_for_admin.beforeFirst();
                                            break;
                                        }
                                    }
                                    break;
                                }
                                case 3:{
                                    System.out.println("payment report: " + admin.Payment_Report(Resultset_for_customer));
                                    
                                    break;
                                }
                                case 4:{
                                    admin.parkedCars_Report(tickets);
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
                choiceOfContinuityOfProgram=input.next();
            }while(choiceOfContinuityOfProgram.equals("yes"));
            

            Resultset_for_customer.close();
            Resultset_for_admin.close();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    static class customer {
        private int id_of_ticket;
        private String Plate_Number_of_car;
        private int spot_number_in_parking;
        private long phone_of_ticketOwner;
        private boolean state_of_spot;
        private long millisecond_when_enter;
        private Date date_of__entry;
        customer(){
            state_of_spot = false;
        }
        customer(int id_of_ticket, String Plate_Number_of_car, int spot_number_in_parking,long phone_of_ticketOwner,Date transactionDate) {
            this.id_of_ticket = id_of_ticket;
            this.Plate_Number_of_car=Plate_Number_of_car;
            this.spot_number_in_parking = spot_number_in_parking;
            this.phone_of_ticketOwner=phone_of_ticketOwner;
            this.date_of__entry=transactionDate;
            state_of_spot = true;
        }
        public int getId_of_ticket(){
            return id_of_ticket;
        }
        public String getPlate_Number_of_car(){
            return Plate_Number_of_car;
        }
        public int getSpot_number_in_parking(){
            return spot_number_in_parking;
        }
        public boolean isSpotAvailable(){
            return state_of_spot;
        }
        public void doSpotAvailable(){
            state_of_spot=false;
        }
        public long getMillisecond_when_enter(){
            return millisecond_when_enter;
        }
        public void setId_of_ticket(int id_of_ticket){
            this.id_of_ticket=id_of_ticket;
        }
        public void setPlate_Number_of_car(String Plate_Number_of_car){
            this.Plate_Number_of_car=Plate_Number_of_car;
        }
        public void setSpot_number_in_parking(int spot_number_in_parking){
            this.spot_number_in_parking=spot_number_in_parking;
        }
        public void setMillisecond_when_enter(long millisecond_when_enter){
            this.millisecond_when_enter=millisecond_when_enter;
        }
        public Date getDate_of_entry(){
            return date_of__entry;
        }
        public void setDate_of_entry(Date date_of__entry){
            this.date_of__entry=date_of__entry;
        }
        public long getPhone_of_ticketOwner(){
            return phone_of_ticketOwner;
        }
        public void setPhone_of_ticketOwner(int phone_of_ticketOwner){
            this.phone_of_ticketOwner=phone_of_ticketOwner;
        }
        static void entry(ResultSet Resultset_for_customer, customer[] ticket,int newId){
            try{
                Scanner input = new Scanner(System.in);
                System.out.println("\nIf you will stay more than one hour choose a spot from 20 to 30");
                int user_input = input.nextInt();
                if(ticket[user_input-1].isSpotAvailable() == false){
                    System.out.println("Enter the plate Number of the car");
                    ticket[user_input-1].setPlate_Number_of_car(input.next());
                    System.out.println("Enter the phone Number");
                    ticket[user_input-1].setPhone_of_ticketOwner(input.nextInt());
                    ticket[user_input-1].setMillisecond_when_enter(System.currentTimeMillis());
                    ticket[user_input - 1] = new customer(newId, ticket[user_input-1].getPlate_Number_of_car(), user_input, ticket[user_input-1].getPhone_of_ticketOwner(), new Date(ticket[user_input-1].getMillisecond_when_enter()));
                    customer.print_ticket(ticket[user_input - 1]);
                    Resultset_for_customer.moveToInsertRow();
                    Resultset_for_customer.updateInt(1, newId);
                    Resultset_for_customer.updateString(2, ticket[user_input-1].getPlate_Number_of_car());
                    Resultset_for_customer.updateInt(3, user_input);
                    Resultset_for_customer.updateLong(9, ticket[user_input-1].getPhone_of_ticketOwner());
                    Resultset_for_customer.updateDate(4, ticket[user_input-1].getDate_of_entry());
                    Resultset_for_customer.updateLong(5, System.currentTimeMillis());
                    Resultset_for_customer.insertRow();
                }
                else{
                    System.out.println("This spot is not available\n");
                }
            }
            catch(Exception exception){
                System.out.println(exception.getMessage());
            }
        }
        static void pay_theBill(ResultSet Resultset_for_customer, customer[] ticket){
            try{
                Scanner input = new Scanner(System.in);
                Resultset_for_customer.beforeFirst();
                System.out.print("enter your spot:");
                int customer_spot = input.nextInt();
                if(ticket[customer_spot-1].isSpotAvailable()==true){
                    ticket[customer_spot-1].doSpotAvailable();
                    while(Resultset_for_customer.next()){
                        if(Resultset_for_customer.getInt(3) == customer_spot && Resultset_for_customer.getBoolean(8) == false){
                            long millisecond_when_exit = System.currentTimeMillis();
                            Resultset_for_customer.updateBoolean(8, true);
                            Resultset_for_customer.updateLong(6, millisecond_when_exit);
                            Resultset_for_customer.updateLong(7, calculateExitCost(millisecond_when_exit - Resultset_for_customer.getLong(5)));
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
            }
            catch(Exception exception){
                System.out.println(exception.getMessage());
            }
        }
        private static void print_ticket(customer ticket) {
            System.out.println("\n----------------------------");
            System.out.println("\t Ticket");
            System.out.println("The Id of the ticket is : " + ticket.getId_of_ticket());
            System.out.println("The plate number of the car is : " + ticket.getPlate_Number_of_car());
            System.out.println("The spot number of the car is : " + ticket.getSpot_number_in_parking());
            System.out.println("The phone number is : " + ticket.getPhone_of_ticketOwner());
            System.out.println("Date is : " + ticket.getDate_of_entry().toString());
            System.out.println("----------------------------");
        }
    }
    static class admin{
            private String adminName;
            private String adminPassword;
            admin(){}
            admin(String adminName,String adminPassword){
                this.adminName=adminName;
                this.adminPassword=adminPassword;
            }
            public String getAdminName(){
                return adminName;
            }
            public void setAdminName(String adminName){
                this.adminName=adminName;
            }
            public String getAdminPassword(){
                return adminPassword;
            }
            public void setAdminPassword(String adminPassword){
                this.adminPassword=adminPassword;
            }
            static boolean LogIn(ResultSet Resultset_for_admin, String adminName, String adminPassword){
                try{
                    while(Resultset_for_admin.next()){
                        if(adminName.equals(Resultset_for_admin.getString(1)) && adminPassword.equals(Resultset_for_admin.getString(2))){
                            Resultset_for_admin.beforeFirst();
                            return true;
                        }
                    }

                    Resultset_for_admin.beforeFirst();
                }
                catch(Exception exception){
                    System.out.println(exception.getMessage());
                }
                return false;
            }
            static void print_totalAvailableSpots(customer[] spots){
                for (int index_of_ticketsArray = 0; index_of_ticketsArray < spots.length; index_of_ticketsArray++) {
                    if (spots[index_of_ticketsArray].isSpotAvailable() == false) {//check if this spot is available
                        System.out.print((index_of_ticketsArray+1) + "  ");
                    }
                }
            }
            static void addAdmin(ResultSet Resultset_for_admin, admin[] admins, admin newAdmin, int index_of_adminsArray){
                try{
                    Resultset_for_admin.moveToInsertRow();
                    Resultset_for_admin.updateString(1, newAdmin.getAdminName());
                    Resultset_for_admin.updateString(2, newAdmin.getAdminPassword());
                    Resultset_for_admin.insertRow();

                    admins[index_of_adminsArray].setAdminName(newAdmin.getAdminName());
                    admins[index_of_adminsArray].setAdminPassword(newAdmin.getAdminPassword());
                }
                catch(Exception exception){
                    System.out.println(exception.getMessage());
                }
            }
            static void updateAdminPassword(ResultSet Resultset_for_admin, admin[] admins, admin newAdmin){
            try{
                for(int index_of_adminsArray = 0; admins[index_of_adminsArray].getAdminPassword() != null; index_of_adminsArray++){
                    if(admins[index_of_adminsArray].getAdminName().equals(newAdmin.getAdminName())){
                        admins[index_of_adminsArray].setAdminPassword(newAdmin.getAdminPassword());
                    }                                         
                }
                                            
                while(Resultset_for_admin.next()){
                    if(newAdmin.getAdminName().equals(Resultset_for_admin.getString(1))){
                        Resultset_for_admin.updateString(2, newAdmin.getAdminPassword());
                        Resultset_for_admin.updateRow();
                    }
                }
            }
            catch(Exception exception){
                System.out.println(exception.getMessage());
            }
        }
            static void deleteAdmin(ResultSet Resultset_for_admin, String adminName){
                try{
                    while(Resultset_for_admin.next()){
                        if(adminName.equals(Resultset_for_admin.getString(1))){
                            Resultset_for_admin.deleteRow();
                        }
                    }
                }
                catch(Exception exception){
                    System.out.println(exception.getMessage());
                }
            }
            static int Payment_Report(ResultSet Resultset_for_customer){
                int totalCost=0;
                try{
                    Resultset_for_customer.beforeFirst();

                    while(Resultset_for_customer.next()){
                        if(Resultset_for_customer.getBoolean(8)==true){
                            totalCost+=Resultset_for_customer.getInt(7);
                        }
                    }
                }
                catch(Exception exception){
                    System.out.println(exception.getMessage());
                }
                return totalCost;
            }
            static void parkedCars_Report(customer[] ticket){
                System.out.println("----------------------------");
                System.out.println("id\tplate_num\tspot_num\tphone_num\tdate_of_entry\n");
                for (int index_of_ticketsArray=0;index_of_ticketsArray<ticket.length;index_of_ticketsArray++) {
                    if (ticket[index_of_ticketsArray].isSpotAvailable()==true) {
                        System.out.println(ticket[index_of_ticketsArray].getId_of_ticket()+"\t"+ticket[index_of_ticketsArray].getPlate_Number_of_car()+"\t\t"+ticket[index_of_ticketsArray].getSpot_number_in_parking()
                                          +"\t\t"+ticket[index_of_ticketsArray].getPhone_of_ticketOwner()+"\t\t"+ticket[index_of_ticketsArray].getDate_of_entry());
                    }
                }
                System.out.println("----------------------------");
            }
        }
}
