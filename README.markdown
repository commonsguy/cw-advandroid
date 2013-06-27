Welcome to the source code for _The Busy Coder's Guide to
Advanced Android Development_!

**Note that this book has been retired, and so if you are looking for more up-to-date
material, you probably want [The Busy Coder's Guide to Android Development](http://commonsware.com/Android)
and [the cw-omnibus repo](http://github.com/commonsguy/cw-omnibus).**

All of the source code in this archive is licensed under the
Apache 2.0 license except as noted.

The names of the top-level directories roughly correspond to a
shortened form of the chapter titles. Since chapter numbers
change with every release, and since some samples are used by
multiple chapters, I am loathe to put chapter numbers in the
actual directory names.

If you wish to use this code, bear in mind a few things:

* The projects are set up to be built by Ant, not by Eclipse.
	If you wish to use the code with Eclipse, you will need to
	create a suitable Android Eclipse project and import the
	code and other assets.

* You should delete build.xml from the project, then run

		android update project -p ...
  (where ... is the path to a project of interest)

	on those projects you wish to use, so the build files are
	updated for your Android SDK version.

