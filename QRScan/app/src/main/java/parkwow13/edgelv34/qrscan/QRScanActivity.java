package parkwow13.edgelv34.qrscan;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.BarcodeView;
import com.journeyapps.barcodescanner.CameraPreview;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.util.List;

public class QRScanActivity extends AppCompatActivity {

    private final String TAG = QRScanActivity.class.getName();
    private DecoratedBarcodeView mDbarcodeView;
    private BeepManager beepManager;
    private String lastURLText;
    private ImageView laser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscan);
        overridePendingTransition(R.anim.alpha, R.anim.alpha_disa);

        checkCameraPermission();

        mDbarcodeView = findViewById(R.id.zxing_barcode_scanner);
        mDbarcodeView.decodeContinuous(callback);
        mDbarcodeView.setStatusText(getString(R.string.scan_prompt));

        final BarcodeView barcodeView = mDbarcodeView.getBarcodeView();

        barcodeView.addStateListener(new CameraPreview.StateListener() {
            @Override
            public void previewSized() {
                if (laser == null) {
                    laser = new ImageView(QRScanActivity.this);
                    laser.setBackgroundResource(R.drawable.qr_laser);

                    Rect rect = barcodeView.getFramingRect();
                    ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(rect.width() - (int)Util.getDensity(QRScanActivity.this,2.0f), (int)Util.getDensity(QRScanActivity.this,2.0f));

                    laser.setLayoutParams(params);

                    laser.setX(rect.left + (int)Util.getDensity(QRScanActivity.this,1f));
                    laser.setY(0);

                    mDbarcodeView.addView(laser);

                    animateLaser(laser, rect.top + (int)Util.getDensity(QRScanActivity.this, 2f), rect.bottom - (int)Util.getDensity(QRScanActivity.this, 4f));
                }
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
    protected void onResume() {
        super.onResume();
        mDbarcodeView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDbarcodeView.pause();
    }

    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            String resultUrl = result.getText();
            Log.d(TAG, "resultUrl : " + resultUrl + " , lastURLText : " + lastURLText);
            if(result.getText() == null || result.getText().equals(lastURLText)) {
                // Prevent duplicate scans
                return;
            }

            lastURLText = result.getText();
//            beepManager.playBeepSoundAndVibrate();

        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

    private void animateLaser(View view, int startPos, int endPos) {
        Animation transDownLaser = new TranslateAnimation(0, 0, startPos, endPos);
        transDownLaser.setInterpolator(new AccelerateDecelerateInterpolator());
        transDownLaser.setRepeatMode(Animation.REVERSE);
        transDownLaser.setRepeatCount(Animation.INFINITE);
        transDownLaser.setDuration(2000);
        transDownLaser.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        view.setAnimation(transDownLaser);
        view.startAnimation(transDownLaser);

    }

    private boolean checkCameraPermission() {
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            if(ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CAMERA)){
                //최초 접속이 아니고, 사용자가 다시 보지 않기에 체크를 하지 않고, 거절만 누른경우
            }else{
                //최초 접속시, 사용자가 다시 보지 않기에 체크를 했을 경우
            }
            Log.d(TAG,"퍼미션 수락 팝업 출력");
            //Manifest.permission.READ_CALENDAR이 접근 거절 상태 일때
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA} ,0);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean isExistDeclinePermission = false;
        for (int i = 0 ; i < permissions.length ; i++) {
            Log.d(TAG,"request permisstion result reqCode : " + requestCode + " permission : " + permissions[i] + " result : " + grantResults[i]);
            if (grantResults[i] == -1) {
                isExistDeclinePermission = true;
            }
        }

        if(requestCode == 0){

            if (isExistDeclinePermission) {

                AlertDialog.Builder ab = new AlertDialog.Builder(QRScanActivity.this);
                ab.setTitle(getString(R.string.pemission_decline_title));
                ab.setMessage(getString(R.string.permission_decline_camera));
                ab.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        finish();
                    }
                });

                AlertDialog alertDialog = ab.create();
                alertDialog.setCancelable(false);
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.show();
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(0, R.anim.alpha_disa);
    }
}
