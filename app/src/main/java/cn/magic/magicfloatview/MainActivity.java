package cn.magic.magicfloatview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import cn.magic.library.flying.MagicFlyingSurfaceView;

public class MainActivity extends AppCompatActivity {
    private MagicFlyingSurfaceView magicFlyingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        magicFlyingView = (MagicFlyingSurfaceView) this.findViewById(R.id.flyingView);
        magicFlyingView.addDrawable(R.drawable.favourite_love_blue);
        magicFlyingView.addDrawable(R.drawable.favourite_love_pink);
        magicFlyingView.addDrawable(R.drawable.favourite_love_red);
        magicFlyingView.addDrawable(R.drawable.favourite_love_yellow);

        ((Button)(this.findViewById(R.id.btn))).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                magicFlyingView.flying();
            }
        });
    }
}
