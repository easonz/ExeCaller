package org.eason.execaller.schedule;

import static org.eason.execaller.utils.PQDIFConstants.PQDIFPOF_INI;
import static org.eason.execaller.utils.PQDIFConstants.PQDIFPOF_SECT;

import java.io.File;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eason.execaller.utils.PQDIFConfig;
import org.eason.execaller.utils.PQDIFConfigUtils;
import org.eason.utils.FileUtils;
import org.eason.utils.IniUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

public abstract class AbstractJob {
	
	private static Logger logger = LoggerFactory.getLogger(AbstractJob.class);

	/**
	 * 从业务服务器获取现在在线的终端列表
	 * 
	 * @return
	 */
	protected List<String> getDevices(){

		PQDIFConfig config = PQDIFConfigUtils.getPQDIFConfigDTO();
		
		List<String> devList = Lists.newArrayList();
	
		return devList;
	}

	/**
	 * 获取给当前终端生成PQDIF文件的目录</br>
	 * 注：如果目录不存在则自动生成
	 * 
	 * @param ied
	 * @return
	 */
	protected File getTargetFolder(String ied) {

		PQDIFConfig config = PQDIFConfigUtils.getPQDIFConfigDTO();

		File pqdifFolder = new File(config.getPqdifFolder());
		if (!pqdifFolder.exists()) {
			pqdifFolder.mkdirs();
		}
		File deviceInfo = new File(config.getPqdifFolder() + File.separator
				+ PQDIFPOF_INI);
		if (!deviceInfo.exists()) {
			FileUtils.writeFile(deviceInfo, "[" + PQDIFPOF_SECT + "]");
		}
		IniUtils deviceFolderInfo = new IniUtils(deviceInfo.getAbsolutePath());
		String folderName = deviceFolderInfo.getValue(PQDIFPOF_SECT, ied);
		if (StringUtils.isEmpty(folderName)) {
			folderName = ied;
			logger.info("add {} node to ini file.", ied);
			deviceFolderInfo.setValue(PQDIFPOF_SECT, ied, folderName);
			logger.info("store ini file {}.", PQDIFPOF_INI);
			deviceFolderInfo.storeIni();
		}
		File deviceFolder = new File(pqdifFolder.getAbsolutePath()
				+ File.separator + folderName);
		if (!deviceFolder.exists()) {
			logger.info("make device pqdif dir {}.",
					deviceFolder.getAbsoluteFile());
			deviceFolder.mkdir();
		}

		return deviceFolder;
	}

}
