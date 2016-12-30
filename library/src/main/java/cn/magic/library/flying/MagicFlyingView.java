package cn.magic.library.flying;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by yan on 16-12-25.
 */

public class MagicFlyingView extends View {
    private SparseArray<BezierWarpEvaluator.ValueState> sparseArray = new SparseArray<>();
    private List<Bitmap> mBitmapList = new ArrayList<>();

    private int mMeasureH, mMeasureW;
    private Rect mSrcRect, mDestRect;
    private Paint mPaint;

    private boolean mIsAutoPlay = false;

    public MagicFlyingView(Context context) {
        this(context, null);
    }

    public MagicFlyingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MagicFlyingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mSrcRect = new Rect();
        mDestRect = new Rect();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    public void clearDrawable() {
        mBitmapList.clear();
    }

    public void addDrawable(Bitmap bitmap) {
        mBitmapList.add(bitmap);
    }

    public void addDrawable(int resId) {
        Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), resId);
        mBitmapList.add(bitmap);
    }

    public void flyingAutoStart() {
        if (mIsAutoPlay == false && sparseArray.size() <= 0) {
            mIsAutoPlay = true;
            flyingAuto(new Random().nextInt(10));
        }
    }

    public boolean isAutoFlying() {
        return mIsAutoPlay;
    }

    public void flyingAutoStop() {
        mIsAutoPlay = false;
    }

    public void flyingAuto(int delay) {
        getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                flying();
                Log.i("YYYY", "end------------------------flyingAuto ="+mIsAutoPlay);
                if (mIsAutoPlay) {
                    flyingAuto(new Random().nextInt(500));
                }
            }
        }, delay);
    }

    public void flying() {
        this.post(new Runnable() {
            @Override
            public void run() {
                int randomIndex = new Random().nextInt(mBitmapList.size());
                BezierWarpEvaluator evaluator = new BezierWarpEvaluator(mBitmapList.get(randomIndex));
                BezierWarpEvaluator.ValueState start = new BezierWarpEvaluator.ValueState();
                start.alpha = 255;
                start.scale = 0.1f;
                start.pointF = new PointF(mMeasureW/2, mMeasureH);
                BezierWarpEvaluator.ValueState end = new BezierWarpEvaluator.ValueState();
                end.alpha = 0;
                end.scale = 0.1f;
                end.pointF = new PointF(new Random().nextInt(mMeasureW), 0);
                ValueAnimator animator = ValueAnimator.ofObject(evaluator, start, end);
                animator.setDuration(4000);
                animator.setInterpolator(new AccelerateInterpolator());
                MagicAnimatorListener listener = new MagicAnimatorListener();
                animator.addUpdateListener(listener);
                animator.addListener(new MagicListener(listener.hashCode()));
                animator.start();
            }
        });
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility == VISIBLE) {
            //cancel release, auto
        } else {
            mIsAutoPlay = false;
        }
    }

    private class MagicAnimatorListener implements ValueAnimator.AnimatorUpdateListener {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            sparseArray.put(this.hashCode(), (BezierWarpEvaluator.ValueState) animation.getAnimatedValue());
            postInvalidate();
        }
    }

    private class MagicListener extends AnimatorListenerAdapter {
        private int key;

        public MagicListener(int key) {
            this.key = key;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            sparseArray.remove(key);
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            sparseArray.remove(key);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mMeasureH = this.getMeasuredHeight();
        mMeasureW = this.getMeasuredWidth();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int index = 0; index< sparseArray.size(); index++) {
            BezierWarpEvaluator.ValueState valueState = sparseArray.valueAt(index);
            if (valueState != null) {
                mSrcRect.left = 0;
                mSrcRect.top = 0;
                mSrcRect.right = valueState.bitmap.getWidth();
                mSrcRect.bottom = valueState.bitmap.getHeight();

                mDestRect.left = (int) valueState.pointF.x;
                mDestRect.top = (int) valueState.pointF.y;
                mDestRect.right = mDestRect.left + (int) (valueState.scale * valueState.bitmap.getWidth());
                mDestRect.bottom = mDestRect.top + (int) (valueState.scale * valueState.bitmap.getHeight());
                mPaint.setAlpha(valueState.alpha);
                canvas.drawBitmap(valueState.bitmap, mSrcRect, mDestRect, mPaint);
            }
        }
    }
}
