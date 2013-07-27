package org.eason.execaller.utils;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class PQDIFConfig {

	private int generateInterval;
	private int defaultSaveTime;
	private boolean autoDelete;
	private int autoDeleteInterval;
	private boolean autoGenerate;
	private int autoDetectiveInterval;
	private boolean autoDetective;
	private String pqdifFolder;
	private String items;
	private String deviceIds;
	private String userName;
	private String passWord;
	private String generatorExePath;

	public int getGenerateInterval() {
		return generateInterval;
	}

	public void setGenerateInterval(int generateInterval) {
		this.generateInterval = generateInterval;
	}

	public int getDefaultSaveTime() {
		return defaultSaveTime;
	}

	public void setDefaultSaveTime(int defaultSaveTime) {
		this.defaultSaveTime = defaultSaveTime;
	}

	public boolean isAutoDelete() {
		return autoDelete;
	}

	public void setAutoDelete(boolean autoDelete) {
		this.autoDelete = autoDelete;
	}

	public int getAutoDeleteInterval() {
		return autoDeleteInterval;
	}

	public void setAutoDeleteInterval(int autoDeleteInterval) {
		this.autoDeleteInterval = autoDeleteInterval;
	}

	public boolean isAutoGenerate() {
		return autoGenerate;
	}

	public void setAutoGenerate(boolean autoGenerate) {
		this.autoGenerate = autoGenerate;
	}

	public String getPqdifFolder() {
		return pqdifFolder;
	}

	public void setPqdifFolder(String pqdifFolder) {
		this.pqdifFolder = pqdifFolder;
	}

	public String getItems() {
		return items;
	}

	public void setItems(String items) {
		this.items = items;
	}

	public String getDeviceIds() {
		return deviceIds;
	}

	public void setDeviceIds(String deviceIds) {
		this.deviceIds = deviceIds;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param passWord the passWord to set
	 */
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	/**
	 * @return the passWord
	 */
	public String getPassWord() {
		return passWord;
	}

	/**
	 * @param generatorExePath the generatorExePath to set
	 */
	public void setGeneratorExePath(String generatorExePath) {
		this.generatorExePath = generatorExePath;
	}

	/**
	 * @return the generatorExePath
	 */
	public String getGeneratorExePath() {
		return generatorExePath;
	}

	/**
	 * @param autoDetective the autoDetective to set
	 */
	public void setAutoDetective(boolean autoDetective) {
		this.autoDetective = autoDetective;
	}

	/**
	 * @return the autoDetective
	 */
	public boolean isAutoDetective() {
		return autoDetective;
	}

	/**
	 * @param autoDetectiveInterval the autoDetectiveInterval to set
	 */
	public void setAutoDetectiveInterval(int autoDetectiveInterval) {
		this.autoDetectiveInterval = autoDetectiveInterval;
	}

	/**
	 * @return the autoDetectiveInterval
	 */
	public int getAutoDetectiveInterval() {
		return autoDetectiveInterval;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}