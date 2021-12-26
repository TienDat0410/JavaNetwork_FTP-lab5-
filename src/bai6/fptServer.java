/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bai6;

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
    public static final int UPLOAD=3;
    public static final int DANGNHAP = 1;
    public static final int KHONGLALENH = 0;
    public static final int DANGNHAPKHONGTHANHCONG = 0;
    public static final int DANGNHAPTHANHCONG = 1;
    public static final int THOAT = 2;
    public static final int DOWNLOAD=4;
    public static int laLenh(String cmd) {
        if (cmd.equals("DANGNHAP")) {
            return DANGNHAP;
        }
        if (cmd.equals("UPLOAD")) {
            return UPLOAD;
        }
        return KHONGLALENH;
    }
    public static final int PORT = 1234;
    public static void main(String[] args) throws IOException {        
        //String path = "C://";
        String path= null;
        //path = "D:\\THlaptrinhmangmt";
        ServerSocket Ssocket;
        DBAccess acc = new DBAccess();
        try {
            System.out.println("Server đang kết nối");
            Ssocket = new ServerSocket(PORT);
            while (true) {
                Socket server = Ssocket.accept();
                boolean lap = true;
                while (lap) {
                    String cmd;
                    //
                    Scanner sc = new Scanner(server.getInputStream());
                    cmd = sc.nextLine();
                    switch (laLenh(cmd)) {
                        case DANGNHAP:
                            String user = sc.nextLine();
                            String pass = sc.nextLine();
                            PrintWriter out = null;
                            ResultSet rset = acc.Query("select * from taikhoan where username='" + user + "'and password='" + pass + "'");
                            out = new PrintWriter(server.getOutputStream());
                            if (rset.next()) {
                                out.println(DANGNHAPTHANHCONG);
                                path = rset.getString("Duongdan");
                                //mo thu muc len goi ve cho client                               
                                File dir = new File(path);
                                File dsFile[] = dir.listFiles();
                                if (dsFile == null) {
                                    JOptionPane.showMessageDialog(null, "Đường dẫn không đúng,hay không phait thư mục");

                                } else {
                                    out.println(dsFile.length);
                                    for (int i = 0; i < dsFile.length; i++) {
                                        out.println(dsFile[i].getName());
                                    }
                                }

                            } else {
                                //goi ve khong mo duoc
                                out.println(DANGNHAPKHONGTHANHCONG);
                                out.println("Dang nhap ko thanh cong");
                            }
                            out.flush();
                            break;
                        //upload
                        case UPLOAD:
                            System.out.println("Da vao lenh upload");
                            String fileName = sc.nextLine();
                            System.out.println("Da lay ten tap tin");
                            try {
                                String path2;
                                //kiem tra chuoi duong dan co dau / cuoi cung hay ko?
                                //va gan ten tap tin tu client vao tuong ung
                                if (path.lastIndexOf("/") >= path.length() - 1) {
                                    path2 = path + fileName;
                                } else {
                                    path2 = path + "/" + fileName;
                                }
                                System.out.println(path2);
                                FileOutputStream dout = new FileOutputStream(new File(path2));
                                BufferedOutputStream bout = new BufferedOutputStream(dout);
                                BufferedInputStream bis;
                                bis = new BufferedInputStream(server.getInputStream());
                                byte buf[] = new byte[bis.available()];
                                bout.write(bis.read(buf));
                                bout.flush();
                                bout.close();
                                out = new PrintWriter(server.getOutputStream());
                                out.println("DANHAN");
                                out.flush();
                                //yeu cau update lai listbox o server
                                //mo thu muc ra va tra ve noi dung thu muc o phia server
                                Main.traThuMucClient(path, out);
                            } catch (Exception e) {
                                System.out.println(e);
                            }
                            break;
                        //
                         case DOWNLOAD:
                            //lay ten tap tin do client goi len
                            System.out.println("Da vao lenh download");
                            String fileNameD = sc.nextLine();
                            System.out.println("Da lay ten tap tin");
                            try {
                                String cpath;
                                //kiem tra chuoi duong dan co dau / cuoi cung hay ko?
                                //va gan ten tap tin tu client vao tuong ung
                                if (path.lastIndexOf("/") >= path.length() - 1) {
                                    cpath = path + fileNameD;
                                } else {
                                    cpath = path + "/" + fileNameD;
                                }
                                System.out.println(cpath);
                                //mo tap tin ra
                                BufferedInputStream bin;
                                bin = new BufferedInputStream(new FileInputStream(cpath));
                                //lap doc noi dung tap tin va goi lieu len server
                                byte buf[] = new byte[bin.available()];
                                //tao bo dem doc het du lieu tu tap tin vao bo dem roi day
                                //vao luong len server.
                                BufferedOutputStream bout;
                                bout=new BufferedOutputStream(server.getOutputStream());
                                bout.write(bin.read(buf));
                                System.out.println("da goi du lieu tap tin ve cho client");
                                bout.flush();
                                //doi nhan danh sach tap thu cua folder o server voi tinh trang moi
                                Scanner scRequest = new Scanner(server.getInputStream());
                                String cmdRequest = scRequest.nextLine();
                                System.out.println("da nhan dap tra tu server");
                                if (cmdRequest.equals("DANHAN")) {
                                    System.out.println("Đã gửi tệp tin thành công");
                                }
                                else{
                                    System.out.println("Gửi tệp tin thất bại");
                                }
                            } catch (Exception e) {
                                System.out.println(e);
                            }
                            break;
                        //
                        case THOAT:
                            lap = false;
                            break;
                    }
                }
                server.close();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Lỗi");
            ex.printStackTrace();
        }
    }
    //
    
}
