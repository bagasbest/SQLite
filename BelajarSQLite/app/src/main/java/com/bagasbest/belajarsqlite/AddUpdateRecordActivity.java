package com.bagasbest.belajarsqlite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.net.URI;
import java.text.DateFormat;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddUpdateRecordActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener {

    private EditText nameEt, phoneEt, descriptionEt;
    private Spinner workSp;
    private TextView dobTv;
    private Button submitBtn;
    private Spinner spinner;
    private CircleImageView profileIv;
    private ImageView addProfileTv;
    String text;

    //permission constant
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 101;
    private static final int IMAGE_PICK_CAMERA_CODE = 102;
    private static final int IMAGE_PICK_GALLERY_CODE = 103;


    //ARRAY OF PERMISSION
    private String[] cameraPermission;
    private String[] storagePermission;

    //uri
    URI imageUri;

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update_record);

        setTitle("Add Record");

        //casting view
        nameEt = findViewById(R.id.nameEt);
        phoneEt = findViewById(R.id.phoneEt);
        descriptionEt = findViewById(R.id.descriptionEt);
        workSp = findViewById(R.id.workSp);
        dobTv = findViewById(R.id.dobTv);
        submitBtn = findViewById(R.id.submitBtn);
        profileIv = findViewById(R.id.profileTv);
        addProfileTv = findViewById(R.id.addProfileTv);

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


        nameEt.addTextChangedListener(submitTextWtcher);
        phoneEt.addTextChangedListener(submitTextWtcher);
        descriptionEt.addTextChangedListener(submitTextWtcher);

        addProfileTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private TextWatcher submitTextWtcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String nameInput = nameEt.getText().toString().trim();
            String phoneInput = phoneEt.getText().toString().trim();
            String descInput = descriptionEt.getText().toString().trim();

            submitBtn.setEnabled(!nameInput.isEmpty() && !phoneInput.isEmpty() && !descInput.isEmpty());

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

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
}
