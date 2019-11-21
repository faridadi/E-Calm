package com.ecalm.e_calm;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.camerakit.CameraKitView;
import com.ecalm.e_calm.math.calorie;
import com.ecalm.e_calm.sqlite.DatabaseHelper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import es.dmoral.toasty.Toasty;

public class CaptureActivity extends AppCompatActivity {

    private static final String MODEL_PATH = "detect.tflite";
    private static final boolean QUANT = true;
    private static final String LABEL_PATH = "file:///android_asset/foodLabel.txt";
    private static final int INPUT_SIZE = 300;
    private Classifier classifier;

    private Executor executor = Executors.newSingleThreadExecutor();
    private TextView textViewResult;
    private Button btnCapture, btnRecapture;
    private ImageView imageViewResult;
    private CameraKitView cameraKitView;
    private DatabaseHelper db;

    calorie cal = new calorie();
    LinearLayout rice, tempe, egg, bread;
    TextView ricew, tempew, eggw, breadw;
    TextView ricet, tempet, eggt, breadt;

    TextView done, back;

    ProgressDialog mProgressDialog;

    SharedPreferences sharedPreferences;

    int ricetmp =0, tempetmp=0, eggtmp =0, breadtmp=0;
    int totalCalorie;
    String dataUrl = "30";
    String url;
    private String TAG = "h4";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        cameraKitView = findViewById(R.id.camera);
        imageViewResult = findViewById(R.id.preview);
        textViewResult = findViewById(R.id.tv_result);
        textViewResult.setMovementMethod(new ScrollingMovementMethod());


        // masih manual erlu recycleview
        done = findViewById(R.id.doneCapture);
        back = findViewById(R.id.backCapture);

        rice = findViewById(R.id.riceItem);
        ricew = findViewById(R.id.riceItemWeight);
        ricet = findViewById(R.id.riceItemTotal);
        tempe = findViewById(R.id.tempeItem);
        tempew = findViewById(R.id.tempeItemWeight);
        tempet = findViewById(R.id.tempeItemTotal);
        egg = findViewById(R.id.eggItem);
        eggw = findViewById(R.id.eggItemWeight);
        eggt = findViewById(R.id.eggItemTotal);
        bread = findViewById(R.id.breadItem);
        breadw = findViewById(R.id.breadItemWeight);
        breadt = findViewById(R.id.breadItemTotal);

        rice.setVisibility(View.GONE);
        tempe.setVisibility(View.GONE);
        egg.setVisibility(View.GONE);
        bread.setVisibility(View.GONE);

        rice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totalCalorie += ricetmp;
                ricetmp =0;
                rice.setVisibility(View.GONE);
                Toasty.success(CaptureActivity.this, "Calorie Added").show();
            }
        });

        bread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totalCalorie += breadtmp;
                breadtmp = 0;
                Toasty.success(CaptureActivity.this, "Calorie Added").show();
                bread.setVisibility(View.GONE);
            }
        });

        tempe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totalCalorie += tempetmp;
                tempetmp = 0;
                Toasty.success(CaptureActivity.this, "Calorie Added").show();
                tempe.setVisibility(View.GONE);

            }
        });

        egg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totalCalorie += eggtmp;
                eggtmp = 0;
                Toasty.success(CaptureActivity.this, "Calorie Added").show();
                egg.setVisibility(View.GONE);
            }
        });


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUp();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        db = new DatabaseHelper(this);

        sharedPreferences = this.getSharedPreferences(SharedPrefManager.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        url = sharedPreferences.getString(SharedPrefManager.KEY_URL, null);

        btnCapture = findViewById(R.id.btn_capture);
        btnRecapture = findViewById(R.id.btn_recapture);

        btnCapture.setOnClickListener(photoOnClickListener);

        btnRecapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CaptureActivity.this, "Re Capture", Toast.LENGTH_SHORT).show();
                rice.setVisibility(View.GONE);
                tempe.setVisibility(View.GONE);
                egg.setVisibility(View.GONE);
                bread.setVisibility(View.GONE);
                btnRecapture.setVisibility(View.GONE);
                btnCapture.setVisibility(View.VISIBLE);
                imageViewResult.setVisibility(View.GONE);
                cameraKitView.setVisibility(View.VISIBLE);
            }
        });
        initTensorFlowAndLoadModel();

        btnCapture.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new Description().execute();
                return false;
            }
        });
    }


    private  void showPopUp(){
        AlertDialog.Builder alert = new AlertDialog.Builder(CaptureActivity.this);
        alert.setMessage("Are you Sure to add "+totalCalorie+" kcal ?")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int tmp = Integer.parseInt(sharedPreferences.getString(SharedPrefManager.KEY_CALORIE, null));
                        tmp += totalCalorie;
                        sharedPreferences.edit().putString(SharedPrefManager.KEY_CALORIE, Integer.toString(tmp)).apply();
                        CaptureActivity.this.onBackPressed();
                    }
                }).setNegativeButton("No", null);
        AlertDialog alert1 = alert.create();
        alert1.show();
    }
    private class Description extends AsyncTask<Void, Void, Void> {
        String desc;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(CaptureActivity.this);
            mProgressDialog.setTitle("Get Data");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try{
                Document doc = Jsoup.connect(url).get();
                Elements link = doc.getElementsByTag(TAG);
                dataUrl = link.get(0).text();
            }catch (Exception e){

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
             mProgressDialog.dismiss();
            //Toast.makeText(CaptureActivity.this, dataUrl, Toast.LENGTH_SHORT).show();
        }
    }


    private View.OnClickListener photoOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            //coding masih hardcode perlu ditambah recycleview
            cameraKitView = findViewById(R.id.camera);
            cameraKitView.captureImage(new CameraKitView.ImageCallback() {
                @Override
                public void onImage(CameraKitView cameraKitView, final byte[] photo) {
                    Glide.with(CaptureActivity.this).load(photo).override(600,600).into(imageViewResult);

                    //get weight from mikrocontroller
                    new Description().execute();

                    cameraKitView.setVisibility(View.GONE);
                    imageViewResult.setVisibility(View.VISIBLE);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(photo, 0, photo.length);

                    bitmap = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, true);
                    imageViewResult.setImageBitmap(bitmap);
                    final List<Classifier.Recognition> results = classifier.recognizeImage(bitmap);

                    Log.d("data 0" , results.get(0).getTitle());
                    Log.d("data 2" , results.get(1).getTitle());
                    Log.d("data 3" , results.get(2).getTitle());
                    Log.d("data 4" , results.get(3).getTitle());
                    Log.d("hasil tensorflow", results.toString());

                    //result adalah hasil dari tensorflow ada 10 data return
                    //text view dibawah adalah contoh hasilnya

                    rice.setVisibility(View.GONE);
                    tempe.setVisibility(View.GONE);
                    egg.setVisibility(View.GONE);
                    bread.setVisibility(View.GONE);


                    btnRecapture.setVisibility(View.VISIBLE);
                    btnCapture.setVisibility(View.GONE);


                    textViewResult.setVisibility(View.GONE);
                    textViewResult.setText(results.toString());

                    if (results.get(0).getTitle().equals("cooked rice")){
                        rice.setVisibility(View.VISIBLE);
                        ricew.setText(dataUrl+" gram");
                        ricetmp = cal.calculateCalorie(Integer.parseInt(dataUrl),129);
                        ricet.setText(ricetmp+" kcal");

                    }else if(results.get(0).getTitle().equals("white bread")){
                        bread.setVisibility(View.VISIBLE);
                        breadw.setText(dataUrl+" gram");
                        breadtmp = cal.calculateCalorie(Integer.parseInt(dataUrl),270);
                        breadt.setText(breadtmp+" kcal");

                    }else if(results.get(0).getTitle().equals("tempe")){
                        tempe.setVisibility(View.VISIBLE);
                        tempew.setText(dataUrl+" gram");
                        tempetmp = cal.calculateCalorie(Integer.parseInt(dataUrl),34);
                        tempet.setText(tempetmp+" kcal");

                    }else if(results.get(0).getTitle().equals("fried egg")){
                        egg.setVisibility(View.VISIBLE);
                        eggw.setText(dataUrl+" gram");
                        eggtmp = cal.calculateCalorie(Integer.parseInt(dataUrl),210);
                        eggt.setText(eggtmp+" kcal");
                    }

                }
            });
        }
    };




    //commit saat terakhir ok
    private void commit(){
        AlertDialog.Builder alert = new AlertDialog.Builder(CaptureActivity.this);
        alert.setMessage("Are  you sure ?")
                .setPositiveButton("Sure", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sharedPreferences.edit().putString(SharedPrefManager.KEY_URL, Integer.toString(totalCalorie)).apply();
                        onBackPressed();
                    }
                }).setNegativeButton("No", null);
        AlertDialog alert1 = alert.create();
        alert1.show();
    }



    private void initTensorFlowAndLoadModel() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    classifier = TensorFlowImageClassifier.create(
                            getAssets(),
                            MODEL_PATH,
                            LABEL_PATH,
                            INPUT_SIZE,
                            QUANT);
                    makeButtonVisible();
                } catch (final Exception e) {
                    throw new RuntimeException("Error initializing TensorFlow!", e);
                }
            }
        });
    }

    private void makeButtonVisible() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                btnCapture.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        cameraKitView.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    protected void onStart() {
        super.onStart();
        cameraKitView.onStart();
    }

    @Override
    protected void onResume() {
        //onresume error
        //cameraKitView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        //on pause error
        //cameraKitView.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        cameraKitView.onStop();
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        cameraKitView.onStop();
        Intent i = new Intent(CaptureActivity.this, MainActivity.class);
        startActivity(i);
        finish();
        //super.onBackPressed();
    }
}