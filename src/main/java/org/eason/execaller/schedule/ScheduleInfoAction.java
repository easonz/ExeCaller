package org.eason.execaller.schedule;

import java.text.ParseException;
import java.util.Date;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.CronTriggerBean;

public class ScheduleInfoAction {
	private static Logger logger = LoggerFactory.getLogger(ScheduleInfoAction.class);

	private Scheduler scheduler;

	// 设值注入，通过setter方法传入被调用者的实例scheduler
	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	public void reScheduleJob() throws SchedulerException {
		// 运行时可通过动态注入的scheduler得到trigger，注意采用这种注入方式在某些项目中会有问题，如果遇到注入问题，可以采取在运行方法时候，获得bean来避免错误发生。
		CronTriggerBean trigger = (CronTriggerBean) scheduler.getTrigger(
				"cronTrigger", Scheduler.DEFAULT_GROUP);

		logger.info("*********** trigger: " + trigger);
		String dbCronExpression = getCronExpressionFromDB();
		logger.info("*********** dbCronExpression: " + dbCronExpression);
		String originConExpression = trigger.getCronExpression();
		logger.info("*********** originConExpression: " + originConExpression);
		// 判断从DB中取得的任务时间(dbCronExpression)和现在的quartz线程中的任务时间(originConExpression)是否相等
		// 如果相等，则表示用户并没有重新设定数据库中的任务时间，这种情况不需要重新rescheduleJob
		if (!originConExpression.equalsIgnoreCase(dbCronExpression)) {
			try {
				trigger.setCronExpression(dbCronExpression);

				scheduler.rescheduleJob("cronTrigger", Scheduler.DEFAULT_GROUP,
						trigger);
			} catch (SchedulerException e) {
				logger.error("------------------- SchedulerException Error! -------------------");
				e.printStackTrace();
				logger.error("-------------------------------------------------------------");
			} catch (ParseException e) {
				logger.error("------------------- ParseException Error! -------------------");
				e.printStackTrace();
				logger.error("-------------------------------------------------------------");
			}
		}
		// 执行task
		logger.info("task start time: " + new Date());
		System.out.println("Task test success!");
		logger.info("  task end time: " + new Date());
	}

	private String getCronExpressionFromDB() {
		String sql = "0/2 * * * * ?";
		return sql;
	}
}

