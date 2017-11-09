package com.omnia.admin;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;
import java.time.LocalDate;

@RunWith(MockitoJUnitRunner.class)
public class AdminApplicationTests {

	@Test
	public void contextLoads() {

        System.out.println(Date.valueOf("2017-11-05").equals(Date.valueOf("2017-11-04")));;
        System.out.println(Date.valueOf(LocalDate.now()));

    }
}
