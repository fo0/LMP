package com.fo0.lmp.ui.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UIConfig implements IConfig {

	private String autoLogin = "false";

	@Override
	public boolean isValid() {
		return true;
	}

	public boolean isAutoLogin() {
		return Boolean.valueOf(autoLogin.toLowerCase());
	}

	public void setAutoLogin(boolean value) {
		autoLogin = String.valueOf(value);
	}

}
