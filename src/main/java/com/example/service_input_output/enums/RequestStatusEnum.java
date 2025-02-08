package com.example.service_input_output.enums;

public enum RequestStatusEnum {

	WAITING_PROCESS("WAITING_PROCESS"),
	PROCESSING("PROCESSING"),
	FINISHED("FINISHED"),
	ERROR("ERROR")
	;

	private String code;

	private RequestStatusEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}

	public static RequestStatusEnum toEnum(String code) {
		if (code == null) {
			return null;
		}

		for (RequestStatusEnum gen : RequestStatusEnum.values()) {
			if (code.equals(gen.getCode())) {
				return gen;
			}
		}

		throw null;
	}
}
