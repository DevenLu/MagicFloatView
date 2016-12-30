package cn.magic.library.gift;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by yan on 16-12-30.
 */

public class MagicGiftLinearLayout extends LinearLayout {
    public MagicGiftLinearLayout(Context context) {
        this(context, null);
    }

    public MagicGiftLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MagicGiftLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        canvas.drawCircle(100, 100, 100, new Paint());
    }
}
