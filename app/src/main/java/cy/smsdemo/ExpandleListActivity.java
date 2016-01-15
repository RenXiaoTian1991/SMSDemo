package cy.smsdemo;

import android.app.Activity;
import android.app.ExpandableListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cuiyue on 15/10/13.
 */
public class ExpandleListActivity extends Activity {
    private ExpandableListView mListView;
    private ArrayList<String> group;
    private ArrayList<List<String>> child;
    private TextView time;
    private Button btn_action;
    private PlayListener listener;
    private boolean isPlay = false;
    public static final int START_TIME = 0;
    public static final int ING_TIME = 1;
    public static final int END_TIME = 2;
    public static final int CLICK_OTHER = 3;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case START_TIME:

                    break;
                case CLICK_OTHER:
                    isPlay = false;
                    btn_action.setText("点击开始");
                    break;
                case ING_TIME:

                    break;
                case END_TIME:
                    isPlay = false;
                    btn_action.setText("点击再次");
                    break;
                default:

                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);
        mListView = (ExpandableListView) findViewById(R.id.listview);
        time = (TextView) findViewById(R.id.time);
        btn_action = (Button) findViewById(R.id.btn_action);
        group = new ArrayList<String>();
        child = new ArrayList<List<String>>();

        initData();
        ExpandAdapter adapter = new ExpandAdapter(time,ExpandleListActivity.this,group,child,this,mListView,handler);
        mListView.setAdapter(adapter);

        btn_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPlay == false){
                    btn_action.setText("点击暂停");
                    isPlay = !isPlay;
                    listener.startPlay();
                }else{
                    btn_action.setText("开始计时");
                    isPlay = !isPlay;
                    listener.stopPlay();
                }
            }
        });

    }

    private void initData() {
        group.add("第一组");
        group.add("第二组");
        group.add("第三组");
        group.add("第四组");
        group.add("第五组");
        group.add("第六组");
        group.add("第七组");
        group.add("第八组");
        group.add("第九组");
        for(int i=0;i<group.size();i++){
            ArrayList<String> arr = new ArrayList<String>();
            for(int j=0;j<6;j++){
                arr.add("第"+i+"组"+"第"+j+"个子数据");
            }
            child.add(arr);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void setPlayListener(PlayListener listener){
        this.listener = listener;
    }
    public interface PlayListener{
        public void startPlay();
        public void stopPlay();
        public void endPlay();
    }
}
