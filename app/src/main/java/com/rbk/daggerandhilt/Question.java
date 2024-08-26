package com.rbk.daggerandhilt;

import com.google.gson.annotations.SerializedName;

import kotlin.jvm.internal.SerializedIr;

public class Question {

    @SerializedName("title")
    private final String mTitle;

    @SerializedName("question_id")
    private final String mId;

    public Question(String mTitle, String mId) {
        this.mTitle = mTitle;
        this.mId = mId;
    }

    public String getmId() {
        return mId;
    }

    public String getmTitle() {
        return mTitle;
    }
}
