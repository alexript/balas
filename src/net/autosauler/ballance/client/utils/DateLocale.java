/*
 * Copyright 2006 Robert Hanson <iamroberthanson AT gmail.com>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.autosauler.ballance.client.utils;

import java.util.Arrays;
import java.util.List;

/**
 * Date locale support for the {@link SimpleDateParser}. You are encouraged to
 * extend this class and provide implementations for other locales.
 * 
 * @author <a href="mailto:g.georgovassilis@gmail.com">George Georgovassilis</a>
 * 
 */
public class DateLocale {

	/** The Constant TOKEN_DAY_OF_WEEK. */
	public final static String TOKEN_DAY_OF_WEEK = "E";

	/** The Constant TOKEN_DAY_OF_MONTH. */
	public final static String TOKEN_DAY_OF_MONTH = "d";

	/** The Constant TOKEN_MONTH. */
	public final static String TOKEN_MONTH = "M";

	/** The Constant TOKEN_YEAR. */
	public final static String TOKEN_YEAR = "y";

	/** The Constant TOKEN_HOUR_12. */
	public final static String TOKEN_HOUR_12 = "h";

	/** The Constant TOKEN_HOUR_24. */
	public final static String TOKEN_HOUR_24 = "H";

	/** The Constant TOKEN_MINUTE. */
	public final static String TOKEN_MINUTE = "m";

	/** The Constant TOKEN_SECOND. */
	public final static String TOKEN_SECOND = "s";

	/** The Constant TOKEN_MILLISECOND. */
	public final static String TOKEN_MILLISECOND = "S";

	/** The Constant TOKEN_AM_PM. */
	public final static String TOKEN_AM_PM = "a";

	/** The Constant AM. */
	public final static String AM = "AM";

	/** The Constant PM. */
	public final static String PM = "PM";

	/** The Constant SUPPORTED_DF_TOKENS. */
	public final static List<String> SUPPORTED_DF_TOKENS = Arrays
			.asList(new String[] { TOKEN_DAY_OF_WEEK, TOKEN_DAY_OF_MONTH,
					TOKEN_MONTH, TOKEN_YEAR, TOKEN_HOUR_12, TOKEN_HOUR_24,
					TOKEN_MINUTE, TOKEN_SECOND, TOKEN_AM_PM });

	/**
	 * Gets the aM.
	 * 
	 * @return the aM
	 */
	public static String getAM() {
		return AM;
	}

	/**
	 * Gets the pM.
	 * 
	 * @return the pM
	 */
	public static String getPM() {
		return PM;
	}

	/** The MONT h_ long. */
	public String[] MONTH_LONG = { "January", "February", "March", "April",
			"May", "June", "July", "August", "September", "October",
			"November", "December" };

	/** The MONT h_ short. */
	public String[] MONTH_SHORT = { "Jan", "Feb", "Mar", "Apr", "May", "Jun",
			"Jul", "Aug", "Sept", "Oct", "Nov", "Dec" };

	/** The WEEKDA y_ long. */
	public String[] WEEKDAY_LONG = { "Sunday", "Monday", "Tuesday",
			"Wednesday", "Thursday", "Friday", "Saturday" };

	/** The WEEKDA y_ short. */
	public String[] WEEKDAY_SHORT = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri",
			"Sat" };

	/**
	 * Gets the wEEKDA y_ long.
	 * 
	 * @return the wEEKDA y_ long
	 */
	public String[] getWEEKDAY_LONG() {
		return WEEKDAY_LONG;
	}

	/**
	 * Gets the wEEKDA y_ short.
	 * 
	 * @return the wEEKDA y_ short
	 */
	public String[] getWEEKDAY_SHORT() {
		return WEEKDAY_SHORT;
	}

}
