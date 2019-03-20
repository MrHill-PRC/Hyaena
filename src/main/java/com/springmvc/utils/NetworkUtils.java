package com.springmvc.utils;

import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.springmvc.entity.ClientStatus;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
public class NetworkUtils {
    private static final Logger log = LoggerFactory.getLogger("YzLogger");
    private static CLS_GetDevIpStatus_Thread getDevIpStatusThread = null;
    private static HashMap<String,Integer> devIpStatusMap = new HashMap <String, Integer>();
    private static Queue<String> allIp = new LinkedList<String>();


    public static void main(String[] args) {
        System.out.println("Test");
        initialize();
    }

    /**
     *
     */
    public static synchronized void initialize(){
        // 初始化执行任务线程
        if (getDevIpStatusThread == null) {
            getDevIpStatusThread = new CLS_GetDevIpStatus_Thread();
            getDevIpStatusThread.start();
        }
    }
    private  static void addIp(){
        allIp.offer("10.30.40.30");
        allIp.offer("10.30.40.29");
        allIp.offer("10.30.40.231");
        allIp.offer("10.30.40.86");
        allIp.offer("10.30.40.28");
    }
    private static class CLS_GetDevIpStatus_Thread extends Thread {
        // 每次等待时间
        private final int WAIT_TIME = 3000;
        public void run() {
            while (true) {
                try {

//                    System.out.println("检测");

                    StatusUtil stu = new StatusUtil();
                    ClientStatus a = stu.getClientStatus();
//                    System.out.println("已用内存："+a.getTotalMemory());
//                    System.out.println("空闲内存："+a.getFreeMemory());
//                    System.out.println("可用内存："+a.getMaxMemory());
                    if (allIp.size() > 0) { //判断是否要启动线程池进行IP检测
                        startPing();//每30秒进行一次多线程的检测
                    } else {
                        addIp();
                    }
                    Thread.sleep(WAIT_TIME);

                } catch (Exception e) {
                    log.error("CLS_CheckTask_Thread run Error", e);
                }
            }
        }

    }


    public static void startPing() {
        // 创建一个线程池，多个线程同时跑,根据队列长度确定线程池大小
        int threadNum = allIp.size();
        ExecutorService executor = Executors.newFixedThreadPool(threadNum);
        for (int i = 0; i < threadNum; i++) {
            executor.execute(new PingRunner());
        }
        executor.shutdown();//关闭线程池
//        try {
//            while (!executor.isTerminated()) {//检测线程池内的现场是否之心完成
//                Thread.sleep(100);
//            }
//        } catch (Exception e) {
//            log.error(e.getMessage());
//        }
    }
    private static class PingRunner implements Runnable {
        private String taskIp = null;
        public void run() {
            StatusUtil stu = new StatusUtil();
            ClientStatus a = stu.getClientStatus();

            System.out.println("111"+a.getPid());
            try {
                while ((taskIp = getIp()) != null) {
                    Thread.sleep(10000);
                    InetAddress addr = InetAddress.getByName(taskIp);
                    if (addr.isReachable(5000)) {
                        devIpStatusMap.put(taskIp, 0);
                    } else {
                        System.out.println("网络不通的IP"+taskIp);
                        devIpStatusMap.put(taskIp, -1);
                    }
                }
                System.out.println("22222"+a.getPid());
            } catch (SocketException e) {
                log.error("host ["+taskIp+"] permission denied");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //获取队列中的IP
        public String getIp() {
            String ip = null;
            synchronized (allIp) {
                ip = allIp.poll();
            }
            return ip;
        }
    }
}
