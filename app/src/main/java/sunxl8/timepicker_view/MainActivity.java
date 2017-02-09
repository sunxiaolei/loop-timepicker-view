package sunxl8.timepicker_view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import sunxl8.timepicker.LoopTimePickerView;

public class MainActivity extends AppCompatActivity {

    LoopTimePickerView view1, view2, view3, view4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        view1 = (LoopTimePickerView) findViewById(R.id.view1);
        view2 = (LoopTimePickerView) findViewById(R.id.view2);
        view3 = (LoopTimePickerView) findViewById(R.id.view3);
        view4 = (LoopTimePickerView) findViewById(R.id.view4);

        view1.setViewMode(LoopTimePickerView.VIEW_MODE_DATE_ONLY);
        view1.setDateTitle("只显示日期");
        view1.setDate(2000, 3, 3);

        view2.setViewMode(LoopTimePickerView.VIEW_MODE_TIME_ONLY);
        view2.setTimeTitle("只显示时间");
        view2.setStartTime(2000, 3, 3, 6, 50);

        view3.setViewMode(LoopTimePickerView.VIEW_MODE_ALL);
        view3.setDateTitle("都显示");
        view3.setDate(2000, 3, 3);
        view3.setStartTime(2000, 3, 3, 6, 50);
        view3.setEndTime(2000, 3, 3, 9, 30);

        view4.setDateTitle("默认");
    }
}
