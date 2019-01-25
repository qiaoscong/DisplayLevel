package text.qiao.com.displaylevel;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;

/**
* @ClassName: DensityUtil 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author yiw
* @date 2015-12-28 下午4:17:01 
 */
public class DensityUtil {
	private static float sNoncompatDensity;
	private static float sNoncompatScaleDensity;

	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/** 获取手机的密度*/
	public static float getDensity(Context context) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		return dm.density;
	}


	public  static void setCustomDensity(@NonNull Activity activity, @NonNull final Application application) {
		final DisplayMetrics appDisplayMetrics = application.getResources().getDisplayMetrics();
		if (sNoncompatDensity == 0) {
			sNoncompatDensity = appDisplayMetrics.density;
			sNoncompatScaleDensity = appDisplayMetrics.scaledDensity;
		}
		application.registerComponentCallbacks(new ComponentCallbacks() {
			@Override
			public void onConfigurationChanged(Configuration configuration) {
				if (configuration != null && configuration.fontScale > 0) {
					sNoncompatScaleDensity = application.getResources().getDisplayMetrics().scaledDensity;
				}
			}

			@Override
			public void onLowMemory() {
			}
		});

		final float targetDensity = appDisplayMetrics.widthPixels / 375;
		final float targetScaleDensity = targetDensity * (sNoncompatScaleDensity / sNoncompatDensity);
		final int targetDensityDpi = (int) (160 * targetDensity);

		appDisplayMetrics.density = appDisplayMetrics.scaledDensity = targetDensity;
		appDisplayMetrics.densityDpi = targetDensityDpi;
		appDisplayMetrics.scaledDensity = targetScaleDensity;

		final DisplayMetrics activityDisplayMetrics = activity.getResources().getDisplayMetrics();
		activityDisplayMetrics.density = activityDisplayMetrics.scaledDensity = targetDensity;
		activityDisplayMetrics.densityDpi = targetDensityDpi;
		activityDisplayMetrics.scaledDensity = targetScaleDensity;
	}
}
