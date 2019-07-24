package pablo.myexample.simplenote;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.Locale;

public class TypeNote extends AppCompatActivity {
    public static final String EXTRA_ID = "THE_EXTRA_ID";
    public static final String EXTRA_TITLE = "THE_EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "THE_EXTRA_DESCRIPTION";
    public static final String EXTRA_NOTE_TITLE = "THE_EXTRA_NOTE_TITLE";

    private EditText inputTitle;
    private EditText inputDescription;
    private String sameTitle;
    private Intent replyIntent;
    private Intent intent;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_note);
        title = findViewById(R.id.toolbartwo);
        replyIntent = new Intent();
        inputTitle = findViewById(R.id.inputTitle);
        inputDescription = findViewById(R.id.inputDescription);

        final Button saveButton = findViewById(R.id.savebutton);

        intent = getIntent();

        if (intent.hasExtra(EXTRA_ID)) {
            title.setText("Update Note");
            sameTitle = intent.getStringExtra(EXTRA_TITLE);
            inputTitle.setText(intent.getStringExtra(EXTRA_NOTE_TITLE));
            inputDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //replyIntent = new Intent();
                if (TextUtils.isEmpty(inputTitle.getText()) || TextUtils.isEmpty(inputDescription.getText())) { //uh-oh there is a missing input field
                    Toast.makeText(getApplicationContext(), "Missing input", Toast.LENGTH_LONG).show();
                } else { //good to go and get saved
                    String word = inputTitle.getText().toString();
                    String description = inputDescription.getText().toString();

                    if (intent.hasExtra(EXTRA_ID)) {
                        replyIntent.putExtra(EXTRA_TITLE, sameTitle);
                    } else {
                        replyIntent.putExtra(EXTRA_TITLE, word);
                    }
                    replyIntent.putExtra(EXTRA_NOTE_TITLE, word);
                    replyIntent.putExtra(EXTRA_DESCRIPTION, description);
                    setResult(RESULT_OK, replyIntent);
                    finish();
                }
            }
        });

        final SpeechRecognizer mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        final Intent mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {
            }

            @Override
            public void onBeginningOfSpeech() {
            }

            @Override
            public void onRmsChanged(float v) {
            }

            @Override
            public void onBufferReceived(byte[] bytes) {
            }

            @Override
            public void onEndOfSpeech() {
            }

            @Override
            public void onError(int i) {
            }

            @Override
            public void onResults(Bundle bundle) {
                //getting all the matches
                ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                //displaying the first match
                if (matches != null) inputDescription.setText(matches.get(0));
            }

            @Override
            public void onPartialResults(Bundle bundle) {
            }

            @Override
            public void onEvent(int i, Bundle bundle) {
            }
        });

        findViewById(R.id.micbutton).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP:
                        mSpeechRecognizer.stopListening();
                        inputDescription.setHint("Description");
                        break;
                    case MotionEvent.ACTION_DOWN:
                        mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                        inputDescription.setText("");
                        inputDescription.setHint("...Recording...");
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        alertInfo();
    }

    private void alertInfo() {
        AlertDialog alertDialog = new AlertDialog.Builder(TypeNote.this).create();
        alertDialog.setTitle("Careful!");
        if(intent.hasExtra(EXTRA_ID)) {
            alertDialog.setMessage("Discard changes?");
        }else{
            alertDialog.setMessage("Do you want to delete note?");
        }
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setResult(RESULT_CANCELED,replyIntent);
                finish();
            }
        });
        alertDialog.show();
    }


    public void closeActivity(View view) {
        finish();
    }
}
