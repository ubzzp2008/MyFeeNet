/**
 * #  *  DateUtil.java Create on 2010-12-28 上午09:34:24
 * #  *
 * #  * Copyright (c) 2010 by biz.
 * #
 * 文件名：DateUtil.java
 * <p>
 * 版本信息：
 * 日期：2010-12-28
 * Copyright  Corporation 2010
 * 版权所有
 * <p>
 * 文件名：DateUtil.java
 * <p>
 * 版本信息：
 * 日期：2010-12-28
 * Copyright  Corporation 2010
 * 版权所有
 */
/**
 * 文件名：DateUtil.java  
 *
 * 版本信息：  
 * 日期：2010-12-28  
 * Copyright  Corporation 2010   
 * 版权所有  
 *
 */
package com.biz.justin.myFeeNet.util;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    private static final String FORMAT_STRING = "yyyy-MM-dd";
    private static final String FORMAT_STRING_SS = "yyyy-MM-dd HH:mm:ss";


    /**
     * 打印参数formatString-"yyyy-MM-dd" 格式的当前日期的Utils方法.
     *
     * @return the string
     */
    public static String formatCurrentDate() {
        DateTime dateTime = new DateTime();

        return formatDateTimeDate(dateTime.toString());
    }


    /**
     * 当前时间 格式化为yyyy-MM-dd HH:mm:ss
     **/
    public static String formatDateTimeDate() {
        DateTime dt = new DateTime();
        String d = dt.toString(FORMAT_STRING_SS);
        return d;
    }

    /**
     * 打印yyyy-MM-dd格式的dateime日期时间串的Utils方法.
     *
     * @param dateTime
     *            the date time
     * @return the string
     */
    public static String formatDateTimeDate(String dateTime) {
        DateTime dt = new DateTime(dateTime);
        String d = dt.toString(FORMAT_STRING);
        return d;
    }

    public static String date2String(Date objDate) {
        SimpleDateFormat objFormat = new SimpleDateFormat(FORMAT_STRING);
        return objFormat.format(objDate);
    }

    public static String date2String(Date objDate, String strFormat) {
        SimpleDateFormat objFormat = new SimpleDateFormat(strFormat);
        return objFormat.format(objDate);
    }

}
