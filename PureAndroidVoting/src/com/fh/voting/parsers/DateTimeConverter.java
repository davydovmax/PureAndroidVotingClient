package com.fh.voting.parsers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeConverter {
	static private String format = "yyyy-MM-dd'T'HH:mm:ss.SSS";
	
	static public Date Parse(String isoDateTime) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.parse(isoDateTime.substring(0, isoDateTime.length() - 3));
	}
}
