package org.eason.execaller.schedule;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.eason.execaller.utils.PQDIFConfig;
import org.eason.utils.DateFormater;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class PQDIFGeneratorExecutor {
	private static Logger logger = LoggerFactory.getLogger(PQDIFGeneratorExecutor.class);
	
	private static String exePath = "";
	private static String exeName = "PQDifGenerate.exe";
	private static Process process = null;

	public PQDIFGeneratorExecutor() {
		exePath = "" + exeName;
	}

	public void execute(){
		logger.info("executer executed.");
	}
	
	public void generatePQDIF(PQDIFConfig config) {
		File file = null;

		try {
			file = new File(config.getPqdifFolder());
			if (!file.exists()) {
				file.mkdirs();
			}
			
			String beginTime = DateFormater.getDateAndTimeStr(new Date());

			String[] cmdarray = { exePath, config.getUserName(),
					config.getPassWord(), beginTime,
					String.valueOf(config.getGenerateInterval()),
					config.getDeviceIds(), config.getItems(),
					file.getCanonicalPath() };

			logger.info(StringUtils.join(cmdarray, "\r\n"));

			process = Runtime.getRuntime().exec(cmdarray);

			BufferedReader br = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
			String line = null;
			while ((line = br.readLine()) != null) {
				logger.info("read from app: " + line);
			}
			process.waitFor();

		} catch (Throwable e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		} finally {
			if (process != null) {
				process.destroy();
			}
		}
	}

	public void generatePQDIF_test(PQDIFConfig config) {

		try {

			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			Calendar c = Calendar.getInstance();
			// 计算当前触发点时间
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			int timeCompensate = c.get(Calendar.HOUR_OF_DAY)
					% config.getGenerateInterval();
			c.add(Calendar.HOUR_OF_DAY, -timeCompensate);
			Date endDate = c.getTime();

			// 前触发点时间之前N个小时
			c.add(Calendar.HOUR_OF_DAY, -config.getGenerateInterval());
			Date startDate = c.getTime();

			String startDateStr = sdf.format(startDate);
			String endDateStr = sdf.format(endDate);
			File exeFile = new File(config.getGeneratorExePath());
			if (!exeFile.exists()) {
				logger.error("PQGenerator exe not exist. except path is {}",
						exeFile.getAbsolutePath());
				return;
			}

			// string user 当前操作的用户名
			// string passWord 密码
			// string ieds 设备名，以分号分隔
			// string ids 设备项字符串，以分号分隔（如果为空则包含所有项）
			// int statType 数据统计类型,取值和对应信息如下:
			// 0 - 间隔为1分钟
			// 1 - 间隔为1小时
			// 2 - 间隔为1天
			// 3 - 间隔为1月
			// string startTime 开始时间（yyyyMMddHHmmss）
			// string endTime 结束时间（yyyyMMddHHmmss）
			// string pqdifFolder PQDif总父目录

			String[] cmdarray = { exeFile.getCanonicalPath(),
					config.getUserName(), config.getPassWord(),
					config.getDeviceIds(), getItems(), String.valueOf(0),
					startDateStr, endDateStr,
					config.getPqdifFolder() };



			// String iniPath = config.getPqdifFolder() + "/pqdifpof.ini";
			// IniUtils pqdifpofIni = new IniUtils(iniPath);
			//
			// String[] ieds = StringUtils.split(config.getDeviceIds(), ";");
			// for (String ied : ieds) {
			// File deviceFolder = new File(config.getPqdifFolder()
			// + File.separator + ied);
			// String deviceFolderName =
			// pqdifpofIni.getValue("DIRECTORYMAP", ied);
			// String pqdifFileName = DateFormater.getDateAndTimeStr(
			// startDate, "yyyyMMdd'T'HHmmss'.pqd'");
			// String pqdifFilePath = String.format("%s/%s/%s",
			// config.getPqdifFolder(), deviceFolderName, pqdifFileName);
			// logger.info("target PQDIF file path : {}", pqdifFilePath);
			// File pqdifFile = new File(pqdifFilePath);
			// if (pqdifFile.exists()) {
			// logger.info("file {} already exist.", pqdifFilePath);
			// } else {
			// logger.info("generate PQDIF file {} .", pqdifFilePath);
			// FileUtils.writeFile(pqdifFile, pqdifFilePath);
			// }
			//
			// }
			logger.info("execute cmd: [{}]", StringUtils.join(cmdarray, " ,"));
			// process = Runtime.getRuntime().exec(cmdarray);
			File exeParentDir = exeFile.getCanonicalFile().getParentFile();
			process = Runtime.getRuntime().exec(cmdarray, null, exeParentDir);

			BufferedReader br = new BufferedReader(new InputStreamReader(
					process.getInputStream()));

			String line = null;
			while ((line = br.readLine()) != null) {
				logger.info("read from app: " + line);
			}

			process.waitFor();

		} catch (Throwable e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		} finally {
			if (process != null) {
				process.destroy();
			}
		}
	}

	private String getItems() {
		StringBuilder sb = new StringBuilder();
		for (int i = 10000; i < 10202; i++) {
			sb.append(i).append(";");
		}
		return sb.toString();
	}

}
