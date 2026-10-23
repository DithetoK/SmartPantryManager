# Smart Pantry Manager

A Java Android app that helps reduce food waste. The user adds the ingredients they
have at home, and the app suggests recipes they can cook using **only** the ingredients left in the pantry.

## Features
- Add, edit and delete pantry items (name, quantity, unit, expiry date(optional))
- Pantry list screen (ListView with a custom adapter)
- 20 pre-loaded recipes stored in the database
- Possible Recipes screen using strict matching rules: a recipe only appears if 
  ingredient is in the pantry in at least the required quantity
- Matching ignores capital letters and simple plurals such as pear/pears and converts
  kg to g and l to ml
- Recipe detail screen with ingredients and method
- Settings screen with expiring-soon alerts on/off
- Input validation on the add/edit form
- Message shown when no recipes match

## Database: 
This  Smart Pantry Manager uses SQLite because it is built into Android, stores the data on the phone so the
pantry is still there after the app is closed, and needs no internet or server.

## How to run
1. Install Android Studio.
2. Clone this repository: `git clone https://github.com/DithetoK/SmartPantryManager.git`
3. In Android Studio choose File > Open and select the project folder.
4. Wait for Gradle sync to finish.
5. Press Run with an emulator or a phone connected.