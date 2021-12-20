/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bai2;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author TienDat
 */
public class ServerThread implements Runnable{
    private Scanner in = null;
    private PrintWriter out = null;
    private Socket socket;
    private String name;

    public ServerThread(Socket socket, String name) throws  IOException{
        this.socket = socket;
        this.name = name;
        this.in = new Scanner(this.socket.getInputStream());
        this.out = new PrintWriter(this.socket.getOutputStream(), true);
        new Thread(this).start();
    }
    public void run(){
        try {
            while(true){
                String chuoi = in.nextLine().trim();
                Scanner sc = new Scanner(chuoi);
                sc.useDelimiter("@");
                int so1 = sc.nextInt();
                String pheptoan = sc.next();
                int so2 = sc.nextInt();
                if(pheptoan.equals("+"))
                    out.println(so1+so2);
                else if(pheptoan.equals("-"))
                    out.println(so1-so2);
                else if(pheptoan.equals(""))
                    out.println(so1*so2);
                else if(pheptoan.equals("/"))
                    out.println((float)so1/so2);
            }
        } catch (Exception e) {
            System.out.println(name+" has departed");
        }finally{
            try {
                socket.close();
            } catch (IOException e) {

            }
        }
    }
}
