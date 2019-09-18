package com.guolaiwan.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.widget.ImageView;

/**
 * 蔡朝阳:图片处理工具类
 */

public class PictureUtil {

    public static void setImageBackground(Bitmap bitmap, ImageView imageView, int width, int height) {
        //计算最佳缩放倍数,以填充宽高为目标
        float scaleX = (float) width / bitmap.getWidth();
        float scaleY = (float) height / bitmap.getHeight();
        float bestScale = scaleX > scaleY ? scaleX : scaleY;
        //以填充高度的前提下，计算最佳缩放倍数
        float subX = (width - bitmap.getWidth() * bestScale) / 2;
        float subY = (height - bitmap.getHeight() * bestScale) / 2;

        Matrix imgMatrix = new Matrix();
        imageView.setScaleType(ImageView.ScaleType.MATRIX);
        //缩放最佳大小
        imgMatrix.postScale(bestScale, bestScale);
        //移动到居中位置显示
        imgMatrix.postTranslate(subX, subY);
        //设置矩阵
        imageView.setImageMatrix(imgMatrix);
        imageView.setImageBitmap(bitmap);
    }
}
