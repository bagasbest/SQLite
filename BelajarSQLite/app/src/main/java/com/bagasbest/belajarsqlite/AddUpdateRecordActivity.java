package com.bagasbest.belajarsqlite;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.net.URI;
import java.text.DateFormat;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.basgeekball.awesomevalidation.ValidationStyle.COLORATION;

public class AddUpdateRecordActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener {

    private EditText nameEt, noEt, descriptionEt;
    private Spinner workSp;
    private TextView dobTv;
    private Button submitBtn;
    private Spinner spinner;
    private CircleImageView profileIv;
    private ImageView addProfileTv;
    private String text, name, noTelp, job, dob, bio;

    private MyDbHelper dbHelper;

    //permission constant
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 101;
    private static final int IMAGE_PICK_CAMERA_CODE = 102;
    private static final int IMAGE_PICK_GALLERY_CODE = 103;


    //ARRAY OF PERMISSION
    private String[] cameraPermission;
    private String[] storagePermission;

    //uri
    private Uri imageUri;

    private AwesomeValidation awesomeValidation;

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update_record);

        setTitle("Add Record");

        //casting view
        nameEt = findViewById(R.id.nameEt);
        noEt = findViewById(R.id.no_telp);
        descriptionEt = findViewById(R.id.descriptionEt);
        workSp = findViewById(R.id.workSp);
        dobTv = findViewById(R.id.dobTv);
        submitBtn = findViewById(R.id.submitBtn);
        profileIv = findViewById(R.id.profileTv);
        addProfileTv = findViewById(R.id.addProfileTv);

        //init db helper
        dbHelper = new MyDbHelper(this);

        //init permission
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};




        dobTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //choose date of birth
                //show calendar
                    DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        spinner = findViewById(R.id.workSp);
        ArrayAdapter <CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.work_job, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        addProfileTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePickDialog();
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputData();
            }
        });
    }

    private void inputData() {

        awesomeValidation = new AwesomeValidation(COLORATION);
        awesomeValidation.setColor(R.color.colorButton);

        //validation name
        awesomeValidation.addValidation(this, R.id.nameEt, RegexTemplate.NOT_EMPTY, R.string.invalid_mame);
        awesomeValidation.addValidation(this, R.id.descriptionEt, RegexTemplate.NOT_EMPTY, R.string.invalid_desc);
        awesomeValidation.addValidation(this, R.id.no_telp, RegexTemplate.NOT_EMPTY, R.string.invalid_phone);


        if(awesomeValidation.validate()){
            name = ""+nameEt.getText().toString().trim();
            noTelp = ""+noEt.getText().toString().trim();
            job = ""+workSp.getSelectedItem().toString();
            dob = ""+dobTv.getText().toString().trim();
            bio = ""+descriptionEt.getText().toString().trim();


            //save to db
            String timestamp = ""+System.currentTimeMillis();
            long id = dbHelper.insertRecord(
                    ""+name,
                    ""+imageUri,
                    ""+bio,
                    ""+noTelp,
                    ""+job,
                    ""+dob,
                    ""+timestamp,
                    ""+timestamp
            );


            Toast.makeText(this,  id  + " Updated...", Toast.LENGTH_SHORT).show();

        }


    }

    private void imagePickDialog() {
        //show option choose
        String[] options = {"Camera", "Gallery"};
        //alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Image From");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //handle click
                if(which == 0) {
                    //camera choose
                    if(!checkCameraPermission()) {
                        requestCameraPermission();
                    } else {
                        pickFromCamera();
                    }
                } else if(which == 1){
                    //gallery choose
                    if(!checkStoragePermission()) {
                        requestStoragePermission();
                    } else {
                        pickFromGallery();
                    }
                }
            }
        });

        //create dialog
        builder.create().show();

    }

    private void pickFromCamera() {
        //intent to pick image from camera
        ContentValues cv = new ContentValues();
        cv.put(MediaStore.Images.Media.TITLE, "image title");
        cv.put(MediaStore.Images.Media.DESCRIPTION, "image description");

        //put image uri
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);

        //intent to open camera for image
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);


    }

    private void pickFromGallery() {
        //intent to pick image from gallery
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);
    }

    private boolean checkStoragePermission () {
        //check if storage permission enable or not
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return  result;
    }

    private void requestStoragePermission () {
        //req the storage permission
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission () {
        //check if  camera & storage permission enable or not
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == (PackageManager.PERMISSION_GRANTED);

        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);

        return result && result1;
    }

    private void requestCameraPermission () {
        ActivityCompat.requestPermissions(this, cameraPermission, CAMERA_REQUEST_CODE);
    }



    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        String currentDate = DateFormat.getDateInstance().format(calendar.getTime());
        dobTv.setText(currentDate);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        text = parent.getItemAtPosition(position).toString().trim();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //result of permission allowed/not
        switch (requestCode) {
            case CAMERA_REQUEST_CODE : {

                if(grantResults.length>0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] ==PackageManager.PERMISSION_GRANTED;

                    if(cameraAccepted && storageAccepted) {
                        pickFromCamera();
                    } else {
                        Toast.makeText(this, "Camera & Storage permission must enabled to upload image", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;

            case STORAGE_REQUEST_CODE : {

                if(grantResults.length>0) {
                    boolean storageAccepted = grantResults[0] ==PackageManager.PERMISSION_GRANTED;

                    if(storageAccepted) {
                        pickFromGallery();
                    } else {
                        Toast.makeText(this, "Storage permission must enabled to upload image", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //image picked from camera


        if(resultCode == RESULT_OK){
            //IMAGE IS PICKING
            if(requestCode ==IMAGE_PICK_GALLERY_CODE){
                //PICKED FROM GALLEY

                //crop image
                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(this);

            } else if(requestCode == IMAGE_PICK_CAMERA_CODE){
                //pick from camera

                //crop image
                CropImage.activity(imageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(this);
            } else if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if(resultCode == RESULT_OK){
                    Uri resultUri = result.getUri();
                    imageUri = resultUri;
                    //set image
                    profileIv.setImageURI(resultUri);
                }

                else  if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    //ERROR
                    Exception error = result.getError();
                    Toast.makeText(AddUpdateRecordActivity.this, ""+error, Toast.LENGTH_SHORT).show();
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
