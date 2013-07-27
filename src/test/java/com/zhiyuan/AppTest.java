package com.zhiyuan;

import java.util.Calendar;
import java.util.Date;

import org.eason.utils.DateFormater;
import org.junit.Test;


/**
 * Unit test for simple App.
 */
public class AppTest {

	@Test
	public void test() {
		Date date = new Date();
		for (int i = 0; i < 3; i++) {
			date.setHours(date.getHours() - 1);
			System.out.println(date.getHours());
			System.out.println(DateFormater.getDateAndTimeStr(date));
		}

		Calendar c = Calendar.getInstance();
		for (int i = 0; i < 3; i++) {
			c.add(Calendar.DAY_OF_MONTH, -i);
			System.out.println(DateFormater.getDateAndTimeStr(c.getTime()));
		}

		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		System.out.println(DateFormater.getDateAndTimeStr(c.getTime()));
	}
}
