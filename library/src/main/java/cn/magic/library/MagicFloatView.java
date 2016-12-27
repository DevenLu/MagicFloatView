package cn.magic.library;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceView;

/**
 * Created by yan on 16-12-24.
 */

public class MagicFloatView extends SurfaceView {
    public MagicFloatView(Context context) {
        this(context, null);
    }

    public MagicFloatView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MagicFloatView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
