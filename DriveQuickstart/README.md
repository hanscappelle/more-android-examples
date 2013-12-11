
# Project Description

This is a WORKING Google Drive Quickstart example. For instructions on how to set up the project check the original documentation at https://developers.google.com/drive/quickstart-android . This is only a listing of the changes I had to make to get it working.

These changes are also listed at the following Stackoverflow question: http://stackoverflow.com/questions/17429798/usingoauth2-deprecated/20517193#20517193

# Other updates required

## oAuth2 Method changed

The usingOAuth2 method signature changed over time. Instead of a single String it now accepts a collection as second parameter. This is the only error that will prevent you from building the application using Eclipse ADT.

    // had to update this part to a collection of String objects instead of a single String obj ref
    // also tested updating DRIVE to DRIVE_FILE both seem to work fine though the more specific the better
    credential = GoogleAccountCredential.usingOAuth2(this, Arrays.asList(new String[]{DriveScopes.DRIVE_FILE}));


## Google Play Services Lib

The documentation uses the old way of adding libraries to an Android project. This will fail when executed with the latest ADT. You will be able to compile and upload to the device/emulator but on execution you'll get a NoClassDefFoundError. 

    java.lang.NoClassDefFoundError: Failed resolution of: Lcom/google/android/gms/common/AccountPicker;

To fix this you should copy paste the `google-play-services.jar` file to the `libs` folder instead.

## Missing meta-tag

Up to the next error. I then received an `IllegalStateException` with the instructions to add a meta-tag in the manifest holding the google-play-services version.

So within the application tag of the manifest add:

    <meta-data android:name="com.google.android.gms.version" android:value="@integer/google_play_services_version"/>

And in one of the resource files (`res/values/a-file-here.xml`) add the following:

    <integer name="google_play_services_version">4030500</integer>

In my case the lib matched this version. If you enter the wrong version here you'll get an error showing the proper version to specify. So make sure to check output.

## Permission Denied

Finally I got a prompt for oauth in the app just to find out the example app is still missing a permission. The error for reference:

    java.io.FileNotFoundException: /storage/emulated/0/Pictures/IMG_20131211_110629.jpg: open failed: EACCES (Permission denied)

Next to the permissions listed within the example:

    <uses-permission android:name="android.permission.GET_ACCOUNTS" />
    <uses-permission android:name="android.permission.INTERNET" />

Also add the WRITE_EXTERNAL_STORAGE permission in your manifest:

    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>

## More resources

If you get an `com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAuthIOException` exception with in the root cause somewhere a description `Unknown` you should check the settings in the Google API console at https://cloud.google.com/console. I received this error on a package mismatch.

Another links of interest are the oauth2 documentation at https://developers.google.com/accounts/docs/OAuth2 and the google api playground at ttps://developers.google.com/oauthplayground/.
    
    // java.io.FileNotFoundException: /storage/emulated/0/Pictures/IMG_20131211_110629.jpg: open failed: EACCES (Permission denied)
    // and had to add the following permission to fix this
    //   <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>