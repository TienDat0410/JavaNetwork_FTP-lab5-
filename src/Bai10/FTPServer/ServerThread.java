/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bai10.FTPServer;
import java.io.*;
import java.util.*;
import java.net.*;
import java.sql.ResultSet;
//import java.sql.ResultSetMetaData;
import javax.swing.JOptionPane;

/**
 *
 * @author TienDat
 */
public class ServerThread implements Runnable {
    private Scanner in = null;
    private PrintWriter out = null;
    private Socket socket;
    private String name;

    //
    public ServerThread(Socket socket, String name) throws IOException {
        this.socket = socket;
        this.name = name;
        this.in = new Scanner(this.socket.getInputStream());
        this.out = new PrintWriter(this.socket.getOutputStream(), true);
        new Thread(this).start();
    }
    //cac cau lenh giao tiep voi client
    public static final int DANGNHAP = 1;
    public static final int DOWNLOAD = 2;
    public static final int NOCMD = -1;
    public static final int UPLOAD = 3;

    public int lenh(String strcmd) {
        if (strcmd.equals("dangnhap")) {
            return DANGNHAP;//Client yeu cau dang nhap
        }
        if (strcmd.equals("upload")) {
            return UPLOAD;//client upload file len sẻver
        }
        if (strcmd.equals("download")) {
            return DOWNLOAD;//client yeu cau download file
        }
        return NOCMD;//client khong có yêu cầu gì
    }

    private void traThuMucClient(String path, PrintWriter out) {
        try {
            File dir = new File(path);
            File dsFile[];
            try {
                dsFile = dir.listFiles();
                if (dsFile == null) {
                    out.println(0);
                } else {
                    out.println(dsFile.length);
                    for (int i = 0; i < dsFile.length; i++) {
                        String filename = dsFile[i].getName();
                        out.println(filename);
                    }
                }
                out.flush();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.toString());
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    //run
    public void run() {
        Scanner sc = null;
        DBAccess acc = null;
        String filename;
        String path=null;
        File f;
        byte[] mybytearray;
        try {
            while (true) {
                String s = in.nextLine().trim();
                sc = null;
                String cmd = "";
                String data = "";
                try {
                    sc = new Scanner(s);
                    sc.useDelimiter("@");
                    cmd = sc.next();
                    data = sc.next();
                } catch (Exception e) {
                }
                //dieu phoi su kien tu client
                switch (lenh(cmd)) {
                    case DANGNHAP:
                        acc = new DBAccess();
                        ResultSet rs = acc.Query(data);
                        if (rs.next()) {
                            out.println("OK");
                            path = rs.getString("Duongdan");
                            //mo thu muc ra va tra ve noi dung thu muc o phia server
                            this.traThuMucClient(path, out);
                        } else {
                            out.println("NOTOK");
                        }
                        break;
                    case UPLOAD:
                        int bytesRead;
                        int current = 0;
                        int doDaiFile = 0;
                        rs = acc.Query(data);
                        filename = sc.next();
                        doDaiFile = Integer.parseInt(sc.next());
                        rs.next();
                        path = rs.getString("Duongdan");
                        f = new File(path + "/" + filename);
                        if (!f.exists()) {
                            f.createNewFile();
                        }
                        mybytearray = new byte[doDaiFile];
                        InputStream is = socket.getInputStream();
                        FileOutputStream fos = new FileOutputStream(f);
                        BufferedOutputStream bos;
                        bos = new BufferedOutputStream(fos);
                        bytesRead = is.read(mybytearray, 0, mybytearray.length);
                        current = bytesRead;
                        while (current != doDaiFile) {
                            bytesRead = is.read(mybytearray, current, mybytearray.length - current);
                            if (bytesRead >= 0) {
                                current += bytesRead;
                            }
                        }
                        bos.write(mybytearray, 0, current);
                        bos.flush();
                        bos.close();
                        this.traThuMucClient(path, out);
                        break;
                    case DOWNLOAD:
//                        System.out.println("Da vao lenh download");
//                        rs = acc.Query(data);
//                        filename = sc.next();
//                        rs.next();
//                        path = rs.getString("Duongdan");
//                        FileInputStream fi = new FileInputStream(path+"/"+filename);                                           
//                        mybytearray = new byte[1024*32];
//                        fi.read(mybytearray, 0, mybytearray.length);                                              
//                        OutputStream os = socket.getOutputStream();                      
//                        os.write(mybytearray, 0, mybytearray.length);
                        System.out.println("Da vao lenh download");
                        rs = acc.Query(data);
                        filename = sc.next();
                        rs.next();
                        path = rs.getString("Duongdan");
                        f = new File(path + "/" + filename);
                        out.println(f.length());//truyen do dai file
                        mybytearray = new byte[(int) f.length()];
                        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f));
                        bis.read(mybytearray, 0, mybytearray.length);
                        OutputStream os = socket.getOutputStream();
                        os.write(mybytearray, 0, mybytearray.length);
                        os.flush();
                        bis.close();                        
                }
            }
        } catch (Exception e) {
            System.out.println(name + " has departed");
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
            }
            if (sc != null) {
                sc.close();
            }
        }
    }
}
            
