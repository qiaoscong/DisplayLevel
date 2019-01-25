package text.qiao.com.displaylevel;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.myGradeView)
    MyGradeView myGradeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        myGradeView.setDengjishu("LV2");
        myGradeView.setNicheng("我的名字");
        myGradeView.setJindu(2);
        myGradeView.start();

    }
}
