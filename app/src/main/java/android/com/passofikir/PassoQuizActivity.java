package android.com.passofikir;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;

import at.grabner.circleprogress.AnimationState;
import at.grabner.circleprogress.AnimationStateChangedListener;
import at.grabner.circleprogress.CircleProgressView;
import at.grabner.circleprogress.TextMode;

public class PassoQuizActivity extends AppCompatActivity {
    CircleProgressView circleProgressView;
    private final int smsRetryDelaySeconds = 10;
    private final int smsRetryDelayMillis = smsRetryDelaySeconds * 1000;
    private final int smsRetryDelayShiftMillis = 1000;
    boolean firstIdle = false;
    Button btn1, btn2, btn3, btn4;
    private boolean firstTry = true;
    AlertDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        btn1 = (Button)findViewById(R.id.button);
        btn2 = (Button)findViewById(R.id.button2);
        btn3 = (Button)findViewById(R.id.button3);
        btn4 = (Button)findViewById(R.id.button4);


        final AlertDialog.Builder dialog = new AlertDialog.Builder(PassoQuizActivity.this);
        dialog.setPositiveButton(getString(android.R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                /*SharedPreferences preferences = getSharedPreferences("login_preferences", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();
                Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);*/
                finish();
            }
        });
        alertDialog = dialog.create();
        /*alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                Button yesButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                Button noButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                yesButton.setTextSize(16f);
                yesButton.setTypeface(tf_medium);
                yesButton.setTextColor(getResources().getColor(R.color.green));
                noButton.setTextSize(16f);
                noButton.setTypeface(tf_regular);
                noButton.setTextColor(getResources().getColor(R.color.green));

            }

        });*/
        /*logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.show();
            }
        });*/


        circleProgressView = (CircleProgressView) findViewById(R.id.verification_code_circleView);
        circleProgressView.setMaxValue(smsRetryDelaySeconds);
        circleProgressView.setTextMode(TextMode.VALUE);
        circleProgressView.setValueInterpolator(new LinearInterpolator());
        circleProgressView.setOnAnimationStateChangedListener(new AnimationStateChangedListener() {
            @Override
            public void onAnimationStateChanged(AnimationState animationState) {
                switch (animationState) {
                    case IDLE: {
                        circleProgressView.setVisibility(View.INVISIBLE);
                        if(firstIdle){
                            alertDialog.setTitle("Süre doldu!");
                            alertDialog.setMessage("Verilen süre içinde cevaplayamadığınız için puan kazanamadınız..");
                            alertDialog.show();
                        }
                        firstIdle=true;

                        break;
                    }
                    case ANIMATING: {
                        circleProgressView.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        circleProgressView.setValueAnimated(smsRetryDelaySeconds, 0, smsRetryDelayMillis + smsRetryDelayShiftMillis);


    }
    public void onClick(View view){
        switch (view.getId()){
            case R.id.button:
                btn1.setBackground(getDrawable(R.drawable.button_rounded_red));
                firstTry = false;
                break;
            case R.id.button2:
                btn2.setBackground(getDrawable(R.drawable.button_rounded_red));
                firstTry = false;
                break;
            case R.id.button3:
                btn3.setBackground(getDrawable(R.drawable.button_rounded_green));
                circleProgressView.stopSpinning();
                circleProgressView.setVisibility(View.GONE);
                firstIdle = false;
                if(firstTry){
                    alertDialog.setTitle("Tebrikler!");
                    alertDialog.setMessage("Soruyu doğru yanıtladığınız için 10 puan kazandınız");
                    alertDialog.show();
                    btn1.setClickable(false);
                    btn2.setClickable(false);
                    btn4.setClickable(false);
                } else {
                    alertDialog.setTitle("Üzgünüz..");
                    alertDialog.setMessage("Soruyu tek seferde bilemediğiniz için puan kazanamadınız..");
                    alertDialog.show();
                }
                break;
            case R.id.button4:
                btn4.setBackground(getDrawable(R.drawable.button_rounded_red));
                firstTry = false;
                break;
        }
    }
}
