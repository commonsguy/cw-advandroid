package com.commonsware.android.appwidget;

import android.test.ActivityInstrumentationTestCase;

/**
 * This is a simple framework for a test of an Application.	See
 * {@link android.test.ApplicationTestCase ApplicationTestCase} for more information on
 * how to write and extend Application tests.
 * <p/>
 * To run this test, you can type:
 * adb shell am instrument -w \
 * -e class com.commonsware.android.appwidget.TWPrefsTest \
 * com.commonsware.android.appwidget.tests/android.test.InstrumentationTestRunner
 */
public class TWPrefsTest extends ActivityInstrumentationTestCase<TWPrefs> {

		public TWPrefsTest() {
				super("com.commonsware.android.appwidget", TWPrefs.class);
		}

}