package com.rbk.daggerandhilt;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class QuestionDetailsActivity extends AppCompatActivity implements Callback<SingleQuestionResponseSchema> {

    public static final String EXTRA_QUESTION_ID="EXTRA_QUESTION_ID";
    private StackOverFLowAPI stackOverFLowAPI;
    private TextView mTxtQuestionBody;
    private String mQuestionId;
    private Call<SingleQuestionResponseSchema> mCall;


    public static void start(Context context, String questionId){

        Intent intent=new Intent(context,QuestionDetailsActivity.class);
        intent.putExtra(EXTRA_QUESTION_ID,questionId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_details);

        mTxtQuestionBody=findViewById(R.id.txtQuestionBody);
        Retrofit retrofit=new Retrofit.Builder().baseUrl(Constants.BaseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        stackOverFLowAPI=retrofit.create(StackOverFLowAPI.class);
        mQuestionId=getIntent().getExtras().getString(EXTRA_QUESTION_ID);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mCall=stackOverFLowAPI.questionDetails(mQuestionId);
        mCall.enqueue(this);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mCall!=null){
            mCall.cancel();
        }
    }

    @Override
    public void onResponse(Call<SingleQuestionResponseSchema> call, Response<SingleQuestionResponseSchema> response) {
        SingleQuestionResponseSchema questionResponseSchema;
        if (response.isSuccessful() && (questionResponseSchema=response.body())!=null){

            String questionBody=questionResponseSchema.getQuestions().getmBody();
            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){

                mTxtQuestionBody.setText(Html.fromHtml(questionBody,Html.FROM_HTML_MODE_LEGACY));

            }else {
                mTxtQuestionBody.setText(Html.fromHtml(questionBody));
            }
        }else {
            onFailure(call,null);
        }
    }

    @Override
    public void onFailure(Call<SingleQuestionResponseSchema> call, Throwable t) {

        FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(ServerErrorDialogFragment.newInstance(),null)
                .commitAllowingStateLoss();
    }
}