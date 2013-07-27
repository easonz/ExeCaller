package org.eason.execaller.schedule;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.eason.execaller.utils.PQDIFConfig;
import org.eason.execaller.utils.PQDIFConfigUtils;
import org.eason.utils.DateFormater;
import org.eason.utils.IniUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AutoDetectiveJob extends AbstractJob {
	private static Logger logger = LoggerFactory.getLogger(AutoDetectiveJob.class);
	public final static String JOB_NAME = "autoDetectiveJob";
	public final static String TRIGGER_NAME = "autoDetectiveJobTrigger";

	public AutoDetectiveJob() {

	}

	public void doJob() {
		logger.info("doJob");

		List<String> devList = getDevices();
		if (devList == null) {
			logger.info("get device list fail. current task canceled.");
			return;
		}


		PQDIFConfig config = PQDIFConfigUtils.getPQDIFConfigDTO();
		Calendar timeLineCal = Calendar.getInstance();
		timeLineCal.add(Calendar.DAY_OF_MONTH, -config.getDefaultSaveTime());
		final String timeLineStr = DateFormater.getTimeStr(
				timeLineCal.getTime(), "yyyyMMdd");

		String iniPath = config.getPqdifFolder() + "/pqdifpof.ini";
		IniUtils pqdifpofIni = new IniUtils(iniPath);

		Calendar checkedCal = Calendar.getInstance();
		// 计算前一次生成PQDIF程序运行的时间
		checkedCal.set(Calendar.MINUTE, 0);
		checkedCal.set(Calendar.SECOND, 0);
		int timeCompensate = checkedCal.get(Calendar.HOUR_OF_DAY)
				% config.getGenerateInterval();
		checkedCal.add(Calendar.HOUR_OF_DAY, -timeCompensate);

		while (checkedCal.getTimeInMillis() >= timeLineCal.getTimeInMillis()) {

			// 计算前一次的触发时间
			checkedCal.add(Calendar.HOUR_OF_DAY, -config.getGenerateInterval());
			Date startDate = checkedCal.getTime();
			String pqdifFileName = DateFormater.getDateAndTimeStr(startDate,
					"yyyyMMdd'T'HHmmss'.pqd'");

			for (String deviceId : devList) {

				String deviceFolderName = pqdifpofIni.getValue("DIRECTORYMAP",
						deviceId);
				String pqdifFilePath = String.format("%s/%s/%s",
						config.getPqdifFolder(), deviceFolderName,
						pqdifFileName);
				File pqdifFile = new File(pqdifFilePath);
				if (!pqdifFile.exists()) {
					logger.info(
							"target PQDIF file not exists, need to generate. : {}",
							pqdifFilePath);
					config.setDeviceIds(deviceId);
					// new PQDIFGeneratorExecutor().generatePQDIF_test(config);
				}
			}
		}
	}
}
