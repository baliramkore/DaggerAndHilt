package com.rbk.daggerandhilt;

import com.google.gson.annotations.SerializedName;

import java.util.List;

 class QuestionsListResponseSchema {
    @SerializedName("items")
    private final List<Question> mQuestions;

    public QuestionsListResponseSchema(List<Question> questions){
        mQuestions = questions;
    }

    public List<Question> getQuestions() {
        return mQuestions;
    }
}