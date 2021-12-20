/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bai5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JTextPane;

/**
 *
 * @author TienDat
 */
public class AssetRead {
    private Socket socket;
    private JTextPane txpMessageBrad;
    private PrintWriter out;
    private BufferedReader in;
    //
    public AssetRead(Socket socket, JTextPane txpMessageBrad) throws IOException {
        this.socket = socket;
        this.txpMessageBrad = txpMessageBrad;
        
        out = new PrintWriter(socket.getOutputStream());
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        //tạo quá trình nhận dữ liệu từ socket
        receive();
    }
    //nhận dữ liệu từ socket
    public void receive(){
        Thread thread = new Thread()
        {
            public void run()
            {
                while(true){
                    try {
                        String chuoi = in.readLine();
                        if(chuoi != null){
                            txpMessageBrad.setText(txpMessageBrad.getText() + "\n" + chuoi);                       
                        }
                    } catch (Exception e) {
                    }
                }
            }
                                  
        };
        thread.start();
    }
    //gửi dữ liệu
    public void send(String messager)
    {
        String chuoigui = txpMessageBrad.getText();
        txpMessageBrad.setText(chuoigui + "\nsent:" + messager);
        out.println(messager);
        out.flush();
    }
    //đóng kết nối
    public void close()
    {
        try {
            out.close();
            in.close();
            socket.close();
        } catch (Exception e) {
        }
    }
}
