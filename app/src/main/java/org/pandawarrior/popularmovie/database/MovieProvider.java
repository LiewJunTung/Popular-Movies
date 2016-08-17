package org.pandawarrior.popularmovie.database;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Popular Movie App
 * Created by jtlie on 8/17/2016.
 */
@ContentProvider(authority = MovieProvider.AUTHORITY, database = AppDatabase.class)
public final class MovieProvider {
    public static final String AUTHORITY = "org.pandawarrior.popularmovie";

    @TableEndpoint(table = AppDatabase.MOVIES)
    public static class Movies {
        @ContentUri(
                path = "movies",
                type = "vnd.android.cursor.dir/movies"
        )
        public static final Uri MOVIES_URI = Uri.parse("content://" + AUTHORITY + "/movies");

        @InexactContentUri(
                path = "movies/#",
                name = "MOVIE_ID",
                type = "vnd.android.cursor.item/movies",
                whereColumn = MovieColumns._ID,
                pathSegment = 1
        )
        public static Uri buildMovieId(long id){
            return Uri.parse("content://" + AUTHORITY + "/movies/" + id);
        }
    }
}
