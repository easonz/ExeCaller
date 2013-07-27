package org.eason.execaller.schedule;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.eason.execaller.utils.PQDIFConfig;
import org.eason.execaller.utils.PQDIFConfigUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

public class AutoGenerateJob extends AbstractJob {
	private static Logger logger = LoggerFactory.getLogger(AutoGenerateJob.class);
	public final static String JOB_NAME = "autoGenerateJob";
	public final static String TRIGGER_NAME = "autoGenerateJobTrigger";


	public AutoGenerateJob() {

	}

	public void doJob() {

		logger.info("doJob");
		try {

			generatePQDIF();
		} catch (Throwable t) {
			logger.error("generate PQDIF error...", t);
		}
	}

	private void generatePQDIF() {
		PQDIFConfig config = PQDIFConfigUtils.getPQDIFConfigDTO();

		List<String> devList = getDevices();
		if (devList == null) {
			logger.info("get device list fail. current task canceled.");
			return;
		}

		Set<String> ieds = Sets.newHashSet();
		for (String deviceId : devList) {
			getTargetFolder(deviceId);
			ieds.add(deviceId);
		}


		logger.info("everything is fine ,ready to generate pqdif file.");
		config.setDeviceIds(StringUtils.join(ieds, ";"));

		new PQDIFGeneratorExecutor().generatePQDIF_test(config);
	}

}
