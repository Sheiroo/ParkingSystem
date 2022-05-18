package parkingsystem;

import java.sql.*;
import java.util.*;
import java.sql.Date;
import java.sql.DriverManager;


class Admins{
        private String adminName;
        private String adminPassword;
        
        Admins(){}
            
        Admins(String adminName, String adminPassword){
            this.adminName = adminName;
            this.adminPassword = adminPassword;
        }
        
        public String getAdminName(){
            return adminName;
        }
        
        public void setAdminName(String adminName){
            this.adminName = adminName;
        }
            
        public String getAdminPassword(){
            return adminPassword;
        }
        
        public void setAdminPassword(String adminPassword){
            this.adminPassword = adminPassword;
        }
    }
