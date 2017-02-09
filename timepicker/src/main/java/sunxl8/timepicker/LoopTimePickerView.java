package sunxl8.timepicker;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import sunxl8.timepicker.adapter.LoopViewAdapter;
import sunxl8.timepicker.widget.LoopView;

/**
 * Created by sunxl8 on 2016/12/1.
 */

public class LoopTimePickerView extends LinearLayout implements View.OnClickListener, ILoopTimePicker {

    //时间格式
    public static final String DATE_FOMAT_PATTERN_ALL = "yyyy-MM-dd HH:mm";
    public static final String DATE_FOMAT_PATTERN_DATE = "yyyy/MM/dd";
    public static final String DATE_FOMAT_PATTERN_TIME = "HH:mm";
    //显示样式
    public static final int VIEW_MODE_ALL = 0;
    public static final int VIEW_MODE_DATE_ONLY = 1;
    public static final int VIEW_MODE_TIME_ONLY = 2;

    List<String> yearList = new ArrayList<>();
    List<String> monthList = new ArrayList<>();
    List<String> hourStartList = new ArrayList<>();
    List<String> minuteStartList = new ArrayList<>();
    List<String> hourEndList = new ArrayList<>();
    List<String> minuteEndList = new ArrayList<>();
    private int startYear = 1900;
    private int endYear = 2100;
    private int defaultYear;
    private int defaultMonth;
    private int defaultDay;
    private String defaultStartHour;
    private String defaultStartMinute;
    private String defaultEndHour;
    private String defaultEndMinute;

    private int viewMode = 0;

    private boolean dateViewExpand = false;
    private boolean timeStartViewExpand = false;
    private boolean timeEndViewExpand = false;
    //选择日期
    private LinearLayout layoutDate;
    private RelativeLayout layoutDateTitle;
    private TextView tvDateTitle;
    private TextView tvDateSelected;
    private LinearLayout layoutDateView;
    //选择开始时间
    private LinearLayout layoutTimeStart;
    private RelativeLayout layoutTimeStartTitle;
    private TextView tvTimeStartTitle;
    private TextView tvTimeStartSelected;
    private LinearLayout layoutTimeStartView;
    //选择结束时间
    private LinearLayout layoutTimeEnd;
    private RelativeLayout layoutTimeEndTitle;
    private TextView tvTimeEndTitle;
    private TextView tvTimeEndSelected;
    private LinearLayout layoutTimeEndView;

    private long startTimeSet;//设置的开始时间
    private long endTimeSet;//设置的结束时间

    private LoopView yearView;
    private LoopView monthView;
    private LoopView dayView;
    private LoopView timeStartHourView;
    private LoopView timeStartMinuteView;
    private LoopView timeEndHourView;
    private LoopView timeEndMinuteView;

    private View line;

    private Context mContext;

    public LoopTimePickerView(Context context) {
        super(context);
    }

    public LoopTimePickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;

        View mInflate = LayoutInflater.from(context).inflate(R.layout.viewloop_time_picker, this);

        layoutDate = (LinearLayout) mInflate.findViewById(R.id.ll_loop_picker_date);
        layoutDateTitle = (RelativeLayout) mInflate.findViewById(R.id.rl_loop_picker_date_title);
        tvDateTitle = (TextView) mInflate.findViewById(R.id.tv_loop_picker_date_title);
        tvDateSelected = (TextView) mInflate.findViewById(R.id.tv_loop_picker_date_title_selected);
        layoutDateView = (LinearLayout) mInflate.findViewById(R.id.ll_loop_picker_date_view);

        layoutTimeStart = (LinearLayout) mInflate.findViewById(R.id.ll_loop_picker_time_start);
        layoutTimeStartTitle = (RelativeLayout) mInflate.findViewById(R.id.rl_loop_picker_time_start_title);
        tvTimeStartTitle = (TextView) mInflate.findViewById(R.id.tv_loop_picker_time_start_title);
        tvTimeStartSelected = (TextView) mInflate.findViewById(R.id.tv_loop_picker_time_start_title_selected);
        layoutTimeStartView = (LinearLayout) mInflate.findViewById(R.id.ll_loop_picker_time_start_view);

        layoutTimeEnd = (LinearLayout) mInflate.findViewById(R.id.ll_loop_picker_time_end);
        layoutTimeEndTitle = (RelativeLayout) mInflate.findViewById(R.id.rl_loop_picker_time_end_title);
        tvTimeEndTitle = (TextView) mInflate.findViewById(R.id.tv_loop_picker_time_end_title);
        tvTimeEndSelected = (TextView) mInflate.findViewById(R.id.tv_loop_picker_time_end_title_selected);
        layoutTimeEndView = (LinearLayout) mInflate.findViewById(R.id.ll_loop_picker_time_end_view);

        yearView = (LoopView) mInflate.findViewById(R.id.loop_loop_picker_year);
        monthView = (LoopView) mInflate.findViewById(R.id.loop_loop_picker_month);
        dayView = (LoopView) mInflate.findViewById(R.id.loop_loop_picker_day);
        timeStartHourView = (LoopView) mInflate.findViewById(R.id.loop_loop_picker_start_hour);
        timeStartMinuteView = (LoopView) mInflate.findViewById(R.id.loop_loop_picker_start_minute);
        timeEndHourView = (LoopView) mInflate.findViewById(R.id.loop_loop_picker_end_hour);
        timeEndMinuteView = (LoopView) mInflate.findViewById(R.id.loop_loop_picker_end_minute);

        line = mInflate.findViewById(R.id.view_timepicker_line);

        initView();
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < LoopViewConfig.LOOP_ITEM_SIZE; i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    private void initView() {
        switch (viewMode) {
            case VIEW_MODE_ALL:
                layoutDate.setVisibility(View.VISIBLE);
                layoutTimeStart.setVisibility(View.VISIBLE);
                layoutTimeEnd.setVisibility(View.VISIBLE);
                break;
            case VIEW_MODE_DATE_ONLY:
                layoutDate.setVisibility(View.VISIBLE);
                layoutTimeStart.setVisibility(View.GONE);
                layoutTimeEnd.setVisibility(View.GONE);
                break;
            case VIEW_MODE_TIME_ONLY:
                layoutDate.setVisibility(View.GONE);
                layoutTimeStart.setVisibility(View.VISIBLE);
                layoutTimeEnd.setVisibility(View.GONE);
                break;
        }
        yearList.clear();
        for (int i = startYear; i < endYear + 1; i++) {
            yearList.add(i + "");
        }
        monthList.clear();
        for (int i = 1; i < 13; i++) {
            monthList.add(i + "");
        }
        LoopView.OnWheelItemSelectedListener dateListener = new LoopView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, String data) {
                dayView.setDateList(getDayList(yearView.getSelectionItem(), monthView.getSelectionItem()));
                defaultYear = Integer.parseInt(yearView.getSelectionItem());
                defaultMonth = Integer.parseInt(monthView.getSelectionItem());
                defaultDay = Integer.parseInt(dayView.getSelectionItem());
                tvDateSelected.setText(getDate());
            }
        };
        yearView.setDateList(yearList);
        yearView.setLoopAdapter(new LoopViewAdapter(mContext));
        yearView.setOnWheelItemSelectedListener(dateListener);

        monthView.setLoopAdapter(new LoopViewAdapter(mContext));
        monthView.setDateList(monthList);
        monthView.setOnWheelItemSelectedListener(dateListener);

        dayView.setLoopAdapter(new LoopViewAdapter(mContext));
        dayView.setDateList(getDayList(yearView.getSelectionItem(), monthView.getSelectionItem()));
        dayView.setOnWheelItemSelectedListener(dateListener);

        hourStartList.clear();
        hourEndList.clear();
        for (int i = 0; i < 24; i++) {
            if (i < 10) {
                hourStartList.add("0" + i);
                hourEndList.add("0" + i);
            } else {
                hourStartList.add(i + "");
                hourEndList.add(i + "");
            }
        }
        minuteStartList.clear();
        minuteEndList.clear();
        for (int i = 0; i < 60; i++) {
            if (i < 10) {
                minuteStartList.add("0" + i);
                minuteEndList.add("0" + i);
            } else {
                minuteStartList.add(i + "");
                minuteEndList.add(i + "");
            }
        }
        LoopView.OnWheelItemSelectedListener timeStartListener = new LoopView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, String data) {
                defaultStartHour = timeStartHourView.getSelectionItem();
                defaultStartMinute = timeStartMinuteView.getSelectionItem();
                tvTimeStartSelected.setText(getStartTime(DATE_FOMAT_PATTERN_TIME));
            }
        };
        timeStartHourView.setDateList(hourStartList);
        timeStartHourView.setLoopAdapter(new LoopViewAdapter(mContext));
        timeStartHourView.setOnWheelItemSelectedListener(timeStartListener);

        timeStartMinuteView.setDateList(minuteStartList);
        timeStartMinuteView.setLoopAdapter(new LoopViewAdapter(mContext));
        timeStartMinuteView.setOnWheelItemSelectedListener(timeStartListener);

        LoopView.OnWheelItemSelectedListener timeEndListener = new LoopView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, String data) {
                defaultEndHour = timeEndHourView.getSelectionItem();
                defaultEndMinute = timeEndMinuteView.getSelectionItem();
                tvTimeEndSelected.setText(getEndTime(DATE_FOMAT_PATTERN_TIME));
            }
        };
        timeEndHourView.setDateList(hourEndList);
        timeEndHourView.setLoopAdapter(new LoopViewAdapter(mContext));
        timeEndHourView.setOnWheelItemSelectedListener(timeEndListener);

        timeEndMinuteView.setDateList(minuteEndList);
        timeEndMinuteView.setLoopAdapter(new LoopViewAdapter(mContext));
        timeEndMinuteView.setOnWheelItemSelectedListener(timeEndListener);

        //默认值
        Calendar calendar = Calendar.getInstance();
        if (defaultYear == 0) {
            defaultYear = calendar.get(Calendar.YEAR);
        }
        if (defaultMonth == 0) {
            defaultMonth = calendar.get(Calendar.MONTH) + 1;

        }
        if (defaultDay == 0) {
            defaultDay = calendar.get(Calendar.DAY_OF_MONTH);

        }
        if (defaultStartHour == null) {
            defaultStartHour = "00";

        }
        if (defaultStartMinute == null) {
            defaultStartMinute = "00";

        }
        if (defaultEndHour == null) {
            defaultEndHour = "00";

        }
        if (defaultEndMinute == null) {
            defaultEndMinute = "00";
        }

        tvDateSelected.setText(getDate(DATE_FOMAT_PATTERN_DATE));
        layoutDateTitle.setOnClickListener(this);
        layoutTimeStartTitle.setOnClickListener(this);
        layoutTimeEndTitle.setOnClickListener(this);

        yearView.setSelection(yearList.indexOf(defaultYear + ""));
        monthView.setSelection(monthList.indexOf(defaultMonth + ""));
        dayView.setSelection(getDayList(yearView.getSelectionItem(), monthView.getSelectionItem()).indexOf(defaultDay + ""));
        timeStartHourView.setSelection(hourStartList.indexOf(defaultStartHour));
        timeStartMinuteView.setSelection(minuteStartList.indexOf(defaultStartMinute));
        timeEndHourView.setSelection(hourEndList.indexOf(defaultEndHour));
        timeEndMinuteView.setSelection(minuteEndList.indexOf(defaultEndMinute));

    }

    private void hideKeyboard() {
        View view = ((Activity) mContext).getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onClick(View v) {
        hideKeyboard();
        int i = v.getId();
        if (i == R.id.rl_loop_picker_date_title) {
            toggleDateView(!dateViewExpand);
            if (timeStartViewExpand) {
                toggleTimeStartView(false);
            }
            if (timeEndViewExpand) {
                toggleTimeEndView(false);
            }

        } else if (i == R.id.rl_loop_picker_time_start_title) {
            toggleTimeStartView(!timeStartViewExpand);
            if (dateViewExpand) {
                toggleDateView(false);
            }
            if (timeEndViewExpand) {
                toggleTimeEndView(false);
            }

        } else if (i == R.id.rl_loop_picker_time_end_title) {
            toggleTimeEndView(!timeEndViewExpand);
            if (timeStartViewExpand) {
                toggleTimeStartView(false);
            }
            if (dateViewExpand) {
                toggleDateView(false);
            }

        }
    }

    private void toggleDateView(boolean status) {
        if (status) {
            layoutDateView.setVisibility(View.VISIBLE);
            yearView.init();
            monthView.init();
            dayView.init();
            setListViewHeightBasedOnChildren(yearView);
            setListViewHeightBasedOnChildren(monthView);
            setListViewHeightBasedOnChildren(dayView);
        } else {
            layoutDateView.setVisibility(View.GONE);
        }
        dateViewExpand = status;
    }

    private void toggleTimeStartView(boolean status) {
        if (status) {
            layoutTimeStartView.setVisibility(View.VISIBLE);
            timeStartHourView.init();
            timeStartMinuteView.init();
            setListViewHeightBasedOnChildren(timeStartHourView);
            setListViewHeightBasedOnChildren(timeStartMinuteView);
        } else {
            layoutTimeStartView.setVisibility(View.GONE);
        }
        timeStartViewExpand = status;
    }

    private void toggleTimeEndView(boolean status) {
        if (status) {
            layoutTimeEndView.setVisibility(View.VISIBLE);
            timeEndHourView.init();
            timeEndMinuteView.init();
            setListViewHeightBasedOnChildren(timeEndHourView);
            setListViewHeightBasedOnChildren(timeEndMinuteView);
        } else {
            layoutTimeEndView.setVisibility(View.GONE);
        }
        timeEndViewExpand = status;
    }

    /**
     * 根据年月获取当月天数列表
     *
     * @param year
     * @param month
     * @return
     */
    private List<String> getDayList(String year, String month) {
        boolean flag = Integer.parseInt(year) % 4 == 0;
        List<String> listDay = new ArrayList<>();
        for (int i = 1; i < 32; i++) {
            listDay.add(i + "");
        }
        switch (Integer.parseInt(month)) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return listDay;
            case 2:
                return flag ? listDay.subList(0, 29) : listDay.subList(0, 28);
            default:
                return listDay.subList(0, 30);
        }
    }

    public long getStartTimeInMillis() {
        GregorianCalendar calendarStart = new GregorianCalendar(defaultYear, defaultMonth - 1, defaultDay,
                Integer.parseInt(defaultStartHour), Integer.parseInt(defaultStartMinute));
        return calendarStart.getTimeInMillis();
    }

    public long getEndTimeInMillis() {
        GregorianCalendar calendarEnd = new GregorianCalendar(defaultYear, defaultMonth - 1, defaultDay,
                Integer.parseInt(defaultEndHour), Integer.parseInt(defaultEndMinute));
        return calendarEnd.getTimeInMillis();
    }

    //==============================================================================================

    @Override
    public void setViewMode(int mode) {
        this.viewMode = mode;
        initView();
    }

    public void setLineVisible(int vis) {
        line.setVisibility(vis);
    }

    @Override
    public void setDateTitle(String title) {
        tvDateTitle.setText(title);
    }

    @Override
    public void setTimeTitle(String title) {
        tvTimeStartTitle.setText(title);
    }

    @Override
    public String getDate() {
        return getDate(DATE_FOMAT_PATTERN_DATE);
    }

    @Override
    public String getDate(String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        GregorianCalendar calendarStart = new GregorianCalendar(defaultYear, defaultMonth - 1, defaultDay);
        String time = sdf.format(calendarStart.getTime());
        return time;
    }

    @Override
    public String getTime() {
        return getTime(DATE_FOMAT_PATTERN_TIME);
    }

    @Override
    public String getTime(String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        GregorianCalendar calendarStart = new GregorianCalendar(defaultYear, defaultMonth - 1, defaultDay,
                Integer.parseInt(defaultStartHour), Integer.parseInt(defaultStartMinute));
        String time = sdf.format(calendarStart.getTime());
        return time;
    }

    @Override
    public String getStartTime() {
        return getStartTime(DATE_FOMAT_PATTERN_ALL);
    }

    @Override
    public String getStartTime(String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        GregorianCalendar calendarStart = new GregorianCalendar(defaultYear, defaultMonth - 1, defaultDay,
                Integer.parseInt(defaultStartHour), Integer.parseInt(defaultStartMinute));
        String time = sdf.format(calendarStart.getTime());
        return time;
    }

    @Override
    public String getEndTime() {
        return getEndTime(DATE_FOMAT_PATTERN_ALL);
    }

    @Override
    public String getEndTime(String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        GregorianCalendar calendarEnd = new GregorianCalendar(defaultYear, defaultMonth - 1, defaultDay,
                Integer.parseInt(defaultEndHour), Integer.parseInt(defaultEndMinute));
        String time = sdf.format(calendarEnd.getTime());
        return time;
    }

    @Override
    public void setDate(Date date) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        defaultYear = calendar.get(Calendar.YEAR);
        defaultMonth = calendar.get(Calendar.MONTH) + 1;
        defaultDay = calendar.get(Calendar.DAY_OF_MONTH);
        initView();
    }

    @Override
    public void setDate(int year, int month, int day) {
        defaultYear = year;
        defaultMonth = month;
        defaultDay = day;
        initView();
    }

    @Override
    public void setStartTime(Date date) {
        startTimeSet = date.getTime();
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(date.getTime());
        defaultYear = calendar.get(Calendar.YEAR);
        defaultMonth = calendar.get(Calendar.MONTH) + 1;
        defaultDay = calendar.get(Calendar.DAY_OF_MONTH);
        defaultStartHour = (calendar.get(Calendar.HOUR_OF_DAY) + "").length() == 2
                ? calendar.get(Calendar.HOUR_OF_DAY) + "" : "0" + calendar.get(Calendar.HOUR_OF_DAY);
        defaultStartMinute = (calendar.get(Calendar.MINUTE) + "").length() == 2
                ? calendar.get(Calendar.MINUTE) + "" : "0" + calendar.get(Calendar.MINUTE);
        initView();
        tvTimeStartSelected.setText(defaultStartHour + ":" + defaultStartMinute);
    }

    @Override
    public void setStartTime(int year, int month, int day, int hour, int minute) {
        defaultYear = year;
        defaultMonth = month;
        defaultDay = day;
        defaultStartHour = (hour + "").length() == 2 ? hour + "" : "0" + hour;
        defaultStartMinute = (minute + "").length() == 2 ? minute + "" : "0" + minute;
        initView();
        tvTimeStartSelected.setText(defaultStartHour + ":" + defaultStartMinute);
    }

    @Override
    public void setEndTime(Date date) {
        endTimeSet = date.getTime();
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(date.getTime());
        defaultYear = calendar.get(Calendar.YEAR);
        defaultMonth = calendar.get(Calendar.MONTH) + 1;
        defaultDay = calendar.get(Calendar.DAY_OF_MONTH);
        defaultEndHour = (calendar.get(Calendar.HOUR_OF_DAY) + "").length() == 2
                ? calendar.get(Calendar.HOUR_OF_DAY) + "" : "0" + calendar.get(Calendar.HOUR_OF_DAY);
        defaultEndMinute = (calendar.get(Calendar.MINUTE) + "").length() == 2
                ? calendar.get(Calendar.MINUTE) + "" : "0" + calendar.get(Calendar.MINUTE);
        initView();
        tvTimeEndSelected.setText(defaultEndHour + ":" + defaultEndMinute);
    }

    @Override
    public void setEndTime(int year, int month, int day, int hour, int minute) {
        defaultYear = year;
        defaultMonth = month;
        defaultDay = day;
        defaultEndHour = (hour + "").length() == 2 ? hour + "" : "0" + hour;
        defaultEndMinute = (minute + "").length() == 2 ? minute + "" : "0" + minute;
        initView();
        tvTimeEndSelected.setText(defaultEndHour + ":" + defaultEndMinute);
    }

}
