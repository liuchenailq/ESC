package client;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

/**
 * @author LiuChen
 * @date 2020/3/30
 */
public class Task implements Runnable {
    ArrayList<String> arrayList;
    boolean stopFlag = false;
    public Task(ArrayList<String> arrayList, boolean stopFlag){
        this.arrayList = arrayList;
        this.stopFlag = stopFlag;
    }

    @Override
    public void run() {
        String ip = "39.108.67.176";
        int port = 1234;

        try {
            Socket socket = new Socket(ip, port);

            OutputStream ops = socket.getOutputStream();
            OutputStreamWriter opsw = new OutputStreamWriter(ops);
            BufferedWriter bw = new BufferedWriter(opsw);

            if(stopFlag){
                bw.write( "yes\r\n");
                bw.flush();
            }else {
                bw.write( "no\r\n");
                bw.flush();
            }

            int pkCount = 5;  // 一次发送的行数
            int count = 0;
            StringBuilder sb = new StringBuilder();
            for(String line : arrayList){
                String tmp = calc(line);
                if(tmp != null){
                    if(count == pkCount){
                        sb.append(tmp);
                        bw.write(sb.toString() + "\r\n");
                        bw.flush();
                        sb.delete(0, sb.length());
                        count = 0;
                    }else {
                        count ++;
                        sb.append(tmp + ";");
                    }
                }
            }

            if(sb.length() > 0){
                bw.write(sb.toString() + "\r\n");
                bw.flush();
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 容量计算
     * @param line
     * @return
     */
    public String calc(String line){
        int index = line.indexOf(' ');
        String head = line.substring(0, index + 1);
        String data = line.substring(index + 1, line.length());
        String[] arr = data.split(",");
        StringBuilder sb = new StringBuilder();
        sb.append(head);
        boolean flag = false;
        for(String t : arr){
            // 除法可能有改进的地方
            float d = Float.parseFloat(t);
            if(d > 2400.0){
                int a = (int) (d / 2400);
                sb.append(a);
                sb.append(",");
                flag = true;
            }else{
                sb.append(0);
                sb.append(",");
            }
        }
        if(flag){
            return sb.substring(0, sb.length() - 1);
        }else {
            return null;
        }
    }
}