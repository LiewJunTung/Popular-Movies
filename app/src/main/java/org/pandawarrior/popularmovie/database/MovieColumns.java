package org.pandawarrior.popularmovie.database;

import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.PrimaryKey;

import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.REAL;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

/**
 * Popular Movie App
 * Created by jtlie on 8/17/2016.
 */

public interface  MovieColumns {
    String TABLE_NAME = "movies";

    @DataType(INTEGER)
    @PrimaryKey
    String _ID = "_id";

    @DataType(TEXT)
    String POSTER_PATH = "poster_path";

    @DataType(INTEGER)
    String ADULT = "adult";

    @DataType(TEXT)
    String OVERVIEW = "overview";

    @DataType(TEXT)
    String ORIGINAL_TITLE = "original_title";

    @DataType(TEXT)
    String ORIGINAL_LANGUAGE = "original_language";

    @DataType(TEXT)
    String TITLE = "title";

    @DataType(TEXT)
    String BACKDROP_PATH = "backdrop_path";

    @DataType(REAL)
    String POPULARITY = "popularity";

    @DataType(INTEGER)
    String VOTE_COUNT = "vote_count";

    @DataType(INTEGER)
    String VIDEO = "video";

    @DataType(REAL)
    String VOTE_AVERAGE = "vote_average";

    @DataType(TEXT)
    String RELEASE_DATE = "release_date";

}
