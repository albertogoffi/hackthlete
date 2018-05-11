# hackthlete

Outhlete @ Hack the City 2018 in Lugano, Switzerland.

## Google API Key

To build and execute the project, you need a Google API Key. You can create a key at this address
[https://console.cloud.google.com](https://console.cloud.google.com)

The key must support the following APIs:

* Directions API
* Geocoding API
* Maps SDK for Android
* Places SDK for Android

Once you have the key, run the following commands:
```
echo '<resources>' >> app/src/debug/res/values/google_maps_api.xml
echo '<string name="google_maps_key" templateMergeStrategy="preserve" translatable="false">' >> app/src/debug/res/values/google_maps_api.xml
echo YOUR GOOGLE APIs KEY >> app/src/debug/res/values/google_maps_api.xml
echo '</string>' >> app/src/debug/res/values/google_maps_api.xml
echo '</resources>' >> app/src/debug/res/values/google_maps_api.xml

```

## How to Build

To build the project run the command:
```
./gradlew build
```
