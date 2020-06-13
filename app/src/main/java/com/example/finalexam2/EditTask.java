package com.example.finalexam2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class EditTask extends AppCompatActivity implements View.OnTouchListener {
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private TextView _tv;
    private EditText _et;
    private String _date, _name, _type;
    private ImageView _img;
    private CheckBox _ch;
    private ArrayList<Task> _tasks;
    private int _position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_task);
        this._ch = findViewById(R.id.taskchecked);
        this._tv = findViewById(R.id.etaskdate);
        this._et = findViewById(R.id.etaskname);
        this._img = findViewById(R.id.image);

        if (getIntent().getExtras() != null) {
            Bitmap bmp = BitmapFactory.decodeByteArray(getIntent().getByteArrayExtra("image"), 0, getIntent().getByteArrayExtra("image").length);
            this._img.setImageBitmap(bmp);
            this._name = getIntent().getStringExtra("name");
            this._date = getIntent().getStringExtra("date");
            this._position = getIntent().getIntExtra("posicion", 0);
            this._et.setText(this._name);
            if (getIntent().getStringExtra("done").equals("true")) {
                this._ch.setChecked(true);
            }
            this._type = "edit";
            this._tasks = getIntent().getParcelableExtra("tascas");
        } else {
            _date = new Date().toString();
            this._ch.setEnabled(false);
            this._type = "create";
        }
        this._tv.setText(_date);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_task_menu, menu);
        return true;
    }


    public void save(MenuItem mi) {
        _name = _et.getText().toString();
        if (_name.equals("")) {
            Toast.makeText(EditTask.this, "No has insertat totes les dades", Toast.LENGTH_SHORT).show();
        } else {
            Intent resultIntent = new Intent();
            Drawable d = _img.getDrawable();

            resultIntent.putExtra("image", imgToByte(d));
            resultIntent.putExtra("name", _name);

            if (_type.equals("edit")) {
                if (this._ch.isChecked()) {
                    resultIntent.putExtra("done", "true");
                    Date date = new Date();
                    String _date = date.toString();
                } else {
                    resultIntent.putExtra("done", "false");
                }
                resultIntent.putExtra("id", getIntent().getIntExtra("id", 0));
            }
            resultIntent.putExtra("date", _date);
            setResult(RESULT_OK, resultIntent);
            finish();


        }
    }

    public void swiped(int contador) {
        switch (contador) {
            case 1:
                Toast.makeText(this, "Right", Toast.LENGTH_SHORT).show();
                if (_position < _tasks.size()) {
                    Intent i = new Intent(getBaseContext(), EditTask.class);
//                    i.putExtra("id", _tasks.get(_position++).getId());
//                    i.putExtra("image", _tasks.get(_position++).getImage());
//                    i.putExtra("name", _tasks.get(_position++).getName());
//                    i.putExtra("date", _tasks.get(_position++).getDate());
//                    i.putExtra("done", _tasks.get(_position++).getDone());
//                    i.putExtra("tascas", _tasks);
//                    i.putExtra("posicion", _position++);
//                    startActivityForResult(i, 2);
                } else {

                      Toast.makeText(this, "There is no task after this one", Toast.LENGTH_SHORT).show();
                }
                break;
            case 2:
                Toast.makeText(this, "Left", Toast.LENGTH_SHORT).show();

                if (_position < 0) {
//                    Intent i = new Intent(getBaseContext(), EditTask.class);
//                    i.putExtra("id", _tasks.get(_position--).getId());
//                    i.putExtra("image", _tasks.get(_position--).getImage());
//                    i.putExtra("name", _tasks.get(_position--).getName());
//                    i.putExtra("date", _tasks.get(_position--).getDate());
//                    i.putExtra("done", _tasks.get(_position--).getDone());
//                    i.putExtra("tascas", _tasks);
//                    i.putExtra("posicion", _position--);
//                    startActivityForResult(i, 2);
                } else {
                  Toast.makeText(this, "There is no previous task", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void close(MenuItem mi) {
        onBackPressed();
    }

    public byte[] imgToByte(Drawable draw) {
        Bitmap bitmap = ((BitmapDrawable) draw).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bitmapdata = stream.toByteArray();
        return bitmapdata;
    }

    public void cambiarImagen(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    private final GestureDetector gestureDetector;

    public EditTask(Context ctx) {
        gestureDetector = new GestureDetector(ctx, new GestureListener());
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                        result = true;
                    }
                } else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        onSwipeBottom();
                    } else {
                        onSwipeTop();
                    }
                    result = true;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }
    }

    public void onSwipeRight() {
        swiped(1);
    }

    public void onSwipeLeft() {
        swiped(2);
    }

    public void onSwipeTop() {
    }

    public void onSwipeBottom() {
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
//
//            Uri uri = data.getData();
//
//            try {
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
//                // Log.d(TAG, String.valueOf(bitmap));
//
//                ImageView imageView = findViewById(R.id.image);
//                imageView.setImageBitmap(bitmap);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//    }
}
