# MovieInfo
Provides movies, tv shows and person information from [TMDB API](https://api.tmdb.org/).

## 📸 Screenshots
|   |   |   |   |
|---|---|---|---|
| <img src = "./images/onboarding.png" width = 240/> | <img src = "./images/movies.png" width = 240/> | <img src = "./images/tv.png" width = 240/> | <img src = "./images/details.png" width = 240/> |
| <img src = "./images/search.png" width = 240/> | <img src = "./images/you.png" width = 240/> | <img src = "./images/library.png" width = 240/> |


## ✨ Features
- Discover popular and trending movies and TV shows.
- Connect your TMDB account to access your TMDB watchlists and favorites.

## 🏗️ Architecture
- Built with a multi-module architecture, utilizing a hybrid approach that combines feature and layer-based modularization.
- Inspired by [common modularization patterns](https://developer.android.com/topic/modularization/patterns).

## 🖥️Installation
To clone the project, run

```
git clone git@github.com:anshtya/MovieInfo.git
```
Open the project in Android Studio.

Visit [TMDB API](https://api.tmdb.org/) to obtain Access Token. Open `local.poperties` file and type the following:
```
ACCESS_TOKEN = your_access_token
BASE_URL=https://api.themoviedb.org/3/
```

and rebuild the project.

## ℹ️Disclaimer
<img src = "./images/tmdb-logo.svg" width = 120>

This product uses the [TMDB API](https://api.tmdb.org/) but is not endorsed or certified by [TMDB](https://themoviedb.org).
