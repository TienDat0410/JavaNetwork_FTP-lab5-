/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bai4;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JOptionPane;

/**
 *
 * @author TienDat
 */
public class Server {
    public static void main(String[] args) throws IOException 
    {
        ServerSocket server = null;
        Socket socket = null;
        int PORT = 8888;
//        for(int port = 1024; port < 65536; port++){
//                      PORT = port;
//        }       
         try {
                System.out.println("Server đang kết nối");
                server = new ServerSocket(PORT);
                socket = server.accept();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Lỗi");
            }
        socket.close();
        server.close();
    }
}
