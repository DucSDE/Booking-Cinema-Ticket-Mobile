package com.dream.dreamtheather.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dream.dreamtheather.Model.InputValidatorHelper;
import com.dream.dreamtheather.Model.Users;
import com.dream.dreamtheather.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.joda.time.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Register extends AppCompatActivity {

    private ImageButton btnEyeShow, btnDatePicker;
    private EditText edtAuthor_Name, edtRegisterEmail, edtRegisterPassword, edtRegisterPasswordConfirm, edtPhoneNum;
    private Button btnConfirm;
    private TextView tvDatepicked, tvAge;
    private DatePickerDialog.OnDateSetListener dateSetListener1;
    private boolean pickdate = false;
    private ProgressBar progressBar;
    private RadioGroup radioGroup;
    private RadioButton radioButton;

    // Fire cloud
    public FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    //validation checker
    InputValidatorHelper inputValidatorHelper = new InputValidatorHelper();

    //
    private String dateOfBirth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //textview
        tvAge = findViewById(R.id.tvAge);
        tvDatepicked = findViewById(R.id.tvDatepicked);

        //button + img button
        btnDatePicker = findViewById(R.id.btnDatePicker);
        btnEyeShow = findViewById(R.id.btnEyeShow);
        btnConfirm = findViewById(R.id.btnConfirm);

        //radio group
        radioGroup = findViewById(R.id.radioGroup);

        //editText
        edtAuthor_Name = findViewById(R.id.edtAuthor_Name);
        edtRegisterEmail = findViewById(R.id.edtRegisterEmail);
        edtRegisterPassword = findViewById(R.id.edtRegisterPassword);
        edtRegisterPasswordConfirm = findViewById(R.id.edtRegisterPasswordConfirm);
        edtPhoneNum = findViewById(R.id.edtPhoneNum);

        //Progress Bar
        progressBar = findViewById(R.id.progressBar);

        //fire cloud
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        btnEyeShow.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        edtRegisterPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        return true;
                    case MotionEvent.ACTION_UP:
                        edtRegisterPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        return true;
                }
                return false;
            }
        });

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(Register.this, android.R.style.Theme_Holo_Dialog_MinWidth,
                        dateSetListener1, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        //current days
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        tvDatepicked.setText(String.valueOf(simpleDateFormat.format(Calendar.getInstance().getTime())));
    }

    @Override
    protected void onStart() {
        super.onStart();

        calAge();

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkValidate()) {
                    Toast.makeText(getApplicationContext(), "Vui l??ng ??i???n ????? th??ng tin", Toast.LENGTH_SHORT).show();
                } else {
                    if (checkPassConfirm()) {
                        if (emailValidate()) {
                            if (checkPhoneNum()) {
                                if (pickdate) {

                                    signUp();

                                } else
                                    Toast.makeText(getApplicationContext(), "Vui l??ng ch???n ng??y sinh", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    public void calAge() {
        //date of birth
        dateSetListener1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                dateOfBirth = day + "/" + month + "/" + year;
                tvDatepicked.setText(dateOfBirth);

                //cal Age
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    //parse th??nh ng??y
                    Date birth = simpleDateFormat.parse(dateOfBirth);
                    Date currentDay = simpleDateFormat.parse(String.valueOf(simpleDateFormat.format(Calendar.getInstance().getTime())));

                    //parse sang s???
                    long startDate = birth.getTime();
                    long endDate = currentDay.getTime();

                    if (startDate <= endDate) {
                        Period period = new Period(startDate, endDate, PeriodType.yearMonthDay());
                        int years = period.getYears();
                        int months = period.getMonths();
                        int days = period.getDays();
                        tvAge.setText("Tu???i: ");
                        tvDatepicked.setText(years + " tu???i " + months + " th??ng " + days + " ng??y");
                        pickdate = true;
                    } else {
                        Toast.makeText(getApplicationContext(), "Ng??y sinh ph???i tr?????c ng??y hi???n t???i!", Toast.LENGTH_LONG).show();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public void addNewUser() {
        Users user = new Users();
        firebaseUser = firebaseAuth.getCurrentUser();

        //get all values
        String fullName = edtAuthor_Name.getText().toString().trim();
        String email = edtRegisterEmail.getText().toString().trim();
        String phoneNum = edtPhoneNum.getText().toString().trim();

        int selectedId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(selectedId);
        String gender = radioButton.getText().toString();
        Log.i("gender: ", gender);

        ArrayList<Integer> idTicket = new ArrayList<>();

        user.setFullName(fullName);
        user.setAddress("ch??a c??");
        user.setBalance(0);
        user.setBirthDay(dateOfBirth);
        user.setAvaUrl("ch??a c??");
        user.setEmail(email);
        user.setPhoneNumber(phoneNum);
        user.setUserType("Kh??ch");
        user.setGender(gender);
        user.setIdTicket(idTicket);
        user.setLoyaltyPoint(0);

        createFireStore(user);

    }

    public void createFireStore(Users user) {
        //add to firestore
        firebaseFirestore
            .collection("user_info")
            .document(firebaseUser.getUid())
            .set(user)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(Register.this, "Th??nh c??ng", Toast.LENGTH_SHORT).show();
                    Log.d("test", "id:" + firebaseUser.getUid());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("TAG", "Error adding document" + e.getMessage());
                }
            }
        );
    }

    public void signUp() {

        firebaseAuth
                .createUserWithEmailAndPassword(
                        edtRegisterEmail.getText().toString().trim(),
                        edtRegisterPassword.getText().toString().trim())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            addNewUser();

                            Log.d("TAG", "th??nh c??ng");
                            //send noti to email
                            firebaseUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(Register.this, "???? g???i email x??c nh???n", Toast.LENGTH_LONG).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("Th???t b???i:", "Kh??ng g???i ???????c email" + e.getMessage());
                                }
                            });

                            SuccessfulRegister();
                        } else {
                            Toast.makeText(Register.this, "C?? g?? ???? kh??ng ???n", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.VISIBLE);
                        }
                    }
                })
                ;
    }

    public void BackToLogin(View view) {
        startActivity(new Intent(Register.this, LoginActivity.class));
        finish();
    }

    public boolean checkValidate() {
        if (edtAuthor_Name.getText().toString().isEmpty() ||
                edtRegisterPassword.getText().toString().isEmpty() ||
                edtRegisterEmail.getText().toString().isEmpty() ||
                edtRegisterPasswordConfirm.getText().toString().isEmpty() ||
                edtAuthor_Name.getText().toString().isEmpty() ||
                edtPhoneNum.getText().toString().isEmpty()) {
            return true;
        }
        return false;
    }

    public boolean emailValidate() {
        if (inputValidatorHelper.isValidEmail(edtRegisterEmail.getText().toString())) {
            return true;
        } else {
            edtRegisterEmail.setError("Nh???p sai ?????nh d???ng email");
            edtRegisterEmail.requestFocus();
            return false;
        }
    }

    public boolean checkPassConfirm() {
        if (edtRegisterPassword.getText().length() < 6) {
            edtRegisterPasswordConfirm.setError("M???t kh???u kh??ng ???????c d?????i 6 k?? t???");
            edtRegisterPasswordConfirm.requestFocus();
            return false;
        } else if (edtRegisterPassword.getText().toString().equals(edtRegisterPasswordConfirm.getText().toString())) {
            return true;
        } else {
            edtRegisterPasswordConfirm.setError("M???t kh???u kh??ng tr??ng nhau");
            edtRegisterPasswordConfirm.requestFocus();
            return false;
        }
    }

    public boolean checkPhoneNum() {
        if ((edtPhoneNum.getText().length() >= 8 && edtPhoneNum.getText().length() <= 10)) {
            return true;
        } else {
            edtPhoneNum.setError("S??T ph???i t??? 8 ?????n 10 s???!");
            edtPhoneNum.requestFocus();
            return false;
        }
    }

    public void SuccessfulRegister() {
        Toast.makeText(Register.this, "T???o t??i kho???n th??nh c??ng", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Register.this, UserProfile.class);
        startActivity(intent);
        finish();
    }

}