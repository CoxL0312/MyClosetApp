# MyCloset — Android Wardrobe Manager

MyCloset is an Android application designed to help users organize their wardrobe, store item details, and filter or search through clothing efficiently. 
The app uses Kotlin, Room Database, and a custom teal-themed UI. A custom illustrated cat serves as the app’s logo and splash image, giving the application a light and approachable identity.

---

## Features

### Add and Edit Clothing Items

Users can create and manage clothing entries with:

* Name
* Clothing type
* Color
* Occasion
* An image selected from the device’s gallery

Image selection is implemented through the Storage Access Framework, including persisted URI permissions so images remain accessible even after app restarts.

### Search and Filtering

The list of clothing items can be searched by name and filtered by:

* Type
* Color
* Occasion

### Item Detail View

Each item has a dedicated detail screen displaying:

* Enlarged item image
* Name
* Metadata (type, color, occasion)
* Options to edit or delete the entry

### Custom Teal Theme

The user interface uses a fully customized teal color palette, including:

* Themed buttons
* Rounded corners
* Styled EditTexts and Spinners
* Coordinated backgrounds and text colors
* A consistent visual design across all screens


## Technology Stack

| Component               | Technology                                    |
| ----------------------- | --------------------------------------------- |
| Language                | Kotlin                                        |
| UI                      | XML layouts, custom drawables, theme overlays |
| Database                | Room (SQLite)                                 |
| Architecture            | Multiple Fragments and a single Activity      |
| Asynchronous Operations | Kotlin Coroutines                             |
| Image Handling          | SAF `OpenDocument`, persisted URI permissions |
| Design                  | Custom theme, drawables, color palette        |

---

## Project Structure

```
app/
 └── src/
     └── main/
         ├── java/com/example/mycloset/
         │    ├── database/        # Room entities, DAO, database
         │    ├── ui/              # Fragments for add, list, detail, splash
         │    └── MainActivity.kt
         ├── res/
         │    ├── drawable/        # Logo, rounded button backgrounds, frames
         │    ├── layout/          # Fragment XML files
         │    ├── values/          # Colors, themes, styles
         │    └── mipmap/          # App icons
         └── AndroidManifest.xml
```

---

## How to Run

1. Clone the repository:

```
git clone https://github.com/CoxL0312/MyClosetApp.git
```

2. Open the project in Android Studio
3. Allow Gradle to sync
4. Run on an emulator or a physical device


## Credits

Application designed and developed by Lindsey Cox.
UI theming and structure crafted for clarity, usability, and a cohesive aesthetic.

