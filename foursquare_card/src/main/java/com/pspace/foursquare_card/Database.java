package com.pspace.foursquare_card;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Veroutis
 */
public class Database {
    private Connection conn;
    private String url;
    private String user;
    private String pass;

    
    public Database(String url,String database,String user,String pass)
    {
        this.url="jdbc:mysql://"+url+"/"+database;
        System.out.println(this.url);
        this.user=user;
        this.pass=pass;
    }
    
    public Connection connect()
    {
        try {
            try {
                Class.forName ("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            }
            conn =DriverManager.getConnection(url,user,pass);
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }

        
        return conn;
    }
    
    public void disconnect()
    {
        try {
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String get_token(String pspace_username) {
        Statement s1;
        String token = null;
        try {
            s1 = conn.createStatement();
            String query="SELECT access_token from p_access_tokens where pspace_username='"+pspace_username+"'";
            ResultSet rs=s1.executeQuery(query);
            if(rs.next())
                token=rs.getString("access_token");
        }catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return token;    
    }
    
    public String get_venue_id() {
        Statement s1;
        String venue = null;
        try {
            s1 = conn.createStatement();
            String query="SELECT venue_id from p_foursquare_admin";
            ResultSet rs=s1.executeQuery(query);
            if(rs.next())
                venue=rs.getString("venue_id");
        }catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return venue;    
    }
    
}
