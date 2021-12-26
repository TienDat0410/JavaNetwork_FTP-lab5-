/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bai10.FTPServer;

import Bai10.FTPServer.DBAccess;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.util.Scanner;
import javax.swing.JOptionPane;

/**
 *
 * @author TienDat
 */
public class fptServer {
    static final int PORT=1234; 
       private ServerSocket server = null;
       public fptServer(){
           try {
               server = new ServerSocket(PORT);
           } catch (Exception e) {
               e.printStackTrace();
           }
       }
       public void action(){
           Socket socket = null;
           int i = 0;
           System.out.println("Server đang kết nối!");
           try {
               while((socket = server.accept())!= null){
                   new ServerThread(socket, "Client#"+i);
                   System.out.printf("Thread for Client#%d generating... %n ",i++);
               }
           } catch (Exception e) {
               e.printStackTrace();
           }
       }
    public static void main(String[] args) throws IOException {        
        new fptServer().action();
    
    }
   
    
}
