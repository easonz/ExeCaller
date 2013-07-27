package org.eason.execaller.schedule;

import java.io.Serializable;

import org.springframework.scheduling.quartz.CronTriggerBean;

public class InitCronTrigger extends CronTriggerBean implements Serializable {

	private String getCronExceptionDB() {
		String sql = "select CRON from t_test_task_trigger where available = 1 and trigger_name = 'cronTrigger'";
		System.out.println("*****" + sql);
		return sql;
	}
}