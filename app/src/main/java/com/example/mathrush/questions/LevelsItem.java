package com.example.mathrush.questions;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class LevelsItem implements Parcelable {

	@SerializedName("difficulty")
	private String difficulty;

	@SerializedName("questions")
	private List<QuestionsItem> questions;

	@SerializedName("id")
	private String id;

	public LevelsItem() {
	}

	protected LevelsItem(Parcel in) {
		difficulty = in.readString();
		questions = in.createTypedArrayList(QuestionsItem.CREATOR);
		id = in.readString();
	}

	public static final Creator<LevelsItem> CREATOR = new Creator<LevelsItem>() {
		@Override
		public LevelsItem createFromParcel(Parcel in) {
			return new LevelsItem(in);
		}

		@Override
		public LevelsItem[] newArray(int size) {
			return new LevelsItem[size];
		}
	};

	public String getDifficulty() {
		return difficulty;
	}

	public List<QuestionsItem> getQuestions() {
		return questions;
	}

	public String getId() {
		return id;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(difficulty);
		dest.writeTypedList(questions);
		dest.writeString(id);
	}

	@Override
	public int describeContents() {
		return 0;
	}
}
