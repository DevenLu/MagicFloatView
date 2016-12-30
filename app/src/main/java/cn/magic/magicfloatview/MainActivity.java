package cn.magic.magicfloatview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import cn.magic.library.flying.MagicFlyingView;
import cn.magic.library.gift.MagicGiftLinearLayout;

public class MainActivity extends AppCompatActivity {
    private MagicGiftLinearLayout giftLinearLayout;
    private MagicFlyingView magicFlyingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        giftLinearLayout = (MagicGiftLinearLayout) this.findViewById(R.id.gift_layout);
        magicFlyingView = (MagicFlyingView) this.findViewById(R.id.flying_view);
        magicFlyingView.addDrawable(R.drawable.favourite_love_blue);
        magicFlyingView.addDrawable(R.drawable.favourite_love_pink);
        magicFlyingView.addDrawable(R.drawable.favourite_love_red);
        magicFlyingView.addDrawable(R.drawable.favourite_love_yellow);

        ((Button)(this.findViewById(R.id.btn))).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (magicFlyingView.isAutoFlying()) {
                    magicFlyingView.flyingAutoStop();
                } else {
                    magicFlyingView.flyingAutoStart();
                }
            }
        });
    }
}
