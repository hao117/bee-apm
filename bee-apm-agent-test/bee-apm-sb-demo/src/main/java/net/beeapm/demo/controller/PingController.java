package net.beeapm.demo.controller;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.concurrent.ScheduledFuture;

/**
 * @author yuanlong.chen
 * @date 2020/07/30
 */
@RequestMapping("/ping")
@Controller
public class PingController {
    @Autowired
    ThreadPoolTaskScheduler scheduler;

    public String test() {
       for(int i =0 ; i < 1000; i++) {
           final int idx = i;
           ScheduledFuture future = scheduler.schedule(new Runnable() {
               @Override
               public void run() {
                   System.out.println("==================="+ idx + " : " + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
               }
           }, new CronTrigger("0/30 * * * * ?"));
       }
        return "ok";
    }
}
