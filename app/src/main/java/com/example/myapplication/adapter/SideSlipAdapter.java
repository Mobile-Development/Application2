package com.example.myapplication.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.utils.TypeUtil;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;

import java.util.List;
import java.util.Map;
public class SideSlipAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<? extends Map<String, ?>> list;
    private Context context;

    public SideSlipAdapter (Context context, List<? extends Map<String, ?>> data) {
        //super(context,data,resource, from, to);
        this.inflater = LayoutInflater.from(context);
        this.list = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        View closeView = null;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.listview_item_delete, parent, false);
            holder = new ViewHolder();
            holder.tv_time = convertView.findViewById(R.id.item_time);
            holder.tv_cal = convertView.findViewById(R.id.item_cal);
            holder.img = convertView.findViewById(R.id.item_image);
            holder.btn_top = convertView.findViewById(R.id.btnTop);
            holder.btn_delete = convertView.findViewById(R.id.btnDelete);
            convertView.setTag(holder);
        }

        if (closeView == null){
            closeView = convertView;
        }
        final View finalCloseView = closeView;// listView的itemView

        holder = (ViewHolder) convertView.getTag();
        holder.tv_time.setText(list.get(position).get("time").toString());
        holder.tv_cal.setText(list.get(position).get("cal").toString());
        holder.img.setImageResource(TypeUtil.TypeToResourse(list.get(position).get("img").toString()));



        // 置顶按钮的单击事件
        holder.btn_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SwipeMenuLayout)(finalCloseView)).quickClose();// 关闭侧滑菜单：需要将itemView强转，然后调用quickClose()方法
                if (modItemListener != null){
                    modItemListener.modify(position);// 调用接口的方法，回调删除该项数据
                }
            }
        });

        // 删除按钮的单击事件
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SwipeMenuLayout)(finalCloseView)).quickClose();// 关闭侧滑菜单
                if (delItemListener != null){
                    delItemListener.delete(position);// 调用接口的方法，回调删除该项数据
                }
            }
        });

        return convertView;
    }

    /**
     * 缓存控件用
     */
    static class ViewHolder{
        TextView tv_time;
        TextView tv_cal;
        ImageView img;
        Button btn_top;// 置顶
        Button btn_delete;// 删除
    }

    // 定义接口，包含了删除数据的方法
    public interface DeleteItem{
        void delete(int pos);
    }

    private DeleteItem delItemListener;
    // 设置监听器的方法
    public void setDelItemListener(DeleteItem delItemListener){
        this.delItemListener = delItemListener;
    }

    //定义接口，用于修改数据
    public interface ModifyItem{
        void modify(int pos);
    }
    private ModifyItem modItemListener;
    // 设置监听器的方法
    public void setModItemListener(ModifyItem modItemListener){
        this.modItemListener = modItemListener;
    }
}
