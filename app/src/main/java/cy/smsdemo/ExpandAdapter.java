package cy.smsdemo;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by cuiyue on 15/10/13.
 */
public class ExpandAdapter extends BaseExpandableListAdapter{
    private ArrayList<String> group;
    private ArrayList<List<String>> child;
    private Context context;
    private LayoutInflater inflater;
    private ExpandableListView listview;

    private Handler handler;
    private TextView time;
    private ExpandleListActivity activity;
    private int clickPosition = 0;
    private int expandPosition = 0;
    private static final int PLAY_ING = 1;
    private static final int PLAY_END = 2;
    private static final int PLAY_START = 3;
    private static final int PLAY_STOP = 4;
    private int daoTime = 10;
    private ViewPager viewPager;


    private Handler myhandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case PLAY_END:
                    gotoNextGroup();
                    break;
                case PLAY_ING:
                    if(daoTime!=0){
                        --daoTime;
                        time.setText("倒计时"+daoTime+"秒");
                        myhandler.sendEmptyMessageDelayed(PLAY_ING,1000);
                    }else{
                        myhandler.sendEmptyMessage(PLAY_END);
                    }

                    break;
                case PLAY_START:

                    break;
                case PLAY_STOP:

                    break;
            }
        }
    };

    public void gotoNextGroup(){
        if(expandPosition== (group.size()-1)){
            listview.collapseGroup(expandPosition);
            time.setText("已全部完成");
            handler.sendEmptyMessage(activity.END_TIME);
                    return;
        }
        daoTime = 10;
        //listview.expandGroup(expandPosition+1);
        //此处是listview弹性展开的一个重要属性，第一个没有弹性给的属性
        listview.expandGroup(expandPosition+1,true);
        time.setText("倒计时"+daoTime+"秒");
        myhandler.sendEmptyMessageDelayed(PLAY_ING, 1000);
    }

    public ExpandAdapter(TextView time,ExpandleListActivity activity,ArrayList<String> group,ArrayList<List<String>> child,Context context,ExpandableListView listview,Handler handler) {
        this.activity = activity;
        this.time = time;
        this.group = group;
        this.child = child;
        this.context= context;
        this.listview = listview;
        this.handler = handler;
        inflater = LayoutInflater.from(context);
        listview.setOnGroupExpandListener(new MyOnGroupExpandListener());
        listview.setOnGroupClickListener(new MyOnGroupClickListener());
        listview.setOnGroupCollapseListener(new MyOnGroupCollapseListener());
        activity.setPlayListener(new MyPlayListener());
    }

    @Override
    public int getGroupCount() {
        return group.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return 1;
    }

    @Override
    public Object getGroup(int i) {
        return group.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return null;
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        ViewHolder myViewHolder = null;
        if(view == null){
            myViewHolder = new ViewHolder();
            view  = inflater.inflate(R.layout.listview_group,null);
            TextView tv = (TextView) view.findViewById(R.id.txt_group);
            myViewHolder.tv = tv;
            view.setTag(myViewHolder);
        }else{
            myViewHolder = (ViewHolder) view.getTag();
        }

        TextView tv = myViewHolder.tv;
        if (listview.isGroupExpanded(i)){
            tv.setTextColor(context.getResources().getColor(R.color.title));
            tv.setTextSize(28);
        }else{
            tv.setTextColor(context.getResources().getColor(R.color.black));
            tv.setTextSize(22);
        }
        tv.setText(group.get(i));
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        final ViewHolder2 myViewHolder;
        if(view == null){
            myViewHolder = new ViewHolder2();
            view  = inflater.inflate(R.layout.llistview_child,null);
            viewPager = (ViewPager) view.findViewById(R.id.viewpager);
            ImageView iv_left = (ImageView) view.findViewById(R.id.iv_personal_left_arrow);
            ImageView iv_right = (ImageView) view.findViewById(R.id.iv_personal_right_arrow);
            ImageView one = (ImageView) view.findViewById(R.id.iv_personal_left_dot);
            ImageView two = (ImageView) view.findViewById(R.id.iv_personal_right_dot);
            View v1 = inflater.inflate(R.layout.view1,null);
            View v2 = inflater.inflate(R.layout.view2,null);
            ImageView imageView = (ImageView) v1.findViewById(R.id.view1_image);
            SurfaceView surfaceView = (SurfaceView) v1.findViewById(R.id.view1_surface);
            TextView textView = (TextView) v2.findViewById(R.id.view2_txt);
            myViewHolder.iv = imageView;
            myViewHolder.sur = surfaceView;
            myViewHolder.tv = textView;
            ArrayList<View> arrayList = new ArrayList<View>();
            arrayList.add(v1);
            arrayList.add(v2);
            MyPageAdapter adapter1 = new MyPageAdapter(arrayList);
            viewPager.setAdapter(adapter1);
            myViewHolder.viewPger = viewPager;
            myViewHolder.iv_left = iv_left;
            myViewHolder.iv_right = iv_right;
            myViewHolder.one = one;
            myViewHolder.two = two;

            view.setTag(myViewHolder);
        }else{
            myViewHolder = (ViewHolder2) view.getTag();
        }
            myViewHolder.viewPger.setCurrentItem(0);
            myViewHolder.iv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myViewHolder.viewPger.setCurrentItem(0);
            }
        });

        myViewHolder.iv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("abc","你点击了向右的箭头");
                myViewHolder.viewPger.setCurrentItem(1);
            }
        });
            myViewHolder.viewPger.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    if(position==0){
                        myViewHolder.one.setBackgroundResource(R.drawable.personal_pager_selector);
                        myViewHolder.two.setBackgroundResource(R.drawable.personal_pager_normal);
                    }else if(position ==1){
                        myViewHolder.two.setBackgroundResource(R.drawable.personal_pager_selector);
                        myViewHolder.one.setBackgroundResource(R.drawable.personal_pager_normal);
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

        return view;
    }

    public class ViewHolder{
        public TextView tv;
    }


    public class ViewHolder2{
        public ViewPager viewPger;
        private ImageView iv;
        private SurfaceView sur;
        private TextView tv;
        private ImageView iv_left;
        private ImageView iv_right;
        private ImageView one;
        private ImageView two;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    private class MyOnGroupExpandListener implements ExpandableListView.OnGroupExpandListener{

        @Override
        public void onGroupExpand(int i) {
            expandPosition = i;
            //关闭其他的group
             for(int a=0;a<group.size();a++){
                 if(a!= i){
                     listview.collapseGroup(a);
                 }
             }
            //这个没有动画效果的选中
            //listview.setSelectedGroup(i);
            Message msg = handler.obtainMessage();
            msg.what = ExpandleListActivity.ING_TIME;
            handler.sendMessage(msg);
        }
    }

    private class MyOnGroupClickListener implements ExpandableListView.OnGroupClickListener{

        @Override
        public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
            clickPosition = i;
            daoTime = 10;
            time.setText("倒计时"+daoTime+"秒");
            myhandler.removeMessages(PLAY_ING);
            handler.sendEmptyMessage(activity.CLICK_OTHER);
            return false;
        }
    }

    private class MyOnGroupCollapseListener implements ExpandableListView.OnGroupCollapseListener{

        @Override
        public void onGroupCollapse(int i) {

        }
    }

    public class MyPlayListener implements ExpandleListActivity.PlayListener{

        @Override
        public void startPlay() {
            if(expandPosition==(group.size()-1)){
                expandPosition = 0;
                daoTime = 10;
            }
                listview.expandGroup(expandPosition);
                time.setText("倒计时"+daoTime+"秒");
                myhandler.sendEmptyMessageDelayed(PLAY_ING, 1000);

        }

        @Override
        public void stopPlay() {
            myhandler.sendEmptyMessage(PLAY_STOP);
            myhandler.removeMessages(PLAY_ING);
        }

        @Override
        public void endPlay() {

        }
    }


}
