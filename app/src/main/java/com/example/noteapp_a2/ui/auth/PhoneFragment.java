package com.example.noteapp_a2.ui.auth;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.noteapp_a2.Prefs;
import com.example.noteapp_a2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneFragment extends Fragment {

    private View viewPhone;
    private View viewCode;
    private EditText editCode;
    private EditText editPhone;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private String verificationId;
    private TextView textViewTimer;
    private CountDownTimer timer;
    private TextView textViewRed;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_phone, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editPhone = view.findViewById(R.id.editPhone);
        editCode = view.findViewById(R.id.editCode);
        viewPhone = view.findViewById(R.id.viewPhone);
        viewCode = view.findViewById(R.id.viewCode);
        textViewTimer = view.findViewById(R.id.timer);
        textViewRed = view.findViewById(R.id.textViewRed);
        textViewRed.setTextColor(getResources().getColor(R.color.red));
        textViewRed.setVisibility(View.GONE);
        setListeners(view);
        setCallbacks();
        initView();
        buttonBackUp();
    }

    private void buttonBackUp() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                requireActivity().finish();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(),callback);

    }

    private void setListeners(View view) {
        view.findViewById(R.id.btnNext).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            requestSms();
        }
    });
        view.findViewById(R.id.btnSignIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirm();
            }
        });
        requireActivity().getOnBackPressedDispatcher().addCallback(
                getViewLifecycleOwner(),
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        requireActivity().finish();
                    }
                }
        );
    }

    private void confirm() {
        String code = editCode.getText().toString();
        if (code.length() == 6 && TextUtils.isDigitsOnly(code)){
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            signIn(credential);
        }
    }

    private void initView() {
        viewCode.setVisibility(View.GONE);
    }

    private void showViewCode() {
        viewCode.setVisibility(View.VISIBLE);
        viewPhone.setVisibility(View.GONE);
        starTimer();
    }

    private void starTimer() {
        timer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String text = String.valueOf(millisUntilFinished/1000);
                textViewTimer.setText("00:" + text);
                if (text.length() < 2) textViewTimer.setText("00:0" + text);
            }

            @Override
            public void onFinish() {
                showViewPhone();
                textViewRed.setVisibility(View.VISIBLE);
            }
        };
        timer.start();
    }

    private void showViewPhone() {
        viewPhone.setVisibility(View.VISIBLE);
        viewCode.setVisibility(View.GONE);
    }

    private void setCallbacks() {
        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                Log.e("ololo", "onVerificationCompleted");
                signIn(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.e("ololo", "onVerificationFailed" + e.getMessage());
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationId = s;
                showViewCode();
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                super.onCodeAutoRetrievalTimeOut(s);
            }
        };
    }

    private void signIn(PhoneAuthCredential phoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    close();
                }else {
                    Toast.makeText(requireContext(), "Error" + task.getException(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void requestSms() {
        String phone = editPhone.getText().toString();
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                        .setPhoneNumber(phone)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(requireActivity())                 // Activity (for callback binding)
                        .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    private void close() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        navController.navigateUp();
    }
}