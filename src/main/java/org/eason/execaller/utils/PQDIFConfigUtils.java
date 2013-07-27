package org.eason.execaller.utils;

import java.io.File;

import org.eason.utils.BeanMapper;
import org.eason.utils.IniUtils;
import org.eason.utils.RescourcesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PQDIFConfigUtils {

	private static Logger logger = LoggerFactory.getLogger(PQDIFConfigUtils.class);
	private final static String PQDIF_CFG_FILE = "pqdif-generate-cfg.ini";
	private final static String SECT_NAME = "generate-config";
	private static IniUtils iniUtils = null;
	private static File configFile = null;
	private static PQDIFConfig configDTO = null;
	private static long fileLastModified = 0L;

	static {
		// configFile = new File(PQDIF_CFG_FILE);
		configFile = RescourcesUtils.getFile(PQDIF_CFG_FILE);
		configDTO = new PQDIFConfig();
	}

	public PQDIFConfigUtils() {

	}

	public static void reloadCfg() {

		iniUtils = new IniUtils(configFile.getAbsolutePath());
		fileLastModified = configFile.lastModified();

		configDTO.setUserName(iniUtils.getValue(SECT_NAME, "userName", ""));
		configDTO.setPassWord(iniUtils.getValue(SECT_NAME, "passWord", ""));
		configDTO.setAutoGenerate(iniUtils.getBoolean(SECT_NAME, "autoGenerate", false));
		configDTO.setGenerateInterval(iniUtils.getInteger(SECT_NAME, "generateInterval", 2));
		configDTO.setAutoDelete(iniUtils.getBoolean(SECT_NAME, "autoDelete", false));
		configDTO.setAutoDeleteInterval(iniUtils.getInteger(SECT_NAME, "autoDeleteInterval", 2));
		configDTO.setAutoDetective(iniUtils.getBoolean(SECT_NAME, "autoDetective", false));
		configDTO.setAutoDetectiveInterval(iniUtils.getInteger(SECT_NAME, "autoDetectiveInterval", 2));
		configDTO.setDefaultSaveTime(iniUtils.getInteger(SECT_NAME, "defaultSaveTime", 7));
		configDTO.setPqdifFolder(iniUtils.getValue(SECT_NAME, "pqDifFolder", ""));
		configDTO.setItems(iniUtils.getValue(SECT_NAME, "items", ""));
		configDTO.setDeviceIds(iniUtils.getValue(SECT_NAME, "deviceIds", ""));
		configDTO.setGeneratorExePath(iniUtils.getValue(SECT_NAME, "generatorExePath", ""));
	}

	public static PQDIFConfig getPQDIFConfigDTO() {
		if (configFile.lastModified() > fileLastModified) {
			reloadCfg();
		}
		PQDIFConfig tmpDto = new PQDIFConfig();
		BeanMapper.copy(configDTO, tmpDto);
		return tmpDto;
	}
}
