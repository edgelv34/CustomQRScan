package parkwow13.edgelv34.qrscan;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.journeyapps.barcodescanner.CameraPreview;
import com.journeyapps.barcodescanner.ViewfinderView;

/**
 * Created by com on 2017-12-27.
 */

public class CustomViewfinderView extends ViewfinderView {

    public CustomViewfinderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setCameraPreview(CameraPreview view) {
        this.cameraPreview = view;
        view.addStateListener(new CameraPreview.StateListener() {
            @Override
            public void previewSized() {
                refreshSizes();
                invalidate();
            }

            @Override
            public void previewStarted() {

            }

            @Override
            public void previewStopped() {

            }

            @Override
            public void cameraError(Exception error) {

            }

            @Override
            public void cameraClosed() {

            }
        });
    }

    @Override
    public void onDraw(Canvas canvas) {
        refreshSizes();
        if (framingRect == null || previewFramingRect == null) {
            return;
        }

        Rect frame = framingRect;

        int width = canvas.getWidth();
        int height = canvas.getHeight();

        // Draw the exterior (i.e. outside the framing rect) darkened
        paint.setColor(resultBitmap != null ? resultColor : maskColor);
        canvas.drawRect(0, 0, width, frame.top, paint);
        canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
        canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1, paint);
        canvas.drawRect(0, frame.bottom + 1, width, height, paint);

        //rect stroke
        paint.setColor(Util.getColor(getContext(), R.color.colorAccent));
        Paint.Style ps = paint.getStyle();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(Util.getDensity(getContext(), 1.0f));
        canvas.drawRect(frame.left, frame.top, frame.right, frame.bottom, paint);
        paint.setStyle(ps);

        if (resultBitmap != null) {
            // Draw the opaque result bitmap over the scanning rectangle
            paint.setAlpha(CURRENT_POINT_OPACITY);
            canvas.drawBitmap(resultBitmap, null, frame, paint);
        }
    }

    @Override
    public void drawViewfinder() {
        Bitmap resultBitmap = this.resultBitmap;
        this.resultBitmap = null;
        if (resultBitmap != null) {
            resultBitmap.recycle();
        }
        invalidate();
    }

    @Override
    public void drawResultBitmap(Bitmap result) {
        resultBitmap = result;
        invalidate();
    }

}
