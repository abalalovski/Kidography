
package com.twodwarfs.kidography.db.scores;

/**
 * @author Aleksandar Balalovski <abalalovski@gmail.com>
 */

public class ScoresDBCreationString {

	protected static final String POINTS_TABLE_CREATE = "CREATE TABLE "
			+ ScoresFields.TABLE_NAME + " ("
			+ ScoresFields.ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
			+ ScoresFields.PLAYER_NAME + " TEXT,"
			+ ScoresFields.POINTS + " INTEGER);"; 

	protected static final String[] ALL_CREATES = { 
		POINTS_TABLE_CREATE, 
	};
}