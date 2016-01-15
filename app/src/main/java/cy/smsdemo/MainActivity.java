package cy.smsdemo;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText et_phone,et_code,et_password;
    private Button btn_getCode,btn_login,btn_listview,btn_drag;
    private int containerHeight,containerWidth;
    private RelativeLayout rl;
    float lastX,lastY;
    private ArrayList<String> arr;
    EventHandler handler;
    private static final int START_GET_CODE = 4;
    private static final int ING_GET_CODE = 5;
    private static final int END_GET_CODE = 6;
    private int conunt = 10;

    private Handler hl = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what){
                case 0:
                    Toast.makeText(MainActivity.this,"验证码正确，开始对本地服务器进行账号注册",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this,TwoActivity.class);
                    startActivity(intent);
                    break;
                case 1:
                    Toast.makeText(MainActivity.this,"已成功发送验证码",Toast.LENGTH_SHORT).show();
                    break;
                case START_GET_CODE:
                    btn_getCode.setEnabled(false);
                    btn_getCode.setText("10秒后重发");
                    conunt--;
                    hl.sendEmptyMessageDelayed(ING_GET_CODE,1000);
                    break;

                case ING_GET_CODE:
                    hl.removeMessages(ING_GET_CODE);
                    if(conunt == 0){
                        hl.sendEmptyMessage(END_GET_CODE);
                        break;
                    }
                    btn_getCode.setText(conunt-- +"秒后重发");
                    hl.sendEmptyMessageDelayed(ING_GET_CODE,1000);
                    break;

                case END_GET_CODE:
                    btn_getCode.setText("获取验证码");
                    btn_getCode.setEnabled(true);
                    conunt = 10;
                    break;

            }
            return false;
        }
    });


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        // 这里来获取容器的宽和高
        if (hasFocus) {
            containerHeight = rl.getHeight();
            containerWidth = rl.getWidth();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        init();
    }

    private void initView() {
         rl = (RelativeLayout) findViewById(R.id.rl);
        btn_getCode = (Button) findViewById(R.id.btn_get_code);
        btn_login = (Button) findViewById(R.id.btn_login);
        et_code = (EditText) findViewById(R.id.et_code);
        et_password = (EditText) findViewById(R.id.et_password);
        et_phone = (EditText) findViewById(R.id.et_phone);
        btn_listview = (Button) findViewById(R.id.listview);
        btn_drag = (Button) findViewById(R.id.btn_drag);
        btn_drag.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = motionEvent.getRawX();
                        lastY = motionEvent.getRawY();
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        //  不要直接用getX和getY,这两个获取的数据已经是经过处理的,容易出现图片抖动的情况
                        float distanceX = lastX - motionEvent.getRawX();
                        float distanceY = lastY - motionEvent.getRawY();

                        float nextY = btn_drag.getY() - distanceY;
                        float nextX = btn_drag.getX() - distanceX;

                        // 不能移出屏幕
                        if (nextY < 0) {
                            nextY = 0;
                        } else if (nextY > containerHeight - btn_drag.getHeight()) {
                            nextY = containerHeight - btn_drag.getHeight();
                        }
                        if (nextX < 0)
                            nextX = 0;
                        else if (nextX > containerWidth - btn_drag.getWidth())
                            nextX = containerWidth - btn_drag.getWidth();

                        // 属性动画移动
                        ObjectAnimator y = ObjectAnimator.ofFloat(btn_drag, "y", btn_drag.getY(), nextY);
                        ObjectAnimator x = ObjectAnimator.ofFloat(btn_drag, "x", btn_drag.getX(), nextX);

                        AnimatorSet animatorSet = new AnimatorSet();
                        animatorSet.playTogether(x, y);
                        animatorSet.setDuration(0);
                        animatorSet.start();

                        lastX = motionEvent.getRawX();
                        lastY = motionEvent.getRawY();

                        case MotionEvent.ACTION_UP:
                            lastX = motionEvent.getRawX();
                            lastY = motionEvent.getRawY();
                            break;
                }
                return false;
            }
        });
        btn_listview.setOnClickListener(this);
        btn_getCode.setOnClickListener(this);
        btn_login.setOnClickListener(this);
    }

    private void init() {
        SMSSDK.initSDK(this,ConstantValue.app_id,ConstantValue.app_secret);
        handler = new EventHandler(){
            @Override
            public void afterEvent(int event, int result, Object data) {
                super.afterEvent(event, result, data);
                if (result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        //提交验证码成功
                        Message msg = hl.obtainMessage();
                        msg.what = 0;
                        hl.sendMessage(msg);
                    }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                        //获取验证码成功
                        Message msg = hl.obtainMessage();
                        msg.what = 1;
                        hl.sendMessage(msg);
                    }else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){
                        //返回支持发送验证码的国家列表
                    }
                }else{
                    ((Throwable)data).printStackTrace();
                }
            }
        };
        SMSSDK.registerEventHandler(handler);

    }

    @Override
    public void onClick(View view) {
        String number = et_phone.getText().toString();
        switch (view.getId()){
            case R.id.btn_get_code:
                if(number.length()!=11){
                    Toast.makeText(MainActivity.this,"手机号码格式不符",Toast.LENGTH_SHORT).show();
                    return;
                }
                SMSSDK.getVerificationCode("86", number);
                hl.sendEmptyMessage(START_GET_CODE);


                break;
            case R.id.btn_login:
                String code = et_code.getText().toString();
                if(number.length()!=11 || code == null){
                    Toast.makeText(MainActivity.this,"输入不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }

                SMSSDK.submitVerificationCode("86", number, code);
                break;
            case R.id.listview:

                Intent intent = new Intent(MainActivity.this,ExpandleListActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(handler);
    }
}
