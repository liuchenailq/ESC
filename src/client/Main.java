package client;

import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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

    public static void main(String[] args) throws IOException {
        if(args.length != 3){
            System.out.println("filepath ip port");
            return;
        }

        File file = new File(args[0]);
        if(!file.exists()){
            System.out.println(args[0] + " file no exist!!!");
            return;
        }
        BufferedReader fbr = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

        int corePoolSize = 2;
        ArrayBlockingQueue<Runnable> arrayBlockingQueue = new ArrayBlockingQueue<>(corePoolSize);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, corePoolSize, 0L, TimeUnit.MILLISECONDS, arrayBlockingQueue, new CustomRejectedExecutionHandler());

        int seq = 1;  // 序号
        String line = "";
        int batchSize = 200000;
        while (line != null){
            ArrayList<String> arrayList = new ArrayList<>(batchSize);
            int batch = 0;
            while (batch < batchSize && ((line = fbr.readLine()) != null)){
                arrayList.add(line);
                batch ++;
            }

            // 调用线程处理并发送
            boolean stopFlag = line == null ? true : false;
            Task task = new Task(arrayList, stopFlag, seq, args[1], Integer.parseInt(args[2]));
            threadPoolExecutor.submit(task);
            seq ++;
        }
    }

}
