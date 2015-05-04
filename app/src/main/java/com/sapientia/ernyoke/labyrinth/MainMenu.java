package com.sapientia.ernyoke.labyrinth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;


public class MainMenu extends Activity implements View.OnClickListener{

    private Button mediumBtn;
    private Button easyBtn;
    private Button hardBtn;
    private ProgressBar rolling;
    public enum DIFFICULTY{EASY, MEDIUM, HARD, NET};
    private DIFFICULTY currentDiff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_menu);

        easyBtn = (Button) findViewById(R.id.easyBtn);
        mediumBtn = (Button) findViewById(R.id.mediumBtn);
        hardBtn = (Button) findViewById(R.id.hardBtn);

        easyBtn.setOnClickListener(this);
        mediumBtn.setOnClickListener(this);
        hardBtn.setOnClickListener(this);

        rolling = (ProgressBar) findViewById(R.id.rolling);
        rolling.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View view) {
        final Intent intent = new Intent(view.getContext(), LabyrinthActivity.class);
        final Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.easyBtn: {
                bundle.putSerializable(Constants.DIFF_ID, DIFFICULTY.EASY);
                currentDiff = DIFFICULTY.EASY;
                intent.putExtras(bundle);
                setUpPlayground(intent);
                break;
            }
            case R.id.mediumBtn: {
                bundle.putSerializable(Constants.DIFF_ID, DIFFICULTY.MEDIUM);
                currentDiff = DIFFICULTY.MEDIUM;
                intent.putExtras(bundle);
                setUpPlayground(intent);
                break;
            }
            case R.id.hardBtn: {
                bundle.putSerializable(Constants.DIFF_ID, DIFFICULTY.HARD);
                currentDiff = DIFFICULTY.HARD;
                intent.putExtras(bundle);
                setUpPlayground(intent);
                break;
            }


        }
    }

    private void setUpPlayground(Intent intent) {
        this.startActivityForResult(intent, Constants.REQ_CODE);
    }

    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data) {
        Intent intent = new Intent(this, LabyrinthActivity.class);
        Bundle bundle = new Bundle();
        if(requestCode == Constants.REQ_CODE) {
            if(resultCode == Constants.NEXT_DIFF) {
                switch (currentDiff) {
                    case EASY: {
                        currentDiff = DIFFICULTY.MEDIUM;
                        break;
                    }
                    case MEDIUM: {
                        currentDiff = DIFFICULTY.HARD;
                        break;
                    }
                    case HARD: {
                        currentDiff = DIFFICULTY.EASY;
                        break;
                    }
                }
                bundle.putSerializable(Constants.DIFF_ID,currentDiff);
                intent.putExtras(bundle);
                setUpPlayground(intent);
            }


        }
    }

}
