package org.eason.execaller.schedule;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Calendar;
import java.util.List;

import org.eason.execaller.utils.PQDIFConfig;
import org.eason.execaller.utils.PQDIFConfigUtils;
import org.eason.utils.DateFormater;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AutoDeleteJob extends AbstractJob {

	private static Logger logger = LoggerFactory.getLogger(AutoDeleteJob.class);
	public final static String JOB_NAME = "autoDeleteJob";
	public final static String TRIGGER_NAME = "autoDeleteJobTrigger";

	public AutoDeleteJob() {

	}

	public void doJob() {

		logger.info("doJob");

		List<String> devList = getDevices();
		if (devList == null) {
			logger.info("get device list fail. current task canceled.");
			return;
		}

		PQDIFConfig config = PQDIFConfigUtils.getPQDIFConfigDTO();
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, -config.getDefaultSaveTime());
		final String timeLineStr = DateFormater.getTimeStr(c.getTime(), "yyyyMMdd");

		for (String device : devList) {
			File deviceFolder = getTargetFolder(device);
			File[] files = deviceFolder.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					String currentTimeStr = name.substring(0, 8);
					if (Integer.valueOf(currentTimeStr) > Integer
							.valueOf(timeLineStr)) {
						return false;
					}
					return true;
				}
			});
			for (File file : files) {
				logger.info("delete PQDIF file {} that out of date...",
						file.getName());
				// file.delete();
			}
		}
	}

}
