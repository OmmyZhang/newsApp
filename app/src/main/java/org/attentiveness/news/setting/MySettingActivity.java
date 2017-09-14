package org.attentiveness.news.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.attentiveness.news.R;
import org.attentiveness.news.base.BaseActivity;
import org.attentiveness.news.globalSetting.GlobalSetting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MySettingActivity extends BaseActivity {

    List<String> list;
    final String[] classTags = {"科技", "教育", "军事", "国内", "社会", "文化", "汽车", "国际", "体育", "财经", "健康", "娱乐"};

    @BindView(R.id.linearLayout1)
    LinearLayout l1;
    @BindView(R.id.linearLayout2)
    LinearLayout l2;
    @BindView(R.id.linearLayout3)
    LinearLayout l3;


    private List<LinearLayout> linearLayout;
    Toast toast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_setting);
        ButterKnife.bind(this);

        linearLayout = new ArrayList<LinearLayout>();
        linearLayout.add(l1);
        linearLayout.add(l2);
        linearLayout.add(l3);

        list = Arrays.asList(classTags);

        initMarksView();
        setTitle("设置");
        toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);

//        setup(R.drawable.ic_menu);
    }

    private void initMarksView() {
        for (int i = 0; i < list.size(); i++) {
            View view = View.inflate(MySettingActivity.this, R.layout.class_tag, null);
            TextView tv = (TextView) view.findViewById(R.id.textView1);
            tv.setText(list.get(i));

            tv.setTag(i);
            boolean fo = GlobalSetting.getINSTANCE().checkClassTag((String) tv.getText());

            view.setTag(fo);
            tv.setEnabled(fo);
            // 设置view的点击事件，与onClick中的View一致
            //否则需要在onClick中，去findViewById，找出设置点击事件的控件进行操作
            //若不如此，则无法触发点击事件
            view.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    TextView tv = (TextView) v.findViewById(R.id.textView1);
                    if ((Boolean) v.getTag()) {
                        v.setTag(false);
                        tv.setEnabled(false);
                        GlobalSetting.getINSTANCE().delClassTag((String) tv.getText());
                        //Toast.makeText(MySettingActivity.this, "已取关" + tv.getText(), Toast.LENGTH_SHORT).show();
                        toast.setText("已取关" + tv.getText());
                        toast.show();
                    } else {
                        v.setTag(true);
                        tv.setEnabled(true);
                        GlobalSetting.getINSTANCE().addClassTag((String) tv.getText());
                        //Toast.makeText(MySettingActivity.this, "已关注" + tv.getText(), Toast.LENGTH_SHORT).show();
                        toast.setText("已关注" + tv.getText());
                        toast.show();
                    }
                }
            });
            linearLayout.get(i / 5).addView(view);
        }
    }
}
