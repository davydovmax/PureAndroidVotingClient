package com.fh.voting.parsers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeConverter {
	static private final String format = "yyyy-MM-dd'T'HH:mm:ss.SSS";
	static private final String displayFormat = "HH:mm, dd.MM.yy";
	static private final int length = 23;
	static private final int lengthShort = 19;

	static public Date parse(String isoDateTime) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		if (isoDateTime.length() > length) {
			isoDateTime = isoDateTime.substring(0, length);
		} else if (isoDateTime.length() == lengthShort) {
			isoDateTime = isoDateTime.concat(".000");
		}
		return sdf.parse(isoDateTime);
	}

	static public String toString(Date dateTime) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(dateTime);
	}

	static public String toDisplayString(Date dateTime) {
		SimpleDateFormat sdf = new SimpleDateFormat(displayFormat);
		return sdf.format(dateTime);
	}
}
