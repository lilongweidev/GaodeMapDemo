package com.llw.mapdemo.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.amap.api.services.route.WalkStep;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.llw.mapdemo.R;
import com.llw.mapdemo.util.MapUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 步行段列表适配器
 *
 * @author llw
 * @date 2021/2/23 9:31
 */
public class WalkSegmentListAdapter extends BaseQuickAdapter<WalkStep, BaseViewHolder> {

    private List<WalkStep> mItemList;

    public WalkSegmentListAdapter(int layoutResId, @Nullable List<WalkStep> data) {
        super(layoutResId, data);
        mItemList = data;
    }

    @Override
    protected void convert(BaseViewHolder helper, WalkStep item) {
        TextView lineName = helper.getView(R.id.bus_line_name);
        ImageView dirIcon = helper.getView(R.id.bus_dir_icon);
        ImageView dirUp = helper.getView(R.id.bus_dir_icon_up);
        ImageView dirDown = helper.getView(R.id.bus_dir_icon_down);
        ImageView splitLine = helper.getView(R.id.bus_seg_split_line);

        if (helper.getAdapterPosition() == 0) {
            dirIcon.setImageResource(R.drawable.dir_start);
            lineName.setText("出发");
            dirUp.setVisibility(View.INVISIBLE);
            dirDown.setVisibility(View.VISIBLE);
            splitLine.setVisibility(View.INVISIBLE);
        } else if (helper.getAdapterPosition() == mItemList.size() - 1) {
            dirIcon.setImageResource(R.drawable.dir_end);
            lineName.setText("到达终点");
            dirUp.setVisibility(View.VISIBLE);
            dirDown.setVisibility(View.INVISIBLE);
        } else {
            splitLine.setVisibility(View.VISIBLE);
            dirUp.setVisibility(View.VISIBLE);
            dirDown.setVisibility(View.VISIBLE);
            String actionName = item.getAction();
            int resID = MapUtil.getWalkActionID(actionName);
            dirIcon.setImageResource(resID);
            lineName.setText(item.getInstruction());
        }
    }
}
