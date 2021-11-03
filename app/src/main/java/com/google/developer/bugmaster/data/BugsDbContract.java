package com.google.developer.bugmaster.data;


import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Pegasus on 13/10/2017.
 * The contract defines the insects database and how to
 * interact with it through the Content Provider.
 */

public class BugsDbContract {

        static final String AUTHORITY = "com.google.developer.bugmaster";
        static final String PATH_INSECT = "insect";
        static final String PATH_INSECT_WITH_NAME = "insect/*";
        private static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);

        private BugsDbContract() {

        }

        public static final class InsectContract implements BaseColumns {
            public static final Uri URI = BASE_URI.buildUpon().appendPath(PATH_INSECT).build();
            public static final String TABLE_NAME = "insects";
            public static final String COLUMN_FRIENDLY_NAME = "friendly_name";
            public static final String COLUMN_SCIENTIFIC_NAME = "scientific_name";
            public static final String COLUMN_CLASSIFICATION = "classification";
            public static final String COLUMN_IMAGE_ASSET = "image_asset";
            public static final String COLUMN_DANGER_LEVEL = "danger_level";

            public static final int POSITION_ID = 0;
            public static final int POSITION_FRIENDLY_NAME = 1;
            public static final int POSITION_SCIENTIFIC_NAME = 2;
            public static final int POSITION_CLASSIFICATION = 3;
            public static final int POSITION_IMAGE_ASSET = 4;
            public static final int POSITION_DANGER_LEVEL = 5;

            public static final String[] INSECT_COLUMNS_PROJECTION = new String[]{
                    _ID,
                    COLUMN_FRIENDLY_NAME,
                    COLUMN_SCIENTIFIC_NAME,
                    COLUMN_CLASSIFICATION,
                    COLUMN_IMAGE_ASSET,
                    COLUMN_DANGER_LEVEL
            };

            public static Uri makeUriForInsect(String insect) {
                return URI.buildUpon().appendPath(insect).build();
            }

            public String getInsectFromUri(Uri queryUri) {
                return queryUri.getLastPathSegment();
            }
        }
    }
