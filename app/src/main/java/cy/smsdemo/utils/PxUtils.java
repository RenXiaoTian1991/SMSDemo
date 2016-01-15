package cy.smsdemo.utils;

import android.content.Context;

/**
 * Created by cuiyue on 15/10/19.
 */
public class PxUtils {


    public static int Dp2Px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
