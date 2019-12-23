package com.example.myapplication.ui.setting;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.myapplication.R;
import com.example.myapplication.constant.HealthData;
import com.example.myapplication.data.AthleticData;
import com.example.myapplication.data.HealthRecord;

import java.util.ArrayList;
import java.util.List;

import devlight.io.library.ArcProgressStackView;

public class SettingFragment extends Fragment {

    private SettingViewModel settingViewModel;
    private ListView listview;
    private List<String> dataList = null;
    private TableRow tableRow1;
    private TableRow tableRow2;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        settingViewModel =
                ViewModelProviders.of(this).get(SettingViewModel.class);
        View root = inflater.inflate(R.layout.fragment_setting, container, false);

        tableRow1 = root.findViewById(R.id.more_page_row1);
        tableRow2 = root.findViewById(R.id.more_page_row2);
        setListener();

        final ArrayList<ArcProgressStackView.Model> models = new ArrayList<>();
        float bmi = HealthData.body_weight/((HealthData.body_height/100) * (HealthData.body_height/100));
        Double Dpbf = (1.2*bmi + 0.23*HealthData.age - 5.4 - 10.8*HealthData.Male);
        //float pbf = Dpbf.floatValue();
        //BMR = 655 + (9.6 x 体重kg) + (1.8 x 身高cm) - (4.7 x 年龄)
        //BMR = 66 + (13.7 x 体重kg) + (5 x 身高cm) - (6.8 x 年龄)
        Double Dbmr = (655 + (9.6*HealthData.body_weight) + 1.8*HealthData.body_height/100 - 4.7*HealthData.age) * HealthData.Male +
                (66 + (13.7*HealthData.body_weight) + 5*HealthData.body_height/100 - 6.8*HealthData.age) * HealthData.Female;
        float bmr = Dbmr.floatValue()/2000;
        models.add(new ArcProgressStackView.Model("BMI", Math.round(bmi), Color.parseColor("#DCDCDC"), Color.parseColor("#26A69A")));
        models.add(new ArcProgressStackView.Model("PBF", Dpbf.intValue(), Color.parseColor("#C0C0C0"), Color.parseColor("#0088FF")));
        models.add(new ArcProgressStackView.Model("BMR", Dbmr.intValue(), Color.parseColor("#D3D3D3"), Color.parseColor("#26A69A")));
        models.add(new ArcProgressStackView.Model("HR", HealthData.body_HR, Color.parseColor("#A9A9A9"), Color.parseColor("#0088FF")));

        final ArcProgressStackView arcProgressStackView = (ArcProgressStackView) root.findViewById(R.id.apsv);
        arcProgressStackView.setModels(models);
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
}