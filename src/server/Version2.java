package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author LiuChen
 * @date 2020/3/30
 */
public class Version2 {

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

                String s = br.readLine();

                boolean stopFlag = s.equals("yes") ? true : false;

                while((s = br.readLine()) != null) {
                    for(String t : s.split(";")){
                        fbw.write(t);
                        fbw.newLine();
                    }
                }

//                if(stopFlag){
//                    break;
//                }
                socket.close();
            }
//            fbw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
