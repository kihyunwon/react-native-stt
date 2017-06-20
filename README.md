# react-native-stt

[![DUB](https://img.shields.io/dub/l/vibe-d.svg?style=flat-square)](https://github.com/marshmelloX/react-native-stt#license)

react-native-stt is a speech-to-text library for [React Native](https://facebook.github.io/react-native/).

This project combines the works of [react-native-speech-to-text-ios](https://github.com/muhaos/react-native-speech-to-text-ios) and [react-native-android-speech-recognizer](https://github.com/de-code/react-native-android-speech-recognizer).

## Documentation
- [Install](https://github.com/marshmelloX/react-native-stt#install)
- [Usage](https://github.com/marshmelloX/react-native-stt#usage)
- [Example](https://github.com/marshmelloX/react-native-stt/tree/master/example)
- [License](https://github.com/marshmelloX/react-native-stt#license)

## Install

```shell
npm install --save react-native-stt
react-native link react-native-stt
```

### Manual

#### Android

1. Open up `MainApplication.java`
  - Import `com.deadmau.rnspeechtotext.RNSpeechToTextPackage;`
  - Add `new RNSpeechToTextPackage()` to the `getPackages()` method
2. Insert the following lines in `android/settings.gradle`:
  ```
  include ':react-native-stt'
  project(':react-native-stt').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-stt/android/lib')
  ```
3. Insert the following lines in `android/app/build.gradle`:
  ```
    compile project(':react-native-stt')
  ```

## Usage

### Imports

```js
import Stt from 'react-native-stt';
```

### Listening

Sets locale & emits speech-to-text result.
If locale is not set, it will start with device default locale.

```js
Stt.start('en-US');
```

Stops speech recognition (but, it will process the last data it received).  

```js
Stt.stop();
```

Destroys Stt.

```js
Stt.destroy();
```

### List Locales

Returns list of available languages

```js
...
Stt.supportedLocales();
...
Stt.addListener('supportedLocales', (result) => {
  if (result.error) {
    alert(JSON.stringify(result.error));
  } else {
    if (Platform.os === 'android') {
      console.log(result.supportedLanguages);
    } else{
      console.log(result);
    }
  }
});

// Prints:
//
// en-GB,
// ...
// uk-UA
```

## License

MIT
