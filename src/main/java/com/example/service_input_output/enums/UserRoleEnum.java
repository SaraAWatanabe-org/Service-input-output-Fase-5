package com.example.service_input_output.enums;

public enum UserRoleEnum {

	USER("USER"),
	ADMIN("ADMIN"),
	;

	private String code;

	private UserRoleEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}

	public static UserRoleEnum toEnum(String code) {
		if (code == null) {
			return null;
		}

		for (UserRoleEnum gen : UserRoleEnum.values()) {
			if (code.equals(gen.getCode())) {
				return gen;
			}
		}

		throw null;
	}
}
