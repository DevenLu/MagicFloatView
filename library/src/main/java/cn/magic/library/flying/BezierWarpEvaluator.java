package cn.magic.library.flying;

import android.animation.TypeEvaluator;
import android.graphics.Bitmap;
import android.graphics.PointF;

import java.util.Random;

/**
 * Created by yan on 16-12-25.
 */

public class BezierWarpEvaluator implements TypeEvaluator<BezierWarpEvaluator.ValueState> {
    private PointF pointF1, pointF2;
    private Bitmap mBitmap;

    public BezierWarpEvaluator(Bitmap bitmap) {
        mBitmap = bitmap;
        pointF1 = getPointF(2);
        pointF2 = getPointF(1);
    }

    private PointF getPointF(int scale) {

        PointF pointF = new PointF();
        pointF.x = new Random().nextInt((600 - 100));
        pointF.y = new Random().nextInt((1000 - 100)) / scale;
        return pointF;
    }

    @Override
    public ValueState evaluate(float fraction, ValueState startValue, ValueState endValue) {
        float timeLeft = 1.0f - fraction;

        ValueState valueState = new ValueState();
        PointF point = new PointF();
        point.x = timeLeft * timeLeft * timeLeft * (startValue.pointF.x) + 3
                * timeLeft * timeLeft * fraction * (pointF1.x) + 3 * timeLeft
                * fraction * fraction * (pointF2.x) + fraction * fraction * fraction * (endValue.pointF.x);

        point.y = timeLeft * timeLeft * timeLeft * (startValue.pointF.y) + 3
                * timeLeft * timeLeft * fraction * (pointF1.y) + 3 * timeLeft
                * fraction * fraction * (pointF2.y) + fraction * fraction * fraction * (endValue.pointF.y);
        valueState.pointF = point;
        valueState.scale = Math.abs((float)(1.0f - Math.pow((1.0f - fraction), 2 * 5)));
        valueState.alpha = (int) (timeLeft * 255);
        valueState.bitmap = mBitmap;
        return valueState;
    }

    public static class ValueState {
        public Bitmap bitmap;
        public int alpha;
        public float scale;
        public PointF pointF;

        @Override
        public String toString() {
            return "{alpha="+alpha+", scale="+scale+", pointF="+pointF.toString()+"}";
        }
    }
}
