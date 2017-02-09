package sunxl8.timepicker.widget;

import java.util.List;

import sunxl8.timepicker.adapter.LoopViewAdapter;


/**
 * Created by sunxl8 on 2016/12/6.
 */

public interface ILoopView {

    /**
     * 设置滚轮位置
     *
     * @param selection
     */
    void setSelection(int selection);

    /**
     * 获取当前滚轮位置
     *
     * @return
     */
    int getCurrentPosition();

    /**
     * 获取当前滚轮位置的数据
     *
     * @return
     */
    String getSelectionItem();

    /**
     * 设置滚轮数据源适配器
     *
     * @param adapter
     */
    void setLoopAdapter(LoopViewAdapter adapter);

    /**
     * 设置滚轮数据
     *
     * @param list
     */
    void setDateList(List<String> list);

    /**
     * 获得滚轮数据总数
     *
     * @return
     */
    int getLoopCount();



}
