package parkwow13.edgelv34.qrscan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void goScan(View v) {
        Intent intent = new Intent(this, QRScanActivity.class);
        startActivity(intent);
    }
}
