package Bai10.FTPServer;

;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author TienDat
 */
public class DBAccess {

    private Connection con;
    private Statement stmt;

    public DBAccess() {
        try {
            MyConnection mycon = new MyConnection();
            con = mycon.getConnection();
            stmt = con.createStatement();
        } catch (SQLException e) {
        }
    }

    public int Update(String str) {
        try {
            int i = stmt.executeUpdate(str);
            return i;
        } catch (SQLException e) {
            return -1;
        }
    }

    public ResultSet Query(String srt) {
        try {
            ResultSet rs = stmt.executeQuery(srt);
            return rs;
        } catch (SQLException e) {
            return null;
        }
    }
}
