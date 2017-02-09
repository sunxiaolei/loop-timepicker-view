package sunxl8.timepicker;

import java.util.Date;

/**
 * Created by sunxl8 on 2016/12/7.
 */

public interface ILoopTimePicker {

    /**
     * 设置view显示模式
     *
     * @param mode 0：日期+开始时间+结束时间 1：日期 2：时间
     */
    void setViewMode(int mode);

    /**
     * 设置日期标题
     *
     * @param title
     */
    void setDateTitle(String title);

    /**
     * 设置时间标题
     *
     * @param title
     */
    void setTimeTitle(String title);

    /**
     * 只获取日期(mode：1)
     *
     * @return 返回默认格式:yyyy/MM/dd
     */
    String getDate();

    /**
     * 只获取日期(mode：1)
     *
     * @param pattern 格式：例 yyyy/MM/dd
     * @return 返回指定格式
     */
    String getDate(String pattern);

    /**
     * 只获取时间(mode：2)
     * @return 返回默认格式:HH:mm
     */
    String getTime();

    /**
     * 只获取时间(mode：2)
     *
     * @param pattern 格式：例 HH:mm
     * @return 返回指定格式
     */
    String getTime(String pattern);

    /**
     * 获取开始时间
     * @return 返回默认格式:yyyy/MM/dd HH:mm
     */
    String getStartTime();

    /**
     * 获取开始时间
     *
     * @param pattern 格式：例 yyyy/MM/dd HH:mm
     * @return 返回指定格式
     */
    String getStartTime(String pattern);

    /**
     * 获取结束时间
     * @return 返回默认格式:yyyy/MM/dd HH:mm
     */
    String getEndTime();

    /**
     * 获取结束时间
     *
     * @param pattern 格式：例 yyyy/MM/dd HH:mm
     * @return 返回指定格式
     */
    String getEndTime(String pattern);

    /**
     * 设置默认日期
     *
     * @param date
     */
    void setDate(Date date);

    /**
     * 设置默认日期
     *
     * @param year
     * @param month
     * @param day
     */
    void setDate(int year, int month, int day);

    /**
     * 设置默认开始时间
     *
     * @param date
     */
    void setStartTime(Date date);

    /**
     * 设置默认开始时间
     *
     * @param year
     * @param month
     * @param day
     * @param hour
     * @param minute
     */
    void setStartTime(int year, int month, int day, int hour, int minute);

    /**
     * 设置默认结束时间
     *
     * @param date
     */
    void setEndTime(Date date);

    /**
     * 设置默认结束时间
     *
     * @param year
     * @param month
     * @param day
     * @param hour
     * @param minute
     */
    void setEndTime(int year, int month, int day, int hour, int minute);

}
