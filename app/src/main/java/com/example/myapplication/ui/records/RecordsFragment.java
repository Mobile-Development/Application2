package com.example.myapplication.ui.records;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.myapplication.R;
import com.example.myapplication.adapter.SideSlipAdapter;
import com.example.myapplication.model.Account;
import com.example.myapplication.model.PersonInfo;
import com.example.myapplication.model.PersonSportsInfo;
import com.example.myapplication.ui.dialog.DialogActivity;
import com.example.myapplication.utils.DatabaseUtil;
import com.example.myapplication.utils.TypeUtil;
import com.necer.calendar.BaseCalendar;
import com.necer.calendar.Miui10Calendar;
import com.necer.entity.CalendarDate;
import com.necer.entity.Lunar;
import com.necer.listener.OnCalendarChangedListener;
import com.necer.utils.CalendarUtil;
import com.nightonke.boommenu.BoomButtons.BoomButton;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.TextInsideCircleButton;
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.OnBoomListener;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

import org.joda.time.LocalDate;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import static com.necer.enumeration.SelectedModel.SINGLE_SELECTED;

public class RecordsFragment extends Fragment {

    List<PersonSportsInfo> personSportsInfos;
     RecordsViewModel recordsViewModel;
    private Miui10Calendar miui10Calendar;
    private TextView tv_result;

    private ListView lsv_side_slip_delete;
    private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    private SideSlipAdapter adapter;
    private BoomMenuButton bmb;
    private Integer index;
    private Integer mdoPos;
    private boolean flag = true;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        recordsViewModel =
                ViewModelProviders.of(this).get(RecordsViewModel.class);
        personSportsInfos = new ArrayList<PersonSportsInfo>();
        View root = inflater.inflate(R.layout.fragment_records, container, false);
        calendarInit(root);
        initAdapter();
        initData();
        buildButtons();
        return root;
    }

    protected void calendarInit(View root) {

        tv_result = root.findViewById(R.id.tv_result);

        bmb = (BoomMenuButton) root.findViewById(R.id.bmb);
        assert bmb != null;
        lsv_side_slip_delete = root.findViewById(R.id.lsv_side_slip_delete);

        miui10Calendar = root.findViewById(R.id.miui10Calendar);
        miui10Calendar.setSelectedMode(SINGLE_SELECTED);
        miui10Calendar.toWeek();

        setListener();
    }
    private void setListener(){
        miui10Calendar.setOnCalendarChangedListener(new OnCalendarChangedListener() {
            @Override
            public void onCalendarChange(BaseCalendar baseCalendar, int year, int month, LocalDate localDate) {
                tv_result.setText(localDate.toString());

                if (localDate != null) {
                    CalendarDate calendarDate = CalendarUtil.getCalendarDate(localDate);
                    Lunar lunar = calendarDate.lunar;
                    //updateList(localDate);
                    //tv_data.setText(localDate.toString("yyyy年MM月dd日"));
                    //tv_desc.setText(lunar.chineseEra + lunar.animals + "年" + lunar.lunarMonthStr + lunar.lunarDayStr);
                } else {
                    //tv_data.setText("");
                    //tv_desc.setText("");
                }
            }
        });
    }

    private void initAdapter(){
        // 创建adapter，listview设置adapter
        adapter = new SideSlipAdapter(getActivity(), list);
        lsv_side_slip_delete.setAdapter(adapter);

        if (adapter != null){
            // 注册监听器,回调用来刷新数据显示
            adapter.setDelItemListener(new SideSlipAdapter.DeleteItem() {
                @Override
                public void delete(int pos) {
                    list.remove(pos);
                    adapter.notifyDataSetChanged();
                }
            });
            adapter.setModItemListener(new SideSlipAdapter.ModifyItem() {
                @Override
                public void modify(int pos) {
                    flag = false;
                    mdoPos = pos;
                    Intent intent = new Intent(getActivity(), DialogActivity.class);
                    intent.putExtra("kind",pos);
                    intent.putExtra("data",list.get(pos).get("time").toString());
                    startActivityForResult(intent,0);
                }
            });
        }
    }
    public void refreshList(){   //刷新列表
        Map<String, Object> map =null;
        Log.i("zxc refresh","personSportsInfos"+personSportsInfos.size());
        if(personSportsInfos.size()!=0){
            for(int i=0;i<personSportsInfos.size();i++) {
                map = new HashMap<String, Object>();
                map.put("time", personSportsInfos.get(i).getDuration());
                map.put("cal", personSportsInfos.get(i).getCalorie());
                map.put("img", personSportsInfos.get(i).getType());////
                list.add(map);
            }
            adapter.notifyDataSetChanged();
        }
    }


    private void buildButtons(){
        bmb.setButtonEnum(ButtonEnum.TextInsideCircle);
        bmb.setButtonEnum(ButtonEnum.TextOutsideCircle);
        bmb.setPiecePlaceEnum(PiecePlaceEnum.DOT_9_1);
        bmb.setButtonPlaceEnum(ButtonPlaceEnum.SC_9_1);
        bmb.setNormalColor(R.color.green00);
        addBuilders();

        bmb.setOnBoomListener(new OnBoomListener() {
            @Override
            public void onClicked(int i, BoomButton boomButton) {
                index = i;
                flag = true;
                Intent intent = new Intent(getActivity(), DialogActivity.class);
                //intent.putExtra("kind",index);
                intent.putExtra("data","1");
                startActivityForResult(intent,0);
                //startActivity(intent);
            }
            @Override
            public void onBackgroundClick() { }

            @Override
            public void onBoomWillHide() { }

            @Override
            public void onBoomDidHide() { }

            @Override
            public void onBoomWillShow() { }

            @Override
            public void onBoomDidShow() { }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Integer time = Integer.parseInt(data.getStringExtra("data"));
        if(flag){
            PersonSportsInfo personSportsInfo = new PersonSportsInfo();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("time", data.getIntExtra("exeLength",0));
            map.put("cal","3800" );
            map.put("img", TypeUtil.indexToType(index));
            list.add(map);
            personSportsInfo.setType(TypeUtil.indexToType(index));
            personSportsInfo.setCalorie("3800");
            personSportsInfo.setDuration(data.getIntExtra("exeLength",0));
            DatabaseUtil.InsertPersonSportsInfoRequest(personSportsInfo,this);
            adapter.notifyDataSetChanged();
        }
        else{
            list.get(mdoPos).replace("time", data.getStringExtra("exeLength"));
            adapter.notifyDataSetChanged();
        }
    }

    private void initData() {
        DatabaseUtil.SearchPersonSportsInfoByIdRequest(personSportsInfos, 1,this);

    }

    private void addBuilders(){
        TextOutsideCircleButton.Builder builder0 = new TextOutsideCircleButton.Builder()
                .normalImageRes(R.drawable.ic_swim_24dp)
                .normalText("Swimming");
        bmb.addBuilder(builder0);
        TextOutsideCircleButton.Builder builder1 = new TextOutsideCircleButton.Builder()
                .normalImageRes(R.drawable.ic_cycling)
                .normalText("Cycling");
        bmb.addBuilder(builder1);
        TextOutsideCircleButton.Builder builder2 = new TextOutsideCircleButton.Builder()
                .normalImageRes(R.drawable.ic_basketball)
                .normalText("Basketball");
        bmb.addBuilder(builder2);
        TextOutsideCircleButton.Builder builder3 = new TextOutsideCircleButton.Builder()
                .normalImageRes(R.drawable.ic_tennis)
                .normalText("Tennis");
        bmb.addBuilder(builder3);
        TextOutsideCircleButton.Builder builder4 = new TextOutsideCircleButton.Builder()
                .normalImageRes(R.drawable.ic_ski)
                .normalText("Climbing");
        bmb.addBuilder(builder4);
        TextOutsideCircleButton.Builder builder5 = new TextOutsideCircleButton.Builder()
                .normalImageRes(R.drawable.ic_running)
                .normalText("Running");
        bmb.addBuilder(builder5);
        TextOutsideCircleButton.Builder builder6 = new TextOutsideCircleButton.Builder()
                .normalImageRes(R.drawable.ic_volleyball)
                .normalText("Volleyball");
        bmb.addBuilder(builder6);
        TextOutsideCircleButton.Builder builder7 = new TextOutsideCircleButton.Builder()
                .normalImageRes(R.drawable.sport_net)
                .normalText("Badminton");
        bmb.addBuilder(builder7);
        TextOutsideCircleButton.Builder builder8 = new TextOutsideCircleButton.Builder()
                .normalImageRes(R.drawable.ic_skateboard)
                .normalText("Skateboard");
        bmb.addBuilder(builder8);
    }
}