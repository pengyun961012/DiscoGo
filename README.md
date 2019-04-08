# eecs441Disco

A repository for eecs441 project front-end application. We use Android and Unity as development platform. All the projects are built in Android Studio and Unity Platform. 

## Folder Structure
-readme.md . 
-StarterApp/ . 
-AudioProcess/ . 
-SkeletalProduct/ . 
-FlappyBird/ . 
-src/ . 
-MVP/ .  
-Backend/ . 
-Noteprocessing/

## Project Run and Build
### StarterApp
Built in Android Studio. Follow the spec and nearly the same as instruction. Try to build and run by using Android Studio. Lowest API level is 17. Make sure you have Internet connection when testing your app. Also, please contact the back-end server admin to turn on the server.

### Audio Process
Built in Android Studio. This is an testing app for the audio processing and login page. Main Activity includes a recorder to get amplitude of the voice, a Facebook login page and a Google login page. Test Activity includes a FFT and DSP algorithm on audio and frequency wave map of the audio.

### Skeletal Product
Built in Android Studio. This is a fulling funtional skeletal product. Includes all the UI/UX design for Main, Profile, Shop, Leaderboard and Friends page. Linked with Flappy Bird Unity Product. Enable the message transfer between Android and Unity. Able to play a simple game on this product.

### Flappy Bird
Built in Unity. This is a simple Flappy bird game that can be played on Android phone. It receives message from the Audio Process App and adjust the height of the brid accordingly. When the game ends it will send statistics back to the android app to be processed. The game is close to completion except for the completion of UIs , the implementation of the game clear page and its communication with the android app.

### Source
UI/UX and icon source of the front end app.

### MVP
Built in Android Studio. Includes all MVP code check features. Updates in UI/UX design, user interact logic and game.

### Backend
Backend server developed in python. Should be deployed on digitalocean server. Works together with django database.

### Noteprocessing
Use C#. Extract note identity from a midi file. Use it for game obstracle placement.

