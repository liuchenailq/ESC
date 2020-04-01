package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author LiuChen
 * @date 2020/4/1
 */
public class Main {
    /**
     * 阻塞策略
     */
    private static class CustomRejectedExecutionHandler implements RejectedExecutionHandler {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            try {
                executor.getQueue().put(r);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 工作线程：接受数据
     */
    private static class Work implements Callable<List<String>> {
        private Socket socket;
        private BufferedReader br;
        public Work(Socket socket, BufferedReader br){
            this.socket = socket;
            this.br = br;
        }

        @Override
        public List<String> call() throws Exception {
            String s = br.readLine();  // seq
            List<String> list = new ArrayList<>();
            list.add(s);
            while((s = br.readLine()) != null) {
                for(String t : s.split(";")){
                    list.add(t);
                }
            }
            br.close();
            socket.close();
            return list;
        }
    }

    public static void main(String[] args) throws IOException {
        if(args.length != 1){
            System.out.println("port");
            return;
        }
        int port = Integer.parseInt(args[0]);

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            Socket socket = null;

            int corePoolSize = 2;
            ArrayBlockingQueue<Runnable> arrayBlockingQueue = new ArrayBlockingQueue<>(corePoolSize);
            ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, corePoolSize, 0L, TimeUnit.MILLISECONDS, arrayBlockingQueue, new CustomRejectedExecutionHandler());

            List<Future<List<String>>> futures = new ArrayList<>();

            while(true) {
                socket = serverSocket.accept();
                System.out.println(socket.getInetAddress().getHostAddress());

                InputStream ips = socket.getInputStream();
                InputStreamReader ipsr = new InputStreamReader(ips);
                BufferedReader br = new BufferedReader(ipsr);

                String s = br.readLine();
                boolean stopFlag = s.equals("yes") ? true : false;

                Work work = new Work(socket, br);
                Future<List<String>> future = threadPoolExecutor.submit(work);
                futures.add(future);

                if(stopFlag){
                    break;
                }
            }

            threadPoolExecutor.shutdown();

            File outputfile = new File("/root/exam/result.txt");
            if(!outputfile.exists()){
                outputfile.getParentFile().mkdirs();
                outputfile.createNewFile();
            }
            BufferedWriter fbw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputfile)));

            int maxSeq = -1;
            HashMap<Integer, List<String>> data = new HashMap<>();
            for(Future f : futures){
                List<String> tmp = (List<String>) f.get();
                int seq = Integer.parseInt(tmp.get(0));
                maxSeq = Integer.max(maxSeq, seq);
                data.put(seq, tmp);
            }

            for(int i = 1; i<= maxSeq; i++){
                List<String> tmp = data.get(i);
                for(int j = 1; j<tmp.size(); j++){
                    fbw.write(tmp.get(j));
                    fbw.newLine();
                }
            }
            fbw.close();
        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
