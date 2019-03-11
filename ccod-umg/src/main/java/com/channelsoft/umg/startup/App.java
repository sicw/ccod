package com.channelsoft.umg.startup;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author sicwen
 * @date 2019/03/09
 */
public class App {
    public static void main(String[] args) throws InterruptedException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:provider.xml");
        context.start();
        /**
         * 阻塞main方法，App不退出
         */
        synchronized (App.class){
            App.class.wait();
        }
    }
}