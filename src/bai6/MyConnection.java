package bai6;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.sql.*;
import javax.swing.*;
/**
 *
 * @author TienDat
 */
public class MyConnection {
    public Connection getConnection(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            String URL = "jdbc:mysql://localhost:3308/qltaikhoan?user=root&password=";      
//            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/quanlytaikhoan?user=root&password=", "root", "123456789");
            Connection con = DriverManager.getConnection(URL);           
            return con;
        }
        catch(Exception ex){
            JOptionPane.showMessageDialog(null,ex.toString(),"loi",JOptionPane.ERROR_MESSAGE);
            return null;
        }
    } 
}
