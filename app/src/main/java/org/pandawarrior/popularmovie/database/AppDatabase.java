package org.pandawarrior.popularmovie.database;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Popular Movie App
 * Created by jtlie on 8/17/2016.
 */

@Database(version = AppDatabase.VERSION)
public final class AppDatabase {
    public static final int VERSION = 1;

    @Table(MovieColumns.class)
    public static final String MOVIES = MovieColumns.TABLE_NAME;
}
