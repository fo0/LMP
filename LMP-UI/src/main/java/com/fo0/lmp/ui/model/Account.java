package com.fo0.lmp.ui.model;

import com.fo0.lmp.ui.utils.ETheme;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = { "name", "password" })
public class Account {

	private String name;
	private String password;

	private ETheme theme;

}
