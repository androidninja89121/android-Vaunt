package android.vaunt.ui;

import android.vaunt.R;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.vaunt.utility.DBUtil;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;


/**
 * Created by nine3_marks on 3/27/2015.
 */
public class DynamicImageView extends ImageView {

    public DynamicImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void loadRoundedImage(String imageUrl) {
        Picasso.with(getContext()
                .getApplicationContext())
                .load(imageUrl)
                .fit().centerCrop()
                .placeholder(R.drawable.avatar_placeholder)
                .transform(new RoundedTransformation(20, 0)).into(this);
    }

    public void loadImage(String imageUrl) {
        Picasso.with(getContext()
                .getApplicationContext())
                .load(imageUrl)
                .fit().centerCrop().into(this);
    }

    public void loadImage(String imageUrl, final int placeHolderResId, ImageLoader imageLoader) {
        imageUrl = imageUrl.replaceAll(" ", "%20");

        final String strUrl = imageUrl;

        Bitmap bmp = DBUtil.getInstance().bmpWithName(imageUrl);

        if(bmp == null){
            final ImageView tmpImageView = this;

            imageLoader.loadImage(strUrl, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {
                    tmpImageView.setImageResource(placeHolderResId);
                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    int fadeInDuration = 3000;

                    Animation fadeIn = new AlphaAnimation(0, 1);
                    fadeIn.setInterpolator(new DecelerateInterpolator()); // add this
                    fadeIn.setDuration(fadeInDuration);

                    tmpImageView.setScaleType(ScaleType.FIT_XY);

                    int vw = tmpImageView.getMeasuredWidth();
                    int vh = tmpImageView.getMeasuredHeight();

                    int bw = bitmap.getWidth();
                    int bh = bitmap.getHeight();

                    int dw, dh;

                    if(vh * bw >= bh * vw){
                        dw = bh * vw / vh;
                        dh = bh;
                    }else{
                        dw = bw;
                        dh = bw * vh / vw;
                    }

                    Bitmap resBmp = scaleCenterCrop(bitmap, dh, dw);

                    tmpImageView.setImageBitmap(resBmp);
                    tmpImageView.setAnimation(fadeIn);

                    DBUtil.getInstance().arrBitmap.add(resBmp);
                    DBUtil.getInstance().arrBmpName.add(strUrl);
                }

                @Override
                public void onLoadingCancelled(String s, View view) {

                }
            });
        }else{
            int fadeInDuration = 3000;

            Animation fadeIn = new AlphaAnimation(0, 1);
            fadeIn.setInterpolator(new DecelerateInterpolator()); // add this
            fadeIn.setDuration(fadeInDuration);
            this.setImageBitmap(bmp);
            this.setAnimation(fadeIn);
        }
    }

    public Bitmap scaleCenterCrop(Bitmap source, int newHeight, int newWidth) {
        int sourceWidth = source.getWidth();
        int sourceHeight = source.getHeight();

        // Compute the scaling factors to fit the new height and width, respectively.
        // To cover the final image, the final scaling will be the bigger
        // of these two.
        float xScale = (float) newWidth / sourceWidth;
        float yScale = (float) newHeight / sourceHeight;
        float scale = Math.max(xScale, yScale);

        // Now get the size of the source bitmap when scaled
        float scaledWidth = scale * sourceWidth;
        float scaledHeight = scale * sourceHeight;

        // The target rectangle for the new, scaled version of the source bitmap will now
        // be
        RectF targetRect = new RectF(0, 0, scaledWidth, scaledHeight);

        // Finally, we create a new bitmap of the specified size and draw our new,
        // scaled bitmap onto it.
        Bitmap dest = Bitmap.createBitmap(newWidth, newHeight, source.getConfig());
        Canvas canvas = new Canvas(dest);
        canvas.drawBitmap(source, null, targetRect, null);

        return dest;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    public class RoundedTransformation implements
            com.squareup.picasso.Transformation {
        private final int radius;
        private final int margin; // dp

        // radius is corner radii in dp
        // margin is the board in dp
        public RoundedTransformation(final int radius, final int margin) {
            this.radius = 200;//R.dimen.avatar_size
            this.margin = 0;
        }

        @Override
        public Bitmap transform(final Bitmap source) {
            final Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setShader(new BitmapShader(source, Shader.TileMode.CLAMP,
                    Shader.TileMode.CLAMP));

            try {
                Bitmap output = Bitmap.createBitmap(source.getWidth(),
                        source.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(output);
                canvas.drawRoundRect(new RectF(margin, margin, source.getWidth()
                        - margin, source.getHeight() - margin), radius, radius, paint);

                if (source != output) {
                    source.recycle();
                }

                return output;
            } catch (Exception e) {
                e.printStackTrace();
                return source;
            }
        }

        @Override
        public String key() {
            return "rounded";
        }
    }
}
