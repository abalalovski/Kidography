package com.twodwarfs.kidography.question;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.twodwarfs.kidography.GameType.QuestionsType;

public abstract class Question {
	
	public static class Fields {
		public static final String RIDDLES_TABLE_NAME = "riddles";
		public static final String CAPITALS_TABLE_NAME = "capitals";
		public static final String LANDMARKS_TABLE_NAME = "landmarks";
		public static final String FLAGS_TABLE_NAME = "flags";
		
		public static final String TEXT = "text";
		public static final String ANSWERS = "answers";
		public static final String CORRECT = "correct";
		public static final String DESC = "description";
		public static final String[] ALL_FIELDS = { TEXT, ANSWERS, CORRECT };
	}
	
	private String mText;
	private List<String> mAnswers;
	private int mCorrectAnswer;
	private String mCorrectAnswerString;
	private QuestionsType mType;
	
	public void setText(String text) {
		mText = text;
	}
	
	public String getText() {
		return mText;
	}
	
	public List<String> getAnswers() {
		return mAnswers;
	}
	
//	public String getFirstAnswer() {
//		return mAnswers.get(0);
//	}
//	
//	public String getSecondAnswer() {
//		return mAnswers.get(1);
//	}
//	
//	public String getThirdAnswer() {
//		return mAnswers.get(2);
//	}
	
	public void setAnswers(ArrayList<String> answers) {
		mAnswers = answers;
	}
	
	public void setAnswers(String[] answers) {
		mAnswers = Arrays.asList(answers);
	}
	
	public void setCorrectAnswer(int correctAnswerIndex) {
		mCorrectAnswer = correctAnswerIndex;
		mCorrectAnswerString = mAnswers.get(correctAnswerIndex);
	}

	public int getCorrectAnswer() {
		return mCorrectAnswer;
	}
	
	public String getCorrectAnswerString() {
		return mCorrectAnswerString;
	}
	
	public void setQuestionsType(QuestionsType type) {
		mType = type;
	}
	
	public QuestionsType getType() {
		return mType;
	}
}
