package com.business.start;


import com.business.start.GetNewsApplication;
import com.business.start.service.DataService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=GetNewsApplication.class)
@EnableAutoConfiguration
public class BaoShanTest {

    @Autowired
    @Qualifier(value = "baoShanService")
    private DataService baoShanService ;

    public void read(){
        baoShanService.read();
    }

}
