# Location Manager

## Description:
  Lightweight application for handling Android phone location features. Allows to scan location QR(barcodes), save and see favorite places, see user location and address in real time, without Internet connection. 
  
## Application screenshots:
![Game process](https://github.com/Harnet69/Location/blob/master/app/GitHubMediaFile/main_screen_v.png)
![Game process](https://github.com/Harnet69/Location/blob/master/app/GitHubMediaFile/QR_scan_fav_pl.png)
![Game process](https://github.com/Harnet69/Location/blob/master/app/GitHubMediaFile/hike.png)
![Game process](https://github.com/Harnet69/Location/blob/master/app/GitHubMediaFile/Google_maps.png)

## Application modules: 
- QR scanner:
   - recognizes and scans QR location codes
   - list of favourite places(short click redirect to the place on Google Maps, long click - to an edit page)
- hiking tool:
   - displays user lat, long, alt and address
   - informs user with sound (mutable) when location updates
- customized Google map:
   - shows user location
   - shows place from QR code
   - show favorite places
   - user can mark and save favorite places as well
- menu:
   - profile
   - settings
   - help
   
## Online QR code generator page
You can generate location code for any place (we recomend min. 300x300 px. resolution)
[Generate QR location code](https://qrickit.com/qrickit_apps/qrickit_qrcode_creator_text.php)
### Warsaw
Example of QR code for testing purposes: 
![Game process](https://github.com/Harnet69/Location/blob/master/app/GitHubMediaFile/Warsaw.png)

## Application installation:
- download .apk [Location manager installer](https://drive.google.com/file/d/1ei8HSaC6wGgUoAcRrGC3ehEx4yVbQVt9/view?usp=sharing)
- run it on your Android phone

## Application pdf presentation: 
[Location manager presentation](https://drive.google.com/file/d/1DdzJGwpPzItiWG67BYHCk3LdBrFXkCwb/view?usp=sharing)

## Used technologies:
- MVVM architecture pattern
- Google Maps API
- Fragments
- RecycleView
- SoundPool
- SharredPreferences

## Future plans:
- edit/delete/change favorite places and their order in QR scanner&places module
- add places photo by a camera
- recognize different kinds of barcodes (for example URL QR codes will show a page in a browser, Google location QR code)
- menu:
   - profile page: Google Oauth / Facebook authorization, user's photo
   - user settings page: changing quantity of location updating, application sounds volume, map scale, etc
   - hepl
- remote SQL database for keeping user data(favorite places and settings)
- sharing user location and favorite places with friends (Facebook, Gmail)
