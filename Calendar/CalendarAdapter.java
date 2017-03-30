package nz.ac.cornell.fitnessmealplans.Calendar;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;

import nz.ac.cornell.fitnessmealplans.Models.DayInfo;
import nz.ac.cornell.fitnessmealplans.R;

/**
 * Calendar user adaper
 * Created by HJS on 2016-05-08.
 */
public class CalendarAdapter extends BaseAdapter {

    private ArrayList<DayInfo> mDayList;
    private int mResource;
    private LayoutInflater mLiInflater;
    private Context mContext;

    public CalendarAdapter(Context context, int textResource, ArrayList<DayInfo> dayList){
        this.mContext = context;
        this.mDayList = dayList;
        this.mResource = textResource;
        this.mLiInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount(){return mDayList.size();}

    @Override
    public Object getItem(int position){return mDayList.get(position);}

    @Override
    public long getItemId(int position){return 0;}

    /**
     * implements about one day. day, upDown Arrows, planning flag
     * @param position
     * @param v
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View v, ViewGroup parent){
        DayInfo day = mDayList.get(position);
        DayViewHolder dayViewHolder;
        if(v == null) {
            v = mLiInflater.inflate(mResource, null);
            dayViewHolder = new DayViewHolder();
            dayViewHolder.llBackground = (LinearLayout)v.findViewById(R.id.day_cell_ll_background);
            dayViewHolder.tvDay = (TextView) v.findViewById(R.id.tvDate);
            dayViewHolder.imPlan = (ImageView) v.findViewById(R.id.imPlan);
            dayViewHolder.imUpDown = (ImageView) v.findViewById(R.id.imUpDown);
            v.setTag(dayViewHolder);
        }
        else {
            dayViewHolder = (DayViewHolder) v.getTag();
        }

        if(day != null) {
            dayViewHolder.tvDay.setText(day.getDate());
            //preview month or next month can not access
            if(day.isbMonth()) {
                if(position % 7 == 0)
                    dayViewHolder.tvDay.setTextColor(Color.RED);
                else if(position % 7 == 6)
                    dayViewHolder.tvDay.setTextColor(Color.BLUE);
                else
                    dayViewHolder.tvDay.setTextColor(Color.BLACK);
                dayViewHolder.llBackground.setClickable(false); //Cleckable
            }
            else {
                dayViewHolder.tvDay.setTextColor(Color.GRAY);
                dayViewHolder.llBackground.setClickable(true); //nonClickable
            }

            //shown Planning has or not of each day
            if(day.isHavingPlan()){
                dayViewHolder.imPlan.setVisibility(dayViewHolder.imPlan.VISIBLE);
                dayViewHolder.imUpDown.setVisibility(dayViewHolder.imUpDown.VISIBLE);
                if(day.isUpDown()){
                    dayViewHolder.imUpDown.setImageResource(R.drawable.over_cal);
                }
                else{
                    dayViewHolder.imUpDown.setImageResource(R.drawable.less_cal);
                }
            }
        }
        return v;
    }

    // Day view holer structure
    public class DayViewHolder {
        public LinearLayout llBackground;
        public TextView tvDay;
        public ImageView imPlan;
        public ImageView imUpDown;
    }
}
