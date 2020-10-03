package com.jin.test;



import com.jin.OrderApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = DemoTestTest.class )
@SpringBootTest(classes = OrderApplication.class)
public class DemoTestTest {

    @Test
    public void func (){
        System.out.println(6666);

    }
}