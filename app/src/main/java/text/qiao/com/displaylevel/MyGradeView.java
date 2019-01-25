package text.qiao.com.displaylevel;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by Administrator on 2019/1/25.
 * 自定义  等级
 */

public class MyGradeView extends View {

    private String TAG = "MyGradeView";
    private Paint mPaint_jindu;//当前等级进度
    private Paint mPaint_qishi;//全部等级

    private Paint mPaint_quan_da;//全部圆环
    private Paint mPaint_quan_xiao;//全部圆环
    private Paint mPaint_dangqian_da;//当前圆环
    private Paint mPaint_dangqian_xiao;//当前圆环
    private Paint mPaint_zi;//字
    private Paint mPaint_quanbu;//字
    private Paint mPaint_dangqian;//字

    private int radius_da = 35;//大圆半径
    private int radius_xiao = 27;//小圆半径
    private int zi_cuowei = DensityUtil.dip2px(getContext(), 5);//显示数字移动距离

    private int radius_tupian = 200;//头像背景
    /**
     * 头像半径
     */
    private int head_radius;
    private int screenWidth;//布局的宽
    private int screenHeight;//布局的高
    private int color_jindu = R.color.grade_dangqian;//当前等级进度的颜色
    private int color_quanbujindu = R.color._f3f3f3;//全部等级进度的颜色
    private int color_qishi = R.color._f3f3f3;//全部等级的颜色
    private int color_quan_xiao = R.color.textview_verificationcode_nonclickable;//全部圆环小的颜色
    private int color_quan_da = R.color._fcfcfc;//全部圆环大的颜色
    private int color_dangqian_da = R.color.grade_dangqian;//当前 圆环  大的颜色
    private int color_dangqian_xiao = R.color._fff5b2;//当前 圆环  小的颜色
    private int color_zi_nicheng = R.color.edittest_guangbiao;//呢称 和当前等级
    private int color_zi_dangqian = R.color.edittest_guangbiao;//当前
    private int color_zi_quanbu = R.color.white;//全部
    private int jindu = 1;//当前等级
    private int kuandu = 15;
    private int dengji_gong = 6;//一共几个等级
    private int jvli = 130;//字体距离中间距离
    private String nicheng = "我的名字";
    private String dengjishu = "LV0";
    private String url = "LV0";
    private int fenmu = 10;

    private int mWidth = 0;//宽
    private int mHeight = 0;//高
    /**
     * 屏幕宽
     */
    private int screen_w = 0;
    private int qiShiWeiZhi = 0;
    /**
     * 属性动画
     */
    private Float percentage = 0f;

    /**
     * 起始位置
     * 终止位置
     */
    private int startY;
    private int startX;
    private int zongY;
    private int zongX;

    /**
     * 进度间隔
     */
    private int progressInterval;
    /**
     * 头像宽度
     */
    private int tou_kuan;


    public MyGradeView(Context context) {
        this(context, null);

    }

    public MyGradeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("ResourceAsColor")
    public MyGradeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable
                .grade, defStyleAttr, 0);
        color_jindu = typedArray.getColor(R.styleable.grade_color_jindu, R.color.grade_dangqian);

        color_quanbujindu = typedArray.getColor(R.styleable.grade_color_quanbujindu, R.color
                ._f3f3f3);//全部等级进度的颜色

        color_qishi = typedArray.getColor(R.styleable.grade_color_qishi, R.color._f3f3f3);//全部等级的颜色

        color_quan_xiao = typedArray.getColor(R.styleable.grade_color_quan_xiao, R.color
                .textview_verificationcode_nonclickable);//全部圆环小的颜色

        color_quan_da = typedArray.getColor(R.styleable.grade_color_quan_da, R.color._fcfcfc);
        //全部圆环大的颜色

        color_dangqian_da = typedArray.getColor(R.styleable.grade_color_dangqian_da, R.color
                .grade_dangqian);//当前 圆环  大的颜色

        color_dangqian_xiao = typedArray.getColor(R.styleable.grade_color_dangqian_xiao, R.color
                ._fff5b2);//当前 圆环  小的颜色

        color_zi_nicheng = typedArray.getColor(R.styleable.grade_color_zi_nicheng, R.color
                .edittest_guangbiao); //呢称 和当前等级

        color_zi_dangqian = typedArray.getColor(R.styleable.grade_color_zi_dangqian, R.color
                .edittest_guangbiao); //当前

        color_zi_quanbu = typedArray.getColor(R.styleable.grade_color_zi_quanbu, R.color.white);//全部

        dengji_gong = typedArray.getInteger(R.styleable.grade_dengji_gong, 6);

        radius_da = (int) typedArray.getDimension(R.styleable.grade_radius_da, 35f);

        radius_xiao = (int) typedArray.getDimension(R.styleable.grade_radius_xiao, 27f);

        radius_tupian = (int) typedArray.getDimension(R.styleable.grade_radius_tupian, 200f);

        kuandu = (int) typedArray.getDimension(R.styleable.grade_kuandu, 15f);

        init();
    }

    /**
     * 昵称
     *
     * @param nicheng
     */
    public void setNicheng(String nicheng) {
        this.nicheng = nicheng;
        invalidate();
    }

    /**
     * 等级
     *
     * @param dengjishu
     */
    public void setDengjishu(String dengjishu) {
        this.dengjishu = dengjishu;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawQuanBuJinDu(canvas);
        drawJinDu(canvas);
        drawDangqianyuan(canvas);
        drawQuanyuan(canvas);
        drawQiTa(canvas);
        getTouXiang(canvas);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获取屏幕的宽度
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context
                .WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        Display display = windowManager.getDefaultDisplay();
        display.getMetrics(dm);
        screen_w = dm.widthPixels;
        // 宽度大小;
        screenWidth = screen_w - DensityUtil.dip2px(getContext(), 30);
        qiShiWeiZhi = screenWidth / fenmu;
        Log.e(TAG, "screenWidth:" + screenWidth);
        radius_da = screenWidth / 25;
        radius_xiao = screenWidth / 25 - 5;
        // 高度大小;
        screenHeight = MeasureSpec.getSize(heightMeasureSpec);

        startX = screenWidth / fenmu;
        startY = screenHeight / 2;
        zongY = screenHeight / 2;
        zongX = screenWidth * (fenmu - 1) / fenmu;

        progressInterval = screenWidth / fenmu;
        head_radius = radius_tupian / 2;

    }

    /**
     * 初始化画笔等
     */
    private void init() {
        mPaint_jindu = new Paint();
        mPaint_jindu.setStyle(Paint.Style.FILL);
        mPaint_jindu.setColor(getResources().getColor(color_jindu));
        mPaint_jindu.setAntiAlias(true);

        mPaint_qishi = new Paint();
        mPaint_qishi.setStyle(Paint.Style.FILL);
        mPaint_qishi.setColor(getResources().getColor(color_quanbujindu));
        mPaint_qishi.setAntiAlias(true);

        mPaint_quan_da = new Paint();
        mPaint_quan_da.setStyle(Paint.Style.FILL);
        mPaint_quan_da.setColor(getResources().getColor(color_quan_da));
        mPaint_quan_da.setAntiAlias(true);

        mPaint_quan_xiao = new Paint();
        mPaint_quan_xiao.setStyle(Paint.Style.FILL);
        mPaint_quan_xiao.setColor(getResources().getColor(color_quan_xiao));
        mPaint_quan_xiao.setAntiAlias(true);

        mPaint_dangqian_da = new Paint();
        mPaint_dangqian_da.setStyle(Paint.Style.FILL);
        mPaint_dangqian_da.setColor(getResources().getColor(color_dangqian_da));
        mPaint_dangqian_da.setAntiAlias(true);

        mPaint_dangqian_xiao = new Paint();
        mPaint_dangqian_xiao.setStyle(Paint.Style.FILL);
        mPaint_dangqian_xiao.setColor(getResources().getColor(color_dangqian_xiao));
        mPaint_dangqian_xiao.setAntiAlias(true);

        mPaint_zi = new Paint();
        mPaint_zi.setStyle(Paint.Style.FILL);
        mPaint_zi.setColor(getResources().getColor(color_zi_nicheng));
        mPaint_zi.setAntiAlias(true);
        mPaint_zi.setTextSize(50);

        mPaint_quanbu = new Paint();
        mPaint_quanbu.setStyle(Paint.Style.FILL);
        mPaint_quanbu.setColor(getResources().getColor(color_zi_quanbu));
        mPaint_quanbu.setAntiAlias(true);
        mPaint_quanbu.setTextSize(DensityUtil.dip2px(getContext(), 15));

        mPaint_dangqian = new Paint();
        mPaint_dangqian.setStyle(Paint.Style.FILL);
        mPaint_dangqian.setColor(getResources().getColor(color_zi_dangqian));
        mPaint_dangqian.setAntiAlias(true);
        mPaint_dangqian.setTextSize(DensityUtil.dip2px(getContext(), 15));

    }


    /**
     * 当前等级
     *
     * @param jindu
     */
    public void setJindu(int jindu) {
        this.jindu = jindu;
        invalidate();
    }

    /**
     * 画全部进度
     *
     * @param canvas
     */
    private void drawQuanBuJinDu(Canvas canvas) {
        canvas.drawRect(startX, startY - kuandu, zongX, zongY + kuandu, mPaint_qishi);
    }

    /**
     * 画当前进度
     *
     * @param canvas
     */
    private void drawJinDu(Canvas canvas) {
        Log.e(TAG, "getX_jindu() * percentage:" + getX_jindu() * percentage);
        canvas.drawRect(startX, startY - kuandu, getX_jindu(), zongY + kuandu, mPaint_jindu);
    }

    /**
     * 计算进度
     *
     * @return
     */
    private int getX_jindu() {
        int x_jj = 0;
        if (jindu != dengji_gong && jindu != 0) {
            //最后一个进度圆的位置
            int jinduyuan = (int) (startX + ((jindu - 1) * progressInterval));
            //第一个起始圆的位置
            int quanbuyuan = (zongX - ((dengji_gong - (jindu + 2)) * progressInterval));
            x_jj = (int) (startX + (((quanbuyuan - jinduyuan) / 2) + (jindu - 1) *
                    progressInterval) * percentage);
        } else if (jindu == dengji_gong) {
            x_jj = (int) (qiShiWeiZhi + (getZhi() * percentage));
        } else if (jindu == 0) {
            x_jj = (int) (qiShiWeiZhi + (getZhi()));
        }

        return x_jj;
    }

    /**
     * 进度圆
     *
     * @param canvas
     */
    private void drawDangqianyuan(Canvas canvas) {
        if (jindu > 0) {
            for (int i = 0; i < jindu; i++) {
                canvas.drawCircle(startX + (i * progressInterval), screenHeight /
                        2, radius_da, mPaint_dangqian_da);
                canvas.drawCircle(qiShiWeiZhi + (i * progressInterval), screenHeight /
                        2, radius_xiao, mPaint_dangqian_xiao);
                canvas.drawText("" + i, qiShiWeiZhi + (i * progressInterval) -
                        zi_cuowei, screenHeight / 2 + zi_cuowei, mPaint_dangqian);
            }
        }
    }

    /**
     * 全部
     *
     * @param canvas
     */
    private void drawQuanyuan(Canvas canvas) {

        for (int i = 0; i < dengji_gong - (jindu + 1); i++) {
            canvas.drawCircle(zongX - (i * progressInterval),
                    zongY, radius_da, mPaint_quan_da);
            canvas.drawCircle(zongX - (i * progressInterval),
                    zongY, radius_xiao, mPaint_quan_xiao);
            canvas.drawText("" + (dengji_gong - i - 1), zongX - (i * progressInterval) -
                    zi_cuowei, zongY + zi_cuowei, mPaint_quanbu);
        }
    }


    /**
     * 呢称 、当前等级文字  、头像背景
     *
     * @param canvas
     */
    private void drawQiTa(Canvas canvas) {
        //呢称
        canvas.drawText(nicheng, weiZhiDistance(nicheng),
                zongY - jvli, mPaint_zi);
        //当前等级文字
        canvas.drawText(dengjishu, weiZhiDistance(dengjishu),
                zongY + jvli + 30, mPaint_zi);
    }

    /**
     * 昵称  等级  位置距离
     *
     * @param weizhi
     * @return
     */
    private int weiZhiDistance(String weizhi) {
        int kuan = (int) mPaint_zi.measureText(weizhi, 0, weizhi.length());
        Log.e(TAG, "weizhi:" + kuan);
        if (nicheng.length() <= radius_tupian) {
            Log.e(TAG, "weizhi1:" + kuan);
            return qiShiWeiZhi + getZhi() - kuan / 2;
        } else {
            Log.e(TAG, "weizhi2:" + kuan);
            return qiShiWeiZhi + getZhi() - head_radius;
        }

    }

    /**
     * 头像
     *
     * @param canvas
     */
    public void getTouXiang(final Canvas canvas) {

        Drawable drawable = getContext().getResources().getDrawable(R.mipmap.ic_launcher);
        Bitmap bitmap1 = drawableToBitmap(drawable);
        canvas.drawBitmap(bitmap1, qiShiWeiZhi + getZhi() - radius_tupian / 2,
                zongY - radius_tupian / 2, mPaint_dangqian_da);
    }


    public void start() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "percentage", 0f, 1f);
        animator.setDuration(5000)
                .start();
    }


    public void setPercentage(float percentage) {
        Log.e(TAG, "percentage:" + percentage);
        this.percentage = percentage;
        invalidate();
    }

    /**
     * 平均每个等级的间隔
     *
     * @return
     */
    private int getZhi() {
        return ((zongX - startY) / (dengji_gong - 1)) * jindu;

    }


    public static Bitmap drawableToBitmap(Drawable drawable) {
        // 取 drawable 的长宽
        int tou_kuan = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();

        // 取 drawable 的颜色格式
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        // 建立对应 bitmap
        Bitmap bitmap = Bitmap.createBitmap(tou_kuan, h, config);
        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, tou_kuan, h);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }


}
