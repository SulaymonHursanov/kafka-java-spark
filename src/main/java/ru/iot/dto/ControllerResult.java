package ru.iot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ControllerResult {
	private String message;
	private boolean success;

	public static ControllerResult successResult(String message) {
		ControllerResult result = new ControllerResult();
		result.success = true;
		result.message = message;
		return result;
	}

	public static ControllerResult failResult(String message) {
		ControllerResult result = new ControllerResult();
		result.success = false;
		result.message = message;
		return result;
	}

}
