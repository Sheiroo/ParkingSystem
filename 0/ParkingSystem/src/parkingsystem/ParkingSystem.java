package parkingsystem;

import java.sql.*;
import java.util.*;
import java.sql.Date;
import java.sql.DriverManager;

public class ParkingSystem {

    public static long Cost(long millis){
        long h=0,m=0,s=0; 
        if(millis>=360000)
        {
            h=millis/3600000;
            if(millis-3600000>60000)
            {
                m=(millis-(3600000*h))/60000;
                s=(millis-((3600000*h)+(m*60000)))/1000;
            }
            else
            {
                s=(millis-(3600000*h))/1000;
            }
        }
        else
        {
            if(millis>60000)
            {
                m=millis/60000;
                s=(millis-60000)/1000;
            }
            else
            {
                s=millis/1000;
            }
        }
        if(m>1||s>1){
            h+=1;
        }
        long sum=h*15;
        return sum;
    }
    
    public static void main(String[] args) {
        Connection ConObj=null;
        
        Statement StaObj1=null;
        Statement StaObj2=null;
        
        ResultSet ResObj1=null;
        ResultSet ResObj2=null;
        
        String query1="SELECT * FROM admin";
        String query2="SELECT * FROM customer";
        
        try{
            ConObj=DriverManager.getConnection("jdbc:mysql://localhost:3306/parking?zeroDateTimeBehavior=CONVERT_TO_NULL","root","root1234");
            
            StaObj1=ConObj.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            StaObj2=ConObj.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            
            ResObj1=StaObj1.executeQuery(query1);
            ResObj2=StaObj2.executeQuery(query2);
            
            int size=30;
            int size2=10;
            Scanner input=new Scanner(System.in);
            
            customer[] tickets = new customer[size];
            for (int y = 0; y < size; y++) {
                 tickets[y] = new customer();
            }
            
            admin[] admins=new admin[size2];
            for (int y = 0; y < 10; y++) {
                 admins[y] = new admin();
            }
            
            int e=0;
            int id=0;
            String PlateNum="";
            int NumSpot = 0;
            long phone;
            long millis;
            Date transactionDate;
            
            String choice;

            while(ResObj2.next()){
                
                if(ResObj2.getBoolean("State")==false){
                    
                    id=ResObj2.getInt(1);
                    PlateNum=ResObj2.getString(2);
                    NumSpot=ResObj2.getInt(3);
                    transactionDate=ResObj2.getDate(4);
                    phone=ResObj2.getLong(9);
                    millis=ResObj2.getLong(5);
                    
                    tickets[NumSpot-1]=new customer(id,PlateNum, NumSpot,phone,transactionDate);
                    tickets[NumSpot-1].setMillis(millis);
                }
                else {
                   id=ResObj2.getInt(1); 
                }
                        
            }
            ResObj2.beforeFirst();
            
            
            String name="";
            String pass="";
            
            while(ResObj1.next()){
                name=ResObj1.getString(1);
                pass=ResObj1.getString(2);
                if(admins[e].getPass()==null){
                    admins[e].setName(name);
                    admins[e].setPass(pass);
                }
                e++;
            }
            ResObj1.beforeFirst();
            
            do{
                System.out.println("Customer Press 1 \nAdmin Press 2");
                int i;
                i=input.nextInt();
                switch(i){
                    case 1:{
                        System.out.println("Press 1 to print a ticket\nPress 2 to pay for parking");
                        i=input.nextInt();
                        switch(i){
                            case 1:{
                                System.out.println("choose a num of an available spot");
                                admin.print_totalSpots(tickets);
                                customer.entry(ResObj2, tickets, ++id);
                                break;
                            }
                            
                            case 2:{
                                customer.pay(ResObj2, tickets);
                                break;
                            }
                            default:{
                                 break;
                            }
                        }
                        break;
                    }
                    case 2:{
                        int z;
                        System.out.print("enter your name : ");
                        name=input.next();
                        System.out.print("enter your password : ");
                        pass=input.next();
                        if(admin.LogIn(ResObj1, name, pass)==true){
                            System.out.println("\nEnter the number of operation you want");
                            System.out.println("1-View total spots in parking\n2-Add ,update , delete users with different roles\n3-View shifts report with payment\n4-View parked cars report");
                            i = input.nextInt();
                            switch (i) {
                                case 1:{
                                    admin.print_totalSpots(tickets);
                                    
                                    break;
                                }
                                case 2:{
                                    System.out.println("Enter the number of operation you want\n1-Add user\n2-Update User\n3-Delete user");
                                    i=input.nextInt();
                                    switch(i){
                                        case 1:{
                                            System.out.print("enter new name : ");
                                            name = input.next();
                                            System.out.print("enter new password : ");
                                            pass = input.next();
                                            
                                            admin.addAdmin(ResObj1, admins, pass, name, ++e);
                                            
                                            ResObj1.beforeFirst();
                                            break;
                                        }
                                        case 2:{
                                            System.out.print("enter new password : ");
                                            pass=input.next();
                                            
                                            admin.updatepass(ResObj1, admins, pass, name);
                                            
                                            ResObj1.beforeFirst();
                                            break;
                                        }
                                        case 3:{
                                            System.out.print("enter your name : ");
                                            name = input.next();
                                            
                                            admin.deleteAdmin(ResObj1, name);
                                            
                                            ResObj1.beforeFirst();
                                            break;
                                        }
                                    }
                                    break;
                                }
                                case 3:{
                                    System.out.println("payment report: " + admin.Payment_Report(ResObj2));
                                    break;
                                }
                                case 4:{
                                    admin.cars_report(tickets);
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
                choice=input.next();
            }while(choice.equals("yes"));
            

            ResObj2.close();
            ResObj1.close();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
      static class customer {
        private int Id;
        private String PlateNum;
        private int NumSpot;
        private long phone;
        private boolean a;
        private long millis;
        Date transactionDate;
        customer(){
            a = false;
        }
        customer(int id, String platenumber, int numspot,long phone,Date transactionDate) {
            this.Id = id;
            this.PlateNum=platenumber;
            this.NumSpot = numspot;
            this.phone=phone;
            this.transactionDate=transactionDate;
            a = true;
        }
        public int getId(){
            return Id;
        }
        public String getPlateNum(){
            return PlateNum;
        }
        public int getNumSpot(){
            return NumSpot;
        }
        public boolean isavailable(){
            return a;
        }
        public void doavailable(){
            a=false;
        }
        public long getMillis(){
            return millis;
        }
        public void setId(int id){
            this.Id=id;
        }
        public void setPlateNum(String plateNum){
            this.PlateNum=plateNum;
        }
        public void setNumSpot(int numSpot){
            this.NumSpot=numSpot;
        }
        public void setMillis(long millis){
            this.millis=millis;
        }
        public Date getDate(){
            return transactionDate;
        }
        public void setDate(Date date){
            this.transactionDate=date;
        }
        public long getPhone(){
            return phone;
        }
        public void setPhone(int phone){
            this.phone=phone;
        }
        static void entry(ResultSet Resobj2, customer[] ticket,int newId){
            try{
                Scanner input = new Scanner(System.in);
                System.out.println("\nIf you will stay more than one hour choose a spot from 20 to 30");
                int user_input = input.nextInt();
                if(ticket[user_input-1].isavailable() == false){
                    System.out.println("Enter the plate Number of the car");
                    ticket[user_input-1].setPlateNum(input.next());
                    System.out.println("Enter the phone Number");
                    ticket[user_input-1].setPhone(input.nextInt());
                    ticket[user_input-1].setMillis(System.currentTimeMillis());
                    ticket[user_input - 1] = new customer(newId, ticket[user_input-1].getPlateNum(), user_input, ticket[user_input-1].getPhone(), new Date(ticket[user_input-1].getMillis()));
                    customer.print_ticket(ticket[user_input - 1]);
                    Resobj2.moveToInsertRow();
                    Resobj2.updateInt(1, newId);
                    Resobj2.updateString(2, ticket[user_input-1].getPlateNum());
                    Resobj2.updateInt(3, user_input);
                    Resobj2.updateLong(9, ticket[user_input-1].getPhone());
                    Resobj2.updateDate(4, ticket[user_input-1].getDate());
                    Resobj2.updateLong(5, System.currentTimeMillis());
                    Resobj2.insertRow();
                }
                else{
                    System.out.println("This spot is not available\n");
                }
            }
            catch(Exception exception){
                System.out.println(exception.getMessage());
            }
            
            
        }
        static void pay(ResultSet Resultset_for_customer, customer[] ticket){
            try{
                Scanner input = new Scanner(System.in);
                Resultset_for_customer.beforeFirst();
                System.out.print("enter your spot:");
                int customer_spot = input.nextInt();
                if(ticket[customer_spot-1].isavailable()==true){
                    ticket[customer_spot-1].doavailable();
                    while(Resultset_for_customer.next()){
                        if(Resultset_for_customer.getInt(3) == customer_spot && Resultset_for_customer.getBoolean(8) == false){
                            long millisecond_when_exit = System.currentTimeMillis();
                            Resultset_for_customer.updateBoolean(8, true);
                            Resultset_for_customer.updateLong(6, millisecond_when_exit);
                            Resultset_for_customer.updateLong(7, Cost(millisecond_when_exit - Resultset_for_customer.getLong(5)));
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
            System.out.println("----------------------------");
            System.out.println("             ticket");
            System.out.println("The Id of the ticket is : " +ticket.getId());
            System.out.println("The plate number of the car is : " +ticket.getPlateNum());
            System.out.println("The spot number of the car is : " +ticket.getNumSpot());
            System.out.println("The phone number is : " +ticket.getPhone());
            System.out.println("Date is : " +ticket.getDate().toString());
            System.out.println("----------------------------");
        }
    }
        static class admin{
            private String name;
            private String pass;
            admin(){}
            admin(String name,String pass){
                this.name=name;
                this.pass=pass;
            }
            public String getName(){
                return name;
            }
            public void setName(String name){
                this.name=name;
            }
            public String getPass(){
                return pass;
            }
            public void setPass(String pass){
                this.pass=pass;
            }
            static boolean LogIn(ResultSet Resobj1, String name, String pass){
                try{
                    while(Resobj1.next()){
                        if(name.equals(Resobj1.getString(1)) && pass.equals(Resobj1.getString(2))){
                            Resobj1.beforeFirst();
                            return true;
                        }
                    }

                    Resobj1.beforeFirst();
                }
                catch(Exception exception){
                    System.out.println(exception.getMessage());
                }
                return false;
            }
            static void print_totalSpots(customer[] spots){
                for (int i = 0; i < spots.length; i++) {
                    if (spots[i].isavailable() == false) {
                        System.out.print((i+1) + "  ");
                    }
                }
            }
            static void addAdmin(ResultSet Resultset_for_admin, admin[] admins,String adminPassword, String adminName, int i){
                try{
                    Resultset_for_admin.moveToInsertRow();
                    Resultset_for_admin.updateString(1, adminName);
                    Resultset_for_admin.updateString(2, adminPassword);
                    Resultset_for_admin.insertRow();

                    admins[i].setName(adminName);
                    admins[i].setPass(adminPassword);
                }
                catch(Exception exception){
                    System.out.println(exception.getMessage());
                }
            }
            static void updatepass(ResultSet Resobj1, admin[] admins,String password, String name){
                try{
                    for(int i = 0; admins[i].getPass() != null; i++){
                        if(admins[i].getName().equals(name)){
                            admins[i].setPass(password);
                        }                                         
                    }

                    while(Resobj1.next()){
                        if(name.equals(Resobj1.getString(1))){
                            Resobj1.updateString(2, password);
                            Resobj1.updateRow();
                        }
                    }
                }
                catch(Exception exception){
                    System.out.println(exception.getMessage());
                }
            }
            static void deleteAdmin(ResultSet Resobj1, String name){
                try{
                    while(Resobj1.next()){
                        if(name.equals(Resobj1.getString(1))){
                            Resobj1.deleteRow();
                        }
                    }
                }
                catch(Exception exception){
                    System.out.println(exception.getMessage());
                }
            }
            static int Payment_Report(ResultSet Resobj2){
                int totalCost=0;
                try{
                    Resobj2.beforeFirst();

                    while(Resobj2.next()){
                        if(Resobj2.getBoolean(8)==true){
                            totalCost+=Resobj2.getInt(7);
                        }
                    }
                }
                catch(Exception exception){
                    System.out.println(exception.getMessage());
                }
                return totalCost;
            }
            static void cars_report(customer[] ticket){
            System.out.println("----------------------------");
            System.out.println("id\tplate_num\tspot_num\tphone_num\tdate_of_entry\n");
            for (int i=0;i<ticket.length;i++) {
                if (ticket[i].isavailable()==true) {
                    System.out.println(ticket[i].getId()+"\t"+ticket[i].getPlateNum()+"\t\t"+ticket[i].getNumSpot()+"\t\t"+ticket[i].getPhone()+"\t\t"+ticket[i].getDate());
                }
            }
            System.out.println("----------------------------");
        }
        }
    
}
