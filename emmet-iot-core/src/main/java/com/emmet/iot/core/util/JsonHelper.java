package com.emmet.iot.core.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonHelper {
	public static <T> T JsonStringToObject(String string, Class<T> valueType) {

		ObjectMapper mapper = new ObjectMapper();
		try {

			T obj = mapper.readValue(string, valueType);
			return obj;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	public static String ObjectToJsonString(Object value) {
		ObjectMapper mapper = new ObjectMapper();

		String jsonString = null;
		try {
			jsonString = mapper.writeValueAsString(value);
			return jsonString;
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonString;

	}
}
