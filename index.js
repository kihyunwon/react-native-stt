import { NativeModules, NativeEventEmitter, Platform } from 'react-native';

const SpeechToText = NativeModules.RNSpeechToText;

class Stt extends NativeEventEmitter {
  constructor() {
    super(SpeechToText);
  }

  cancle() {
    if (Platform.os === 'android') {
      return SpeechToText.cancel();
    }
  }

  start(locale = '') {
    if (Platform.os === 'android') {
      return SpeechToText.createSpeechRecognizer()
        .then(() => {
          return SpeechToText.start(locale);
        });
    } else{
      return SpeechToText.start(locale);
    }
  }

  stop() {
    return SpeechToText.stop();
  }

  destroy() {
    return SpeechToText.destroy();
  }

  supportedLocales() {
    return SpeechToText.supportedLocales();
  }

  addEventListener(type, handler) {
    this.addListener(type, handler);
  }

  removeEventListener(type, handler) {
    this.removeListener(type, handler);
  }
}

export default new Stt();
