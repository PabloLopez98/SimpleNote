package pablo.myexample.simplenote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toolbar;

public class TypeNote extends AppCompatActivity {

    public static final String EXTRA_TITLE =
            "THE_EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION =
            "THE_EXTRA_DESCRIPTION";

    private EditText inputTitle;
    private EditText inputDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_note);

        inputTitle = findViewById(R.id.inputTitle);
        inputDescription = findViewById(R.id.inputDescription);

        final Button saveButton = findViewById(R.id.savebutton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(inputTitle.getText())) {
                    setResult(RESULT_CANCELED, replyIntent);
                } else {
                    String word = inputTitle.getText().toString();
                    String description = inputDescription.getText().toString();
                    replyIntent.putExtra(EXTRA_TITLE, word);
                    replyIntent.putExtra(EXTRA_DESCRIPTION, description);
                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }
        });
    }


    public void closeActivity(View view) {
        finish();
    }
}
