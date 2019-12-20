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
        models.add(new ArcProgressStackView.Model("Circle", 25, Color.parseColor("#DCDCDC"), Color.parseColor("#26A69A")));
        models.add(new ArcProgressStackView.Model("Progress", 50, Color.parseColor("#C0C0C0"), Color.parseColor("#0088FF")));
        models.add(new ArcProgressStackView.Model("Stack", 75, Color.parseColor("#D3D3D3"), Color.parseColor("#26A69A")));
        models.add(new ArcProgressStackView.Model("View", 100, Color.parseColor("#A9A9A9"), Color.parseColor("#0088FF")));

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