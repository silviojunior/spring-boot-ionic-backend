package com.codewaiter.cursomc.resources.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class URL {

	public static String decodeParam(String param) {
		try {
			return URLDecoder.decode(param, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}
	
	public static List<Integer> decodeIntList(String paramList){
		
		String[] paramVet = paramList.split(",");
		List<Integer> intList = new ArrayList<>();
		
		for(int i = 0; i < paramVet.length; i++) {
			intList.add(Integer.parseInt(paramVet[i]));
		}
		
		return intList;
		
		/**
		 * Uma outra abordagem
		 * 
		 *	return Arrays.asList(paramList.split(",")).stream().map(param -> Integer.parseInt(param)).collect(Collectors.toList());
		 */
	}
}
