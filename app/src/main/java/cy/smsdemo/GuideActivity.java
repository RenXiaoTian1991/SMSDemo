package cy.smsdemo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import cy.smsdemo.utils.PxUtils;

/**
 * Created by cuiyue on 15/10/19.
 */
public class GuideActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private LinearLayout ll_point;
    private  ArrayList<View> arr;
    private ImageView point_red;
    private int length;
    private int[] imgsId = new int[]{R.drawable.guide_item1_bg,R.drawable.guide_item2_bg,R.drawable.guide_item3_bg,R.drawable.guide_item4_bg};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide);
        mViewPager = (ViewPager) findViewById(R.id.viewpager_guide);
        ll_point = (LinearLayout) findViewById(R.id.ll_point);
        point_red = (ImageView) findViewById(R.id.point_red);
        initData();
        MyAdapter adapter = new MyAdapter();
        adapter.setData(arr);
        mViewPager.setAdapter(adapter);

    }

    private void initData() {
        arr = new ArrayList<View>();
        for(int i=0;i<imgsId.length;i++){
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(imgsId[i]);
            arr.add(imageView);
            if(i==3){
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(GuideActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        }
        for(int i=0;i<imgsId.length;i++){
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(R.drawable.shape_gray);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(PxUtils.Dp2Px(this,10),PxUtils.Dp2Px(this,10));
            if(i>0){
                params.leftMargin = 10;
            }
            imageView.setLayoutParams(params);
            ll_point.addView(imageView);
        }

        ll_point.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                ll_point.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                length = ll_point.getChildAt(1).getLeft()-ll_point.getChildAt(0).getLeft();
            }
        });

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) point_red.getLayoutParams();
                params.leftMargin = (int) (v*length)+i*length;
                point_red.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    public class MyAdapter extends PagerAdapter{

        private ArrayList<View> arr;
        public void setData(ArrayList<View> data){
            arr = data;
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(arr.get(position));
            return arr.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(arr.get(position));
        }

        @Override
        public int getCount() {
            return arr.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
