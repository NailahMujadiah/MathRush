package com.example.mathrush.questions;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.mathrush.ApiConfig;
import com.google.gson.annotations.SerializedName;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class QuestionsItem implements Parcelable {

	@SerializedName("topicId")
	private String topicId;

	@SerializedName("icon")
	private String icon;

	@SerializedName("title")
	private String title;

	@SerializedName("levels")
	private List<LevelsItem> levels;

	@SerializedName("apiExpression")
	private String apiExpression;

	@SerializedName("id")
	private String id;

	@SerializedName("displayQuestion")
	private String displayQuestion;

	@SerializedName("wrongChoices")
	private List<String> wrongChoices;

	public QuestionsItem() {
	}

	protected QuestionsItem(Parcel in) {
		topicId = in.readString();
		icon = in.readString();
		title = in.readString();
		levels = in.createTypedArrayList(LevelsItem.CREATOR);
		apiExpression = in.readString();
		id = in.readString();
		displayQuestion = in.readString();
		wrongChoices = in.createStringArrayList();
	}

	public static final Creator<QuestionsItem> CREATOR = new Creator<QuestionsItem>() {
		@Override
		public QuestionsItem createFromParcel(Parcel in) {
			return new QuestionsItem(in);
		}

		@Override
		public QuestionsItem[] newArray(int size) {
			return new QuestionsItem[size];
		}
	};

	public String getTopicId() {
		return topicId;
	}

	public String getIcon() {
		return icon;
	}

	public String getTitle() {
		return title;
	}

	public List<LevelsItem> getLevels() {
		return levels;
	}

	public String getApiExpression() {
		return apiExpression;
	}

	public String getId() {
		return id;
	}

	public String getDisplayQuestion() {
		return displayQuestion;
	}

	public List<String> getWrongChoices() {
		return wrongChoices;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(topicId);
		dest.writeString(icon);
		dest.writeString(title);
		dest.writeTypedList(levels);
		dest.writeString(apiExpression);
		dest.writeString(id);
		dest.writeString(displayQuestion);
		dest.writeStringList(wrongChoices);
	}

	public Call<ResponseBody> getCorrectAnswerCall() {
		try {
			String encoded = URLEncoder.encode(apiExpression, "UTF-8");
			String url = "https://api.mathjs.org/v4/?expr=" + encoded;
			return ApiConfig.getInstance().getMathApi().getAnswer(url);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
}
