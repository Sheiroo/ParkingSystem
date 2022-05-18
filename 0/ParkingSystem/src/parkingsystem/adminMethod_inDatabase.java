package parkingsystem;

import java.sql.*;
import java.util.*;
import java.sql.Date;
import java.sql.DriverManager;

public class adminMethod_inDatabase {
    
    static void addAdminInDatebase(ResultSet Resultset_for_admin, Admins newAdmin){
        try{
            Resultset_for_admin.moveToInsertRow();
            Resultset_for_admin.updateString(1, newAdmin.getAdminName());
            Resultset_for_admin.updateString(2, newAdmin.getAdminPassword());
            Resultset_for_admin.insertRow();
        }
        catch(Exception exception){
            System.out.println(exception.getMessage());
        }
    }
    
    static void updatePasswordAdminInDatebase(ResultSet Resultset_for_admin, Admins newAdmin){
        try{
            Resultset_for_admin.beforeFirst();
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
            Resultset_for_admin.beforeFirst();
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
    
    static String Payment_Report(ResultSet Resultset_for_customer){
        int totalCost = 0;
        try{
            Resultset_for_customer.beforeFirst();

            while(Resultset_for_customer.next()){
                if(Resultset_for_customer.getBoolean(8) == true){//check if this customer went out 
                    totalCost+=Resultset_for_customer.getInt(7);
                }
            }
        }
        catch(Exception exception){
            System.out.println(exception.getMessage());
        }
        return Integer.toString(totalCost);
    } 
}
