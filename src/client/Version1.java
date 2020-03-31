package client;

import java.io.*;
import java.net.Socket;

/**
 * 读取一行，计算结果，发送一行
 * 发送方入口
 * @author LiuChen
 * @date 2020/3/30
 */
public class Version1 {
    public static void main(String[] args){
        long startTime = System.currentTimeMillis();

        String ip = "39.108.67.176";
        int port = 1234;

        try {
            Socket socket = new Socket(ip, port);

            OutputStream ops = socket.getOutputStream();
            OutputStreamWriter opsw = new OutputStreamWriter(ops);
            BufferedWriter bw = new BufferedWriter(opsw);

            File file = new File("/liuchen/input_data.txt");
            BufferedReader fbr = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

            String line = "";
            int pkCount = 5;  // 一次发送的行数
            int count = 0;
            StringBuilder sb = new StringBuilder();
            while ((line = fbr.readLine()) != null){
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

        System.out.println("总耗时： " + (System.currentTimeMillis() - startTime) / 1000 + " 秒");
    }

    /**
     * 容量计算
     * @param line
     * @return
     */
    public static String calc(String line){
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
