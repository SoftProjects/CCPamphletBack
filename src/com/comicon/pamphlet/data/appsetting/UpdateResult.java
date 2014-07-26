package com.comicon.pamphlet.data.appsetting;

public class UpdateResult {
	UpdateResult(String updateCode,String appVersion){
		this.updateCode = updateCode;
		this.appVersion = appVersion;
	}
	private String updateCode;
	private String appVersion;
	public String getUpdateCode() {
		return updateCode;
	}
	public String getAppVersion() {
		return appVersion;
	}
}
