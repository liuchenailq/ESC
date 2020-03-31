package client;

import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 单线程读取文件、多线程处理并发送
 * @author LiuChen
 * @date 2020/3/30
 */
public class Version2 {

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
        long startTime = System.currentTimeMillis();

        File file = new File("/liuchen/input_data.txt");
        BufferedReader fbr = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

        int corePoolSize = 2;
        ArrayBlockingQueue<Runnable> arrayBlockingQueue = new ArrayBlockingQueue<>(corePoolSize);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, corePoolSize, 0L, TimeUnit.MILLISECONDS, arrayBlockingQueue, new CustomRejectedExecutionHandler());

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
            Task task = new Task(arrayList, stopFlag);
            threadPoolExecutor.submit(task);
        }

        System.out.println("总耗时： " + (System.currentTimeMillis() - startTime) / 1000 + " 秒");
    }
}
