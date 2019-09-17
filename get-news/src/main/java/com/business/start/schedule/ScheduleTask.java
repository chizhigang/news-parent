package com.business.start.schedule;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@Configurable
@EnableScheduling
public class ScheduleTask {

    @Resource
    private RestTemplate restTemplate ;

    @Scheduled(cron = "0/5 * * * * ?")
    public void readDataNews(){

    }
}
