package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 接收方入口
 * @author LiuChen
 * @date 2020/3/30
 */
public class Main {
    public static void main(String[] args){
        int port = 1234;
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            Socket socket = null;

            File outputfile = new File("/root/exam/result.txt");
            if(!outputfile.exists()){
                outputfile.getParentFile().mkdirs();
                outputfile.createNewFile();
            }
            BufferedWriter fbw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputfile)));

            while(true) {
                socket = serverSocket.accept();

                InputStream ips = socket.getInputStream();
                InputStreamReader ipsr = new InputStreamReader(ips);
                BufferedReader br = new BufferedReader(ipsr);

                String s = "";
                while((s = br.readLine()) != null) {
                    fbw.write(s);
                    fbw.newLine();
                }

                fbw.close();
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
