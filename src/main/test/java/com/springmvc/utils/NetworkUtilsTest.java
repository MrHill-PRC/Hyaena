package com.springmvc.utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.*;

public class NetworkUtilsTest {
    private static final Logger log = LoggerFactory.getLogger("YzLogger");
    private static CLS_GetDevIpStatus_Thread getDevIpStatusThread = null;
    private static HashMap<String,Integer> devIpStatusMap = new HashMap <String, Integer>();
    private static Queue<String> allIp = new LinkedList<String>();
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void initialize() {
        if (getDevIpStatusThread == null) {
            getDevIpStatusThread = new CLS_GetDevIpStatus_Thread();
            getDevIpStatusThread.start();
        }
    }
    public void startPing() {
        // 创建一个线程池，多个线程同时跑,根据队列长度确定线程池大小
        int threadNum = allIp.size() + 10;
        ExecutorService executor = Executors.newFixedThreadPool(threadNum);
        for (int i = 0; i < threadNum; i++) {
            executor.execute(new PingRunner());
        }
        executor.shutdown();//关闭线程池
        try {
            while (!executor.isTerminated()) {//检测线程池内的现场是否之心完成
                Thread.sleep(100);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
    private class CLS_GetDevIpStatus_Thread extends Thread {
        // 每次等待时间

        private final int WAIT_TIME = 30000;
        public void run() {
            System.out.println("111");
            while (true) {
                try {
                    Thread.sleep(WAIT_TIME);
                    System.out.println("检测");
                    if (allIp.size() > 0) { //判断是否要启动线程池进行IP检测
                        startPing();//每30秒进行一次多线程的检测
                    }
                } catch (Exception e) {
                    log.error("CLS_CheckTask_Thread run Error", e);
                }
            }
        }

    }
    private class PingRunner implements Runnable {
        private String taskIp = null;
        public void run() {
            try {
                while ((taskIp = getIp()) != null) {
                    InetAddress addr = InetAddress.getByName(taskIp);
                    if (addr.isReachable(5000)) {
                        devIpStatusMap.put(taskIp, 0);
                    } else {
                        devIpStatusMap.put(taskIp, -1);
                    }
                }
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