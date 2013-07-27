package org.eason.execaller;

import java.text.ParseException;

import org.eason.execaller.schedule.AutoDeleteJob;
import org.eason.execaller.schedule.AutoDetectiveJob;
import org.eason.execaller.schedule.AutoGenerateJob;
import org.eason.execaller.utils.PQDIFConfig;
import org.eason.execaller.utils.PQDIFConfigUtils;
import org.eason.utils.ThreadUtils;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.CronTriggerBean;
import org.springframework.stereotype.Component;


@Component
public class PQDIFGenerator {

	private static Logger logger = LoggerFactory.getLogger(PQDIFGenerator.class);

	@Autowired
	private Scheduler scheduler;

	// 设值注入，通过setter方法传入被调用者的实例scheduler
	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	public void start() {
		logger.info("PQDIFGenerater start.");
		try {
			while (true) {
				doSchedule();
				// 十分钟唤醒一次
				ThreadUtils.sleep(10 * 60 * 1000);
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
			logger.info("schedule job fail");
		}
	}

	/**
	 * 根据配置文件调度相应的PQDIF文件生成与维护逻辑
	 * 
	 * @throws SchedulerException
	 */
	public void doSchedule() throws SchedulerException {
		PQDIFConfig config = PQDIFConfigUtils.getPQDIFConfigDTO();
		// 自动删除逻辑
		if (config.isAutoDelete()) {
			int interval = config.getAutoDeleteInterval();
			String cronExpression = "0 0 0/" + interval + " * * ?";
			// String cronExpression = "0/" + interval + " 0 * * * ?";
			autoRunJob(AutoDeleteJob.TRIGGER_NAME, cronExpression);
		} else {
			stopJob(AutoDeleteJob.TRIGGER_NAME);
		}

		// 自动生成逻辑
		if (config.isAutoGenerate()) {
			int interval = config.getGenerateInterval();
			// String cronExpression = "0/" + interval + " * * * * ?";
			// String cronExpression = "0 0/1 * * * ?";
			String cronExpression = "0 0 0/" + interval + " * * ?";
			autoRunJob(AutoGenerateJob.TRIGGER_NAME, cronExpression);
		} else {
			stopJob(AutoGenerateJob.TRIGGER_NAME);
		}
		printTriggerInfo(AutoGenerateJob.TRIGGER_NAME);

		// 自动检测补漏生成逻辑
		if (config.isAutoDetective()) {
			int interval = config.getAutoDetectiveInterval();
			// String cronExpression = "0/" + interval + " * * * * ?";
			// String cronExpression = "0 0/" + interval + " * * * ?";
			String cronExpression = "0 10 0/" + interval + " * * ?";
			autoRunJob(AutoDetectiveJob.TRIGGER_NAME, cronExpression);
		} else {
			stopJob(AutoDetectiveJob.TRIGGER_NAME);
		}
	}

	/**
	 * 开始调度任务
	 * 
	 * @param triggerName
	 * @param newCronExp
	 * @throws SchedulerException
	 */
	public void autoRunJob(String triggerName, String newCronExp)
			throws SchedulerException {

		CronTriggerBean trigger = (CronTriggerBean) scheduler.getTrigger(
				triggerName, Scheduler.DEFAULT_GROUP);
		if (scheduler.getTriggerState(triggerName,
				Scheduler.DEFAULT_GROUP) == Trigger.STATE_PAUSED) {
			logger.info("resume paused trigger {} ",
					AutoGenerateJob.TRIGGER_NAME);
			scheduler.resumeTrigger(triggerName,
					Scheduler.DEFAULT_GROUP);
		}

		String oldCronExp = trigger.getCronExpression();
		if (!oldCronExp.equals(newCronExp)) {
			try {
				trigger.setCronExpression(newCronExp);
				logger.info("old cron exp is {} ", oldCronExp);
				logger.info("set new cron exp {} ", newCronExp);
			} catch (ParseException e) {
				logger.info("parse exp {} error.", newCronExp);
				e.printStackTrace();
			}
			logger.info("reschedule trigger {} ", triggerName);
			scheduler.rescheduleJob(triggerName,
					Scheduler.DEFAULT_GROUP, trigger);
		}
	}

	public void printTriggerInfo(String triggerName) throws SchedulerException {
		CronTriggerBean trigger = (CronTriggerBean) scheduler.getTrigger(
				triggerName, Scheduler.DEFAULT_GROUP);
		logger.info("trigger {}'s next fire time is {}", triggerName,
				trigger.getNextFireTime());
		logger.info("trigger {}'s prev fire time is {}", triggerName,
				trigger.getPreviousFireTime());
	}

	/**
	 * 停止调度任务
	 * 
	 * @param triggerName
	 * @throws SchedulerException
	 */
	public void stopJob(String triggerName) throws SchedulerException {

		if (scheduler.getTriggerState(triggerName,
				Scheduler.DEFAULT_GROUP) != Trigger.STATE_PAUSED) {

			logger.info("pause trigger {} ", triggerName);
			scheduler.pauseTrigger(triggerName,
					Scheduler.DEFAULT_GROUP);
		}
	}

	public void stop() {
		try {
			// waitForJobsToComplete
			scheduler.shutdown(true);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		logger.info("PQDIFGenerater stop.");
	}

}
