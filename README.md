# eecs441Disco

A repository for eecs441 project front-end application. We use Android and Unity as development platform. All the projects are built in Android Studio and Unity Platform. 

## Folder Structure
-readme.md
-StarterApp/
-AudioProcess/

## Project Run and Build
### StarterApp
Built in Android Studio. Follow the spec and nearly the same as instruction. Try to build and run by using Android Studio. Lowest API level is 17. Make sure you have Internet connection when testing your app. Also, please contact the back-end server admin to turn on the server.

### Audio Process
Built in Android Studio. This is an testing app for the audio processing and login page. Main Activity includes a recorder to get amplitude of the voice, a Facebook login page and a Google login page. Test Activity includes a FFT on audio and frequency wave map of the audio.

### Flappy Bird
Built in Unity. This is a simple Flappy bird game that can be played on Android phone. It receives message from the Audio Process App and adjust the height of the brid accordingly. When the game ends it will send statistics back to the android app to be processed. The game is close to completion except for the completion of UIs , the implementation of the game clear page and its communication with the android app.

### Source
UI/UX and icon source of the front end app.
