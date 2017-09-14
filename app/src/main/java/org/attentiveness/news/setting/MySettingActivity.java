package org.attentiveness.news.setting;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.attentiveness.news.R;
import org.attentiveness.news.base.BaseActivity;
import org.attentiveness.news.globalSetting.GlobalSetting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.SimpleTimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MySettingActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    List<String> tagList;
    final String[] classTags = {"科技", "教育", "军事", "国内", "社会", "文化", "汽车", "国际", "体育", "财经", "健康", "娱乐"};

    @BindView(R.id.linearLayout1)
    LinearLayout l1;
    @BindView(R.id.linearLayout2)
    LinearLayout l2;
    @BindView(R.id.linearLayout3)
    LinearLayout l3;

    @BindView(R.id.sw_other_class)
    Switch otherClass;
    @BindView(R.id.sw_auto_refresh)
    Switch autoRefresh;
    @BindView(R.id.sw_no_picture)
    Switch noPicture;
    @BindView(R.id.sw_backtage_voice)
    Switch backtageVoice;


    private List<LinearLayout> linearLayout;
    Toast toast;

    @BindView(R.id.tag_layout)
    Flowlayout mTagLayout;

    private ArrayList<TagItem> mAddTags = new ArrayList<TagItem>();

    EditText inputLabel;

    @BindView(R.id.btn_sure)
    Button btnSure;

    // 存放标签数据的数组
//    String[] mTextStr = { };

    ArrayList<String>  list = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_setting);
        ButterKnife.bind(this);

        linearLayout = new ArrayList<LinearLayout>();
        linearLayout.add(l1);
        linearLayout.add(l2);
        linearLayout.add(l3);

        tagList = Arrays.asList(classTags);

        initMarksView();
        setTitle("设置");
        toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);

        otherClass.setOnCheckedChangeListener(this);
        autoRefresh.setOnCheckedChangeListener(this);
        noPicture.setOnCheckedChangeListener(this);
        backtageVoice.setOnCheckedChangeListener(this);

        otherClass.setChecked(GlobalSetting.getINSTANCE().isAllowOtherClass());
        autoRefresh.setChecked(GlobalSetting.getINSTANCE().isAutoRefreshMayLike());
        noPicture.setChecked(GlobalSetting.getINSTANCE().isNoPicture());
        backtageVoice.setChecked(GlobalSetting.getINSTANCE().isAllowBackstageVoice());


//        setup(R.drawable.ic_menu);

        inputLabel = (EditText) LayoutInflater.from(this).inflate(R.layout.my_edit_text, null);

        initList();

        initLayout(list);

        initBtnListener();
    }

    private void initMarksView() {
        for (int i = 0; i < tagList.size(); i++) {
            View view = View.inflate(MySettingActivity.this, R.layout.class_tag, null);
            TextView tv = (TextView) view.findViewById(R.id.textView1);
            tv.setText(tagList.get(i));

            tv.setTag(i);
            boolean fo = GlobalSetting.getINSTANCE().checkClassTag((String) tv.getText());

            view.setTag(fo);
            tv.setEnabled(fo);


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

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.sw_other_class:
                GlobalSetting.getINSTANCE().setAllowOtherClass(b);
                break;
            case R.id.sw_auto_refresh:
                GlobalSetting.getINSTANCE().setAutoRefreshMayLike(b);
                break;
            case R.id.sw_no_picture:
                GlobalSetting.getINSTANCE().setNoPicture(b);
                break;
            case R.id.sw_backtage_voice:
                GlobalSetting.getINSTANCE().setAllowBackstageVoice(b);
                break;
        }
    }

    private void initList() {
        Set<String> nSW = GlobalSetting.getINSTANCE().getNotShow();
        for(String s:nSW)
            list.add(s);
/*
        for(int i=0;i<mTextStr.length;i++){
            list.add(mTextStr[i]);
        }
        */
    }

    private void initBtnListener() {
        /**
         * 初始化  单击事件：
         */
        btnSure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String label = inputLabel.getText().toString().trim();

                String[] newStr = new String[mTagLayout.getChildCount()];

                /**
                 * 获取  子view的数量   并添加进去
                 */
                if(label!=null&&!label.equals("")&&!GlobalSetting.getINSTANCE().checkNotShow(label)){
                    GlobalSetting.getINSTANCE().addNotShow(label);
                    for(int m = 0;m < mTagLayout.getChildCount()-1;m++){
                        newStr[m] =((TextView)mTagLayout.getChildAt(m).
                                findViewById(R.id.text)).getText().toString();//根据  当前   位置查找到 当前    textView中标签  内容
                    }
                    list.add(label);
                    initLayout(list);
                    inputLabel.setText("");
                }
            }
        });

        inputLabel.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                btnSure.callOnClick();
                return false;
            }
        });
    }


    private void initLayout(final ArrayList<String> arr) {

        mTagLayout.removeAllViewsInLayout();
        /**
         * 创建 textView数组
         */
        final TextView[] textViews = new TextView[arr.size()];
        final TextView[] icons = new TextView[arr.size()];

        for (int i = 0; i < arr.size(); i++) {

            final int pos = i;

            final View view = (View) LayoutInflater.from(MySettingActivity.this).inflate(R.layout.text_view, mTagLayout, false);

            final TextView text = (TextView) view.findViewById(R.id.text);  //查找  到当前     textView
            final TextView icon = (TextView) view.findViewById(R.id.delete_icon);  //查找  到当前  删除小图标

            // 将     已有标签设置成      可选标签
            text.setText(list.get(i));
            /**
             * 将当前  textView  赋值给    textView数组
             */
            textViews[i] = text;
            icons[i] = icon;

            //设置    单击事件：
            icon.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //遍历  图标  删除 当前  被点击项
                    for(int j = 0; j < icons.length;j++){
                        if(icon.equals(icons[j])){  //获取   当前  点击删除图标的位置：
                            mTagLayout.removeViewAt(j);
                            GlobalSetting.getINSTANCE().delNotShow( list.get(j) );
                            list.remove(j);
                            initLayout(list);
                        }
                    }
                }
            });

            text.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    text.setActivated(!text.isActivated()); // true是激活的

                    if (text.isActivated()) {
                        boolean bResult = doAddText(list.get(pos), false, pos);
                        text.setActivated(bResult);
                        //遍历   数据    将图标设置为可见：
                        for(int j = 0;j< textViews.length;j++){
                            if(text.equals(textViews[j])){//非当前  textView
                                icons[j].setVisibility(View.VISIBLE);
                            }
                        }
                    }else{
                        for(int j = 0;j< textViews.length;j++){
                            icons[j].setVisibility(View.GONE);
                        }
                    }

                    /**
                     * 遍历  textView  满足   已经被选中     并且不是   当前对象的textView   则置为  不选
                     */
                    for(int j = 0;j< textViews.length;j++){
                        if(!text.equals(textViews[j])){//非当前  textView
                            textViews[j].setActivated(false); // true是激活的
                            icons[j].setVisibility(View.GONE);
                        }
                    }
                }
            });

            mTagLayout.addView(view);
        }

        mTagLayout.addView(inputLabel);
    }

    // 标签索引文本
    protected int idxTextTag(String text) {
        int mTagCnt = mAddTags.size(); // 添加标签的条数
        for (int i = 0; i < mTagCnt; i++) {
            TagItem item = mAddTags.get(i);
            if (text.equals(item.tagText)) {
                return i;
            }
        }
        return -1;
    }

    // 标签添加文本状态
    private boolean doAddText(final String str, boolean bCustom, int idx) {
        int tempIdx = idxTextTag(str);
        if (tempIdx >= 0) {
            TagItem item = mAddTags.get(tempIdx);
            item.tagCustomEdit = false;
            item.idx = tempIdx;
            return true;
        }

        TagItem item = new TagItem();

        item.tagText = str;
        item.tagCustomEdit = bCustom;
        item.idx = idx;
        mAddTags.add(item);

        return true;
    }
}
