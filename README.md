# Location Manager

## Description:
  Lightweight application for handling Android phone location features. Allows to scan location QR(barcodes), save and see favorite places, see user location and address in real time, without Internet connection. 

## Application features: 
- QR scanner:
   - recognizes and scans QR location codes
   - shows favorite places
- hiking tool:
   - displays user lat, long, alt and address
   - informs user with sound (mutable) when location updates
- customized Google map:
   - shows user location
   - shows place from QR code
   - show favorite places
   - user can mark and save favorite places as well
   
## Application screenshots:
![Game process](https://github.com/Harnet69/Location/blob/master/app/GitHubMediaFile/main_screen_v.png)
![Game process](https://github.com/Harnet69/Location/blob/master/app/GitHubMediaFile/QR_scan_fav_pl.png)
![Game process](https://github.com/Harnet69/Location/blob/master/app/GitHubMediaFile/hike.png)
![Game process](https://github.com/Harnet69/Location/blob/master/app/GitHubMediaFile/Google_maps.png)
   

## Used technologies:
- MVVM architecture pattern
- Google Maps API
- fragments
- recycleView
- soundPool
- sharredPreferences

## Future plans:
- edit/delete/change favorite places and their order
- add places photo by a camera
- recognize different kinds of barcodes (for example URL QR codes will show a page in a browser, Google location QR code)
- user settings : changing quantity of location updating, application sounds volume, map scale, etc
- sharing user location and favorite places with friends (Facebook, Gmail)
