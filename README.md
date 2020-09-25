# Location Manager

## Description:
  Lightweight application for handling Android phone location features. Allows to scan location QR(barcodes). Save, delete, edit favorite places. Track and see userlocation and address in real time without Internet connection. 

## Application screenshots:
![Game process](https://github.com/Harnet69/Location/blob/master/app/GitHubMediaFile/main_screen_v.png)
![Game process](https://github.com/Harnet69/Location/blob/master/app/GitHubMediaFile/QR_scan_fav_pl.png)
![Game process](https://github.com/Harnet69/Location/blob/master/app/GitHubMediaFile/hike.png)
![Game process](https://github.com/Harnet69/Location/blob/master/app/GitHubMediaFile/Google_maps.png)

## Application video:
![Game process](https://github.com/Harnet69/Location/blob/master/app/GitHubMediaFile/location_manager.gif)
  
## Application installation:
- download app from [Google Play](https://play.google.com/store/apps/details?id=com.harnet.locationtest) or scan this QR code by your Android phone
![Game process](https://github.com/Harnet69/Location/blob/master/app/GitHubMediaFile/locationManagerGooglePlay.png)
- download .apk [Location manager v.1.1 installer](https://drive.google.com/file/d/1wm_fZUILPWuW7rpSiuFAoZafVXfWpzBR/view?usp=sharing) and run it on Android phone
- clone a project code from this repo to your computer, and run it via Android studio or another Android emulator

## Application pdf presentation: 
[Location manager presentation](https://drive.google.com/file/d/1DdzJGwpPzItiWG67BYHCk3LdBrFXkCwb/view?usp=sharing)

  

## Application modules: 
- QR scanner:
   - recognizes and scans QR location codes
   - list of favourite places(short click redirect to the place on Google Maps, long click - to delete or edit it)
- hiking tool:
   - displays user lat, long, alt and address
   - informs user with sound (mutable) when location updates
- customized Google map:
   - shows user location
   - shows place from QR code
   - show favorite places with name's flag
   - user can mark and save favorite places from there as well
- menu:
   - profile
   - settings
   - help
   
## Online QR code generator page
You can generate location code for any place (we recomend min. 300x300px. resolution)
[Generate QR location code](https://qrickit.com/qrickit_apps/qrickit_qrcode_creator_text.php)
### Warsaw
Example of QR code for testing purposes: 
![Game process](https://github.com/Harnet69/Location/blob/master/app/GitHubMediaFile/Warsaw.png)

## Used technologies:
- MVVM architecture pattern
- Google Maps API
- Fragments
- RecycleView
- SoundPool
- SharredPreferences

## Future plans:
- change an order and sort favourite places in QR&Places module
- add places image by a camera or from gallery 
- recognize different kinds of barcodes (for example URL QR codes will show a page in a browser, Google location QR code, etc)
- menu:
   - profile page: Google Oauth / Facebook authorization, user's photo
   - user settings page: changing quantity of location updating, application sounds volume, map scale, etc
   - hepl
- remote SQL database for keeping user data(favorite places and settings)
- sharing user location and favorite places with friends (via Facebook, Gmail)
