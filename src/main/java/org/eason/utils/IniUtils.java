package org.eason.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.NoSuchElementException;

import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;
import org.ini4j.Profile.Section;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IniUtils {

	private static Logger logger = LoggerFactory.getLogger(IniUtils.class);
	private File iniFile = null;
	private long lastModifyTime = 0;
	private Ini ini = null;

	public IniUtils() {

	}

	public IniUtils(String filePath) {
		loadIni(filePath);
	}

	private void initIni() {
		long tmpLastModifyTime = iniFile.lastModified();
		if (lastModifyTime < tmpLastModifyTime) {
			lastModifyTime = tmpLastModifyTime;
			try {
				logger.info("load ini config from  : {}", iniFile.getAbsolutePath());
				ini = new Ini(new InputStreamReader(new FileInputStream(iniFile), "UTF-8"));
			} catch (InvalidFileFormatException e) {
				logger.info(e.getMessage());
				e.printStackTrace();
			} catch (IOException e) {
				logger.info(e.getMessage());
				e.printStackTrace();
			}
		}
	}

	public void loadIni(String filePath) {
		iniFile = new File(filePath);
		if (!iniFile.exists()) {
			logger.info("file not exist : {}", iniFile.getAbsolutePath());
			throw new RuntimeException("file : " + filePath + " not exist.");
		}
		initIni();
	}

	public void storeIni() {
		try {
			ini.store(new OutputStreamWriter(new FileOutputStream(iniFile), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		}
	}

	public Section getSect(String sect) {
		return ini.get(sect);
	}

	public Section addSect(String sect) {
		return ini.add(sect);
	}

	public String getValue(String sect, String key) {
		Section section = getSect(sect);
		if (section == null) {
			throw new NoSuchElementException("has no section : " + sect);
		}
		return section.get(key);
	}

	public String getValue(String sect, String key, String defaultValue) {
		String value = getValue(sect, key);
		if (value == null) {
			value = defaultValue;
		}
		return value;
	}

	public Integer getInteger(String sect, String key) {
		return Integer.valueOf(getValue(sect, key));
	}

	public Integer getInteger(String sect, String key, Integer defaultValue) {
		return Integer.valueOf(getValue(sect, key, String.valueOf(defaultValue)));
	}

	public Double getDouble(String sect, String key) {
		return Double.valueOf(getValue(sect, key));
	}
	
	public Double getDouble(String sect, String key, Double defaultValue) {
		return Double.valueOf(getValue(sect, key, String.valueOf(defaultValue)));
	}

	public Boolean getBoolean(String sect, String key) {
		return Boolean.valueOf(getValue(sect, key));
	}
	
	public Boolean getBoolean(String sect, String key, Boolean defaultValue) {
		return Boolean.valueOf(getValue(sect, key, String.valueOf(defaultValue)));
	}

	public void setValue(String sect, String key, String value) {
		Section section = ini.get(sect);
		if (section == null) {
			section = ini.add(sect);
		}
		section.add(key, value);
	}
}
