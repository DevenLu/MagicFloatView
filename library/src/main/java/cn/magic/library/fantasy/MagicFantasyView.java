package cn.magic.library.flying;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.animation.AccelerateInterpolator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by yan on 16-12-25.
 */

public class MagicFantasyView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private SparseArray<BezierWarpEvaluator.ValueState> sparseArray = new SparseArray<>();
    private List<Bitmap> mBitmapList = new ArrayList<>();

    private int mMeasureH, mMeasureW;
    private Rect mSrcRect, mDestRect;
    private Paint mPaint;

    private boolean mIsAutoPlay = false;

    private SurfaceHolder mHolder;
    private Canvas mCanvas;
    private Thread mThread;
    private boolean mIsRunning;

    public MagicFantasyView(Context context) {
        this(context, null);
    }

    public MagicFantasyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MagicFantasyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mSrcRect = new Rect();
        mDestRect = new Rect();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);

        mHolder = getHolder();
        mHolder.addCallback(this);
    }

    public void clearDrawable() {
        mBitmapList.clear();
    }

    public void addDrawable(Bitmap bitmap) {
        mBitmapList.add(bitmap);
    }

    public void addDrawable(int resId) {
        Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), resId);
        mSrcRect.left = 0;
        mSrcRect.top = 0;
        mSrcRect.right = bitmap.getWidth();
        mSrcRect.bottom = bitmap.getHeight();

        mDestRect.left = 0;
        mDestRect.top = 0;
        mDestRect.right = bitmap.getWidth();
        mDestRect.bottom = bitmap.getHeight();
        mBitmapList.add(bitmap);
    }

    public void flyingAuto() {
        mIsAutoPlay = true;

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

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mIsRunning = true;
        mThread = new Thread(this);
        mThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mIsRunning = false;
    }

    @Override
    public void run() {
        while (mIsRunning) {
            try {
                mCanvas = mHolder.lockCanvas();
                if (mCanvas != null) {
                    mCanvas.drawColor(Color.WHITE);
                    for (int index = 0; index< sparseArray.size(); index++) {
                        BezierWarpEvaluator.ValueState valueState = sparseArray.valueAt(index);
                        if (valueState != null) {
                            mDestRect.left = (int) valueState.pointF.x;
                            mDestRect.top = (int) valueState.pointF.y;
                            mDestRect.right = mDestRect.left + (int) (valueState.scale * valueState.bitmap.getWidth());
                            mDestRect.bottom = mDestRect.top + (int) (valueState.scale * valueState.bitmap.getHeight());
                            mPaint.setAlpha(valueState.alpha);
                            mCanvas.drawBitmap(valueState.bitmap, mSrcRect, mDestRect, mPaint);
                        }
                    }
                }
            } catch (Exception e) {

            } finally {
                if (mCanvas != null) {
                    mHolder.unlockCanvasAndPost(mCanvas);
                }
            }
        }
    }

    private class MagicAnimatorListener implements ValueAnimator.AnimatorUpdateListener {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            sparseArray.put(this.hashCode(), (BezierWarpEvaluator.ValueState) animation.getAnimatedValue());
//            postInvalidate();
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

            Log.i("YYYY", "end------------------------sparseArray.size="+sparseArray.size());
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
}
