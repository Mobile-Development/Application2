package com.example.myapplication.ui.setting;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.myapplication.R;
import com.example.myapplication.model.Account;
import com.example.myapplication.model.HealthData;
import com.example.myapplication.data.AthleticData;
import com.example.myapplication.data.HealthRecord;
import com.example.myapplication.model.PersonInfo;
import com.example.myapplication.utils.PhysicalUtil;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import devlight.io.library.ArcProgressStackView;

public class SettingFragment extends Fragment {

    private SettingViewModel settingViewModel;
    private ListView listview;
    private List<String> dataList = null;
    private TableRow tableRow1;
    private TableRow tableRow2;
    private TextView anal_text;
    private TextView username_text;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        settingViewModel =
                ViewModelProviders.of(this).get(SettingViewModel.class);
        View root = inflater.inflate(R.layout.fragment_setting, container, false);
        tableRow1 = root.findViewById(R.id.more_page_row1);
        tableRow2 = root.findViewById(R.id.more_page_row2);
        anal_text = root.findViewById(R.id.anal_text);
        username_text = root.findViewById(R.id.username);
        username_text.setText(Account.getInstance().getAccountNumber());
        setListener();

        final ArrayList<ArcProgressStackView.Model> models = new ArrayList<>();
        initData();
        models.add(new ArcProgressStackView.Model("BMI", (int)PhysicalUtil.getBmi(), Color.parseColor("#DCDCDC"), Color.parseColor("#26A69A")));
        models.add(new ArcProgressStackView.Model("PBF", (int)PhysicalUtil.getDpbf(), Color.parseColor("#C0C0C0"), Color.parseColor("#0088FF")));
        models.add(new ArcProgressStackView.Model("BMR", (int)PhysicalUtil.getBmr(), Color.parseColor("#D3D3D3"), Color.parseColor("#26A69A")));
        models.add(new ArcProgressStackView.Model("HR", HealthData.body_HR, Color.parseColor("#A9A9A9"), Color.parseColor("#0088FF")));
        final ArcProgressStackView arcProgressStackView = (ArcProgressStackView) root.findViewById(R.id.apsv);
        arcProgressStackView.setModels(models);
        anal_text.setText("BMI:"+ Math.round(PhysicalUtil.getBmi())+"\n\nPBF："+(int)PhysicalUtil.getDpbf()+"\n\n BMR："+(int)PhysicalUtil.getBmr() +"\n\n HR："+HealthData.body_HR);
        return root;
    }

    private void setListener(){
        tableRow1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), HealthRecord.class);
                startActivity(intent);
            }
        });
        tableRow2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AthleticData.class);
                startActivity(intent);
            }
        });
    }
    private void initData(){
        HealthData.age = PersonInfo.getInstance().getAge();
        HealthData.body_height = PersonInfo.getInstance().getHeight();
        HealthData.body_weight = PersonInfo.getInstance().getWeight();
        HealthData.body_HR = PersonInfo.getInstance().getHeartBeat();
        Log.i("zxc HealthData",HealthData.age+" "+HealthData.body_height+ " "+HealthData.body_weight);
    }
}