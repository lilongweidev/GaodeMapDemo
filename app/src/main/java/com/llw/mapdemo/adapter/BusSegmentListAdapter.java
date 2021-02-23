package com.llw.mapdemo.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.amap.api.services.busline.BusStationItem;
import com.amap.api.services.route.BusStep;
import com.amap.api.services.route.RailwayStationItem;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.llw.mapdemo.R;
import com.llw.mapdemo.util.SchemeBusStep;

import java.util.ArrayList;
import java.util.List;

/**
 * 公交段列表适配器
 *
 * @author llw
 * @date 2021/2/23 14:37
 */
public class BusSegmentListAdapter extends BaseQuickAdapter<SchemeBusStep, BaseViewHolder> {
    private List<SchemeBusStep> mBusStepList;

    public BusSegmentListAdapter(int layoutResId, @Nullable List<SchemeBusStep> data) {
        super(layoutResId, data);
        mBusStepList = data;
    }

    @Override
    protected void convert(BaseViewHolder helper, final SchemeBusStep item) {
        RelativeLayout busItem = helper.getView(R.id.bus_item);
        TextView busLineName = helper.getView(R.id.bus_line_name);
        ImageView busDirIcon = helper.getView(R.id.bus_dir_icon);
        TextView busStationNum = helper.getView(R.id.bus_station_num);
        final ImageView busExpandImage = helper.getView(R.id.bus_expand_image);
        ImageView busDirUp = helper.getView(R.id.bus_dir_icon_up);
        ImageView busDirDown = helper.getView(R.id.bus_dir_icon_down);
        ImageView splitLine = helper.getView(R.id.bus_seg_split_line);
        final LinearLayout expandContent = helper.getView(R.id.expand_content);


        if (helper.getAdapterPosition() == 0) {
            busDirIcon.setImageResource(R.drawable.dir_start);
            busLineName.setText("出发");
            busDirUp.setVisibility(View.INVISIBLE);
            busDirDown.setVisibility(View.VISIBLE);
            splitLine.setVisibility(View.GONE);
            busStationNum.setVisibility(View.GONE);
            busExpandImage.setVisibility(View.GONE);
        } else if (helper.getAdapterPosition() == mBusStepList.size() - 1) {
            busDirIcon.setImageResource(R.drawable.dir_end);
            busLineName.setText("到达终点");
            busDirUp.setVisibility(View.VISIBLE);
            busDirDown.setVisibility(View.INVISIBLE);
            busStationNum.setVisibility(View.INVISIBLE);
            busExpandImage.setVisibility(View.INVISIBLE);
        } else {
            if (item.isWalk() && item.getWalk() != null && item.getWalk().getDistance() > 0) {
                busDirIcon.setImageResource(R.drawable.dir13);
                busDirUp.setVisibility(View.VISIBLE);
                busDirDown.setVisibility(View.VISIBLE);
                busLineName.setText("步行"
                        + (int) item.getWalk().getDistance() + "米");
                busStationNum.setVisibility(View.GONE);
                busExpandImage.setVisibility(View.GONE);
            } else if (item.isBus() && item.getBusLines().size() > 0) {
                busDirIcon.setImageResource(R.drawable.dir14);
                busDirUp.setVisibility(View.VISIBLE);
                busDirDown.setVisibility(View.VISIBLE);
                busLineName.setText(item.getBusLines().get(0).getBusLineName());
                busStationNum.setVisibility(View.VISIBLE);
                busStationNum
                        .setText((item.getBusLines().get(0).getPassStationNum() + 1) + "站");
                busExpandImage.setVisibility(View.VISIBLE);
            } else if (item.isRailway() && item.getRailway() != null) {
                busDirIcon.setImageResource(R.drawable.dir16);
                busDirUp.setVisibility(View.VISIBLE);
                busDirDown.setVisibility(View.VISIBLE);
                busLineName.setText(item.getRailway().getName());
                busStationNum.setVisibility(View.VISIBLE);
                busStationNum
                        .setText((item.getRailway().getViastops().size() + 1) + "站");
                busExpandImage.setVisibility(View.VISIBLE);
            } else if (item.isTaxi() && item.getTaxi() != null) {
                busDirIcon.setImageResource(R.drawable.dir14);
                busDirUp.setVisibility(View.VISIBLE);
                busDirDown.setVisibility(View.VISIBLE);
                busLineName.setText("打车到终点");
                busStationNum.setVisibility(View.GONE);
                busExpandImage.setVisibility(View.GONE);
            }

        }

        busItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.isBus()) {//公交
                    if (item.isArrowExpend() == false) {
                        item.setArrowExpend(true);
                        busExpandImage.setImageResource(R.drawable.up);
                        addBusStation(item.getBusLine().getDepartureBusStation(), expandContent);
                        for (BusStationItem station : item.getBusLine()
                                .getPassStations()) {
                            addBusStation(station, expandContent);
                        }
                        addBusStation(item.getBusLine().getArrivalBusStation(), expandContent);

                    } else {
                        item.setArrowExpend(false);
                        busExpandImage.setImageResource(R.drawable.down);
                        expandContent.removeAllViews();
                    }
                } else if (item.isRailway()) {//火车
                    if (item.isArrowExpend() == false) {
                        item.setArrowExpend(true);
                        busExpandImage.setImageResource(R.drawable.up);
                        addRailwayStation(item.getRailway().getDeparturestop(), expandContent);
                        for (RailwayStationItem station : item.getRailway().getViastops()) {
                            addRailwayStation(station, expandContent);
                        }
                        addRailwayStation(item.getRailway().getArrivalstop(), expandContent);

                    } else {
                        item.setArrowExpend(false);
                        busExpandImage.setImageResource(R.drawable.down);
                        expandContent.removeAllViews();
                    }
                }
            }
        });
    }

    /**
     * 添加公交车站
     * @param station
     * @param expandContent
     */
    private void addBusStation(BusStationItem station, LinearLayout expandContent) {
        LinearLayout ll = (LinearLayout) View.inflate(mContext,
                R.layout.item_segment_ex, null);
        TextView tv = ll.findViewById(R.id.bus_line_station_name);
        tv.setText(station.getBusStationName());
        expandContent.addView(ll);
    }

    /**
     * 添加火车站
     * @param station
     * @param expandContent
     */
    private void addRailwayStation(RailwayStationItem station, LinearLayout expandContent) {
        LinearLayout ll = (LinearLayout) View.inflate(mContext,
                R.layout.item_segment_ex, null);
        TextView tv = ll
                .findViewById(R.id.bus_line_station_name);
        tv.setText(station.getName() + " " + getRailwayTime(station.getTime()));
        expandContent.addView(ll);
    }

    /**
     * 获取铁路时间
     * @param time
     * @return
     */
    public static String getRailwayTime(String time) {
        return time.substring(0, 2) + ":" + time.substring(2, time.length());
    }
}
