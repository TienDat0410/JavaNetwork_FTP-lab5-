/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab5;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import javax.swing.JOptionPane;

/**
 *
 * @author TienDat
 */
public class Server {
    static final int PORT = 1234;
    private ServerSocket server = null;
    public Server(){
        try {
            server = new ServerSocket(PORT);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"Lỗi");
        }
    }
    public void action(){
        Socket socket = null;
        int i = 0;
        System.out.println("Server đang kết nối");
        try {
            while((socket = server.accept()) != null){
                new ServerThread(socket, "Client#"+i);
                System.out.printf("Thread for Client#%d generating...%n",i++);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //
    public class ServerThread implements Runnable{
        private Scanner in = null;
        private PrintWriter out = null;
        private Socket socket;
        private String name;
        //
        public ServerThread(Socket socket, String name) throws IOException{
            this.socket = socket;
            this.name = name;
            this.in = new Scanner(this.socket.getInputStream());
            this.out = new PrintWriter(this.socket.getOutputStream(), true);
            new Thread(this).start();
        }
        //
        @Override
        public void run() {
            try {
                while(true)
                {
                    String chuoi = in.nextLine().trim();
                    chuoi = chuoi.toUpperCase();
                    out.println(chuoi);
                }
            } catch (Exception e) {
                System.out.println(name+"has departed");
            }finally{
                try {
                    socket.close();
                } catch (Exception e) {
                }
            }
        }
        
    }
    public static void main(String[] args) {
        new Server().action();
    }
    
}
