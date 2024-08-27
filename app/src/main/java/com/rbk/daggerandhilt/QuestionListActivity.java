package com.rbk.daggerandhilt;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class QuestionListActivity extends AppCompatActivity implements Callback<QuestionsListResponseSchema>
{

    private RecyclerView mRecyclerView;
    private QuestionsAdapter mQuestionAdapter;
    private StackOverFLowAPI mStackOverFLowAPI;
    private Call<QuestionsListResponseSchema> mSchemaCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_question_list);

        mRecyclerView=findViewById(R.id.recyler);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mQuestionAdapter=new QuestionsAdapter(new OnQuestionClickListener() {
            @Override
            public void onQuestionClicked(Question question) {

                //sending id to the details Activity
                QuestionDetailsActivity.start(QuestionListActivity.this,question.getmId());

            }
        });
        mRecyclerView.setAdapter(mQuestionAdapter);



        //Initiate Retrofit
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(Constants.BaseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mStackOverFLowAPI=retrofit.create(StackOverFLowAPI.class);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mSchemaCall=mStackOverFLowAPI.lastActiveQuestion(20);
        mSchemaCall.enqueue(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mSchemaCall!=null){
            mSchemaCall.cancel();
        }
    }

    @Override
    public void onResponse(Call<QuestionsListResponseSchema> call, Response<QuestionsListResponseSchema> response) {

        QuestionsListResponseSchema responseSchema;
        if ( response.isSuccessful() && (responseSchema = response.body()) != null){
            mQuestionAdapter.bindData(responseSchema.getQuestions());
        }else{

         onFailure(call,null);
        }

    }

    @Override
    public void onFailure(Call<QuestionsListResponseSchema> call, Throwable t) {

        QuestionsListResponseSchema responseSchema;
        FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction().add(ServerErrorDialogFragment.newInstance(),null)
                .commitAllowingStateLoss();


    }

    //***************RecyclerView Adapter **************
    interface OnQuestionClickListener {

        void onQuestionClicked(Question question);
    }

    public static class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.QuestionsViewHolder> {

        private OnQuestionClickListener mOnQuestionClickListener;
        private List<Question> mQuestionList = new ArrayList<>(0);



        private static class QuestionsViewHolder extends RecyclerView.ViewHolder {

            public TextView mTitle;

            public QuestionsViewHolder(@NonNull View itemView) {
                super(itemView);
                this.mTitle = itemView.findViewById(R.id.txt_title);
            }
        }

        public QuestionsAdapter(OnQuestionClickListener questionClickListener) {

            mOnQuestionClickListener = questionClickListener;
        }

        public void bindData(List<Question> questionList){
            mQuestionList=new ArrayList<>(questionList);
            notifyDataSetChanged();
        }
        @NonNull
        @Override
        public QuestionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_question_list_item,parent,false);


            return new QuestionsViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull QuestionsViewHolder holder, @SuppressLint("RecyclerView") int position) {

            holder.mTitle.setText(mQuestionList.get(position).getmTitle());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mOnQuestionClickListener.onQuestionClicked(mQuestionList.get(position));
                }
            });
        }

        @Override
        public int getItemCount() {
            return mQuestionList.size();
        }

    }
}

