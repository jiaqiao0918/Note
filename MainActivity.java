package com.example.administrator.appshortcuts;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String intent_str = "没接收到intent";
    private TextView show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        show = (TextView) findViewById(R.id.show);
        intent_str = getIntent().getStringExtra("msg");
        show.setText(intent_str);
        if(Build.VERSION.SDK_INT>=24) {
            setupShortcuts();
        }
    }

    @TargetApi(Build.VERSION_CODES.N_MR1)//安卓7.1以上才可以使用，App Shortcuts，二级菜单，类似3DTouch
    private void setupShortcuts() {
        ShortcutManager mShortcutManager = getSystemService(ShortcutManager.class);

        List<ShortcutInfo> infos = new ArrayList<>();
        for (int i = 0; i < mShortcutManager.getMaxShortcutCountPerActivity(); i++) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setAction(Intent.ACTION_VIEW);
            intent.putExtra("msg", i+"   "+i+"   "+i);

            ShortcutInfo info = new ShortcutInfo.Builder(this, "id" + i)//作用未知
                    .setShortLabel(i+" ShortLabel")//短名字
                    .setLongLabel(i+" LongLabel")//长名字
                    .setIcon(Icon.createWithResource(this, R.drawable.icon))//图标
                    .setIntent(intent)//点击菜单后所触发intent
                    .build();
            infos.add(info);
//            manager.addDynamicShortcuts(Arrays.asList(info));
        }

        mShortcutManager.setDynamicShortcuts(infos);
    }
}
