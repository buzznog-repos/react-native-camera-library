
# react-native-camera-library

## Getting started

`$ npm install react-native-camera-library --save`

### Mostly automatic installation

`$ react-native link react-native-camera-library`

### Manual installation


#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-camera-library` and add `RNCameraLibrary.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNCameraLibrary.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.reactlibrary.RNCameraLibraryPackage;` to the imports at the top of the file
  - Add `new RNCameraLibraryPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-camera-library'
  	project(':react-native-camera-library').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-camera-library/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-camera-library')
  	```

#### Windows
[Read it! :D](https://github.com/ReactWindows/react-native)

1. In Visual Studio add the `RNCameraLibrary.sln` in `node_modules/react-native-camera-library/windows/RNCameraLibrary.sln` folder to their solution, reference from their app.
2. Open up your `MainPage.cs` app
  - Add `using Camera.Library.RNCameraLibrary;` to the usings at the top of the file
  - Add `new RNCameraLibraryPackage()` to the `List<IReactPackage>` returned by the `Packages` method


## Usage
```javascript
import RNCameraLibrary from 'react-native-camera-library';

// TODO: What to do with the module?
RNCameraLibrary;
```
  # react-native-camera-library
