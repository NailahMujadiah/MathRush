package com.example.mathrush.questions;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Questions implements Parcelable {

	@SerializedName("Questions")
	private List<QuestionsItem> questions;

	public Questions() {
	}

	protected Questions(Parcel in) {
		questions = in.createTypedArrayList(QuestionsItem.CREATOR);
	}

	public static final Creator<Questions> CREATOR = new Creator<Questions>() {
		@Override
		public Questions createFromParcel(Parcel in) {
			return new Questions(in);
		}

		@Override
		public Questions[] newArray(int size) {
			return new Questions[size];
		}
	};

	public List<QuestionsItem> getQuestions() {
		return questions;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeTypedList(questions);
	}
}
