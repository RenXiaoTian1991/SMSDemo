package cy.smsdemo;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by cuiyue on 15/10/14.
 */
public class MyPageAdapter extends PagerAdapter {

    private ArrayList<View> arr;

    public MyPageAdapter(ArrayList<View> arr) {
        this.arr = arr;
    }

    @Override
    public int getCount() {
        return arr.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(arr.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(arr.get(position));
        return arr.get(position);
    }
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }
}
