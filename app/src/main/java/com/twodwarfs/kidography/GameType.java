
package com.twodwarfs.kidography;


/**
 * @author Aleksandar Balalovski <abalalovski@gmail.com>
 */

public class GameType {
	
	public enum Type { TWELVER, LIMITED };
	public enum QuestionsType { RIDDLES, CAPITALS, FLAGS, LANDMARKS, MIXED };

	private Type mGameType;
	private QuestionsType mQuestionsType;
	
	private static GameType sInstance = null;
	
	public void setGameType(Type gameType) {
		mGameType = gameType;
	}
	
	public Type getGameType() {
		return mGameType;
	}
	
	public void setQuestionsType(QuestionsType type) {
		mQuestionsType = type;
	}
	
	public QuestionsType getQuestionsType() {
		return mQuestionsType;
	}

	protected GameType() {
	}

	public static GameType instance() {
		if(sInstance == null) {
			sInstance = new GameType();
		}

		return sInstance;
	}
}
