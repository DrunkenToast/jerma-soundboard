# App dev project - Jerma Soundboard

## Requirements

### This year

#### Lab 1

- [X] App has a (small) database
- [X] App has 2 activities, main activity shows a list of the contents of the
database and a details activity that shows all the data of one row
- [X] Content provider class handles all database accesses

#### Lab 2

- [X] Introduce fragments in your app
  - Easiest most meaningful way is Master-Detail flow (smartphone and 
  tablet layout)

#### Lab 3

- [X] Pick a color scheme, with three primary colors and a secondary color
  - Changed the default theme to use a color palette (used a material color picker)
- [X] Create a style of your own for one of your views
- [X] Create one or more assets for different screen sizes
  - Button in recyclerview has different assets on large screen sizes
- [X] Use a Floating Action Button for a primary action in your app
  - To add audio
- [X] Integrate one of the common design patterns like a toolbar, appbar, tabs
navigation, drawer, ... (Look at what is most useful for your app)
  - Toolbar in the main activity with menu for settings and adding audio
- [X] Create a surface and try to incorporate scrolling of your surfaces
  - Surface for showing the stream status of jerma
  - Will scroll underneath toolbar when recyclerview is scrolled down
- [X] Try to adhere to the standard sizes and keylines which are recommended by
[Material design](https://material.io/)

#### Lab 4

- [X] incorporate two UI tests for your application
  - Test if recyclerview's first three items are AA, EE and OO
  - Test if text in stream button is correct

### Previous year

What should be included: 
- [X] Different activities (use intents)
  - Main activity
  - Add audio activity
  - Settings activity
- [X] Different layouts for portrait and landscape mode and localization in at least two languages (eng and your own language) 
  - Main activity has a landscape and portrait mode
- [X] Two or more user settings 
  - Playback rate
  - Theme settings
  - Jerma URL
- [X] RecyclerView
  - Grid layout for audio items
- [X] Database
  - Add audio activity adds an entry
  - Holding custom item will remove it
Optional: 
- [X] Internet usage 
  - On startup will check jerma's stream status
