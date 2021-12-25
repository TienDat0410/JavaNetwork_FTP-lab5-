/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bai10.FTPServer;
import java.io.*;
import java.net.*;
import java.util.Scanner;
import javax.swing.JOptionPane;

/**
 *
 * @author TienDat
 */
public class Main {

    static void traThuMucClient(String path, PrintWriter out) {
        try {
            File dir = new File(path);
            File dsFile[];
            System.out.println("Dang doc tap tin");
            try {
                dsFile = dir.listFiles();
                System.out.println("da la ds tap tin");
                out.println(dsFile.length);
                for (int i = 0; i < dsFile.length; i++) {
                    String filename = dsFile[i].getName();
                    out.println(filename);
                }
                out.flush();
                System.out.println("da goi client");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.toString());
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
    //
    
}
