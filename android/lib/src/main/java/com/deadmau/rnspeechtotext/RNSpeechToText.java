package com.deadmau.rnspeechtotext;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.modules.core.RCTNativeAppEventEmitter;

import java.util.Locale;

public class RNSpeechToText extends ReactContextBaseJavaModule {

  private static final String TAG = RNSpeechToText.class.getSimpleName();

  private final Handler mainHandler;

  private ReactApplicationContext reactContext;
  private SpeechRecognizer mSpeechRecognizer;

  public RNSpeechToText(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
    this.mainHandler = new Handler(reactContext.getMainLooper());
  }

  @Override
  public String getName() {
    return "RNSpeechToText";
  }

  public void getLocales() {
    Intent detailsIntent =  new Intent(RecognizerIntent.ACTION_GET_LANGUAGE_DETAILS);
    reactContext.sendOrderedBroadcast(
        detailsIntent, null,
        new LocalesBroadcastReceiver(this.reactContext.getJSModule(RCTNativeAppEventEmitter.class)),
        null, Activity.RESULT_OK, null, null);
  }

  @ReactMethod
  public void supportedLocales() {
    Logger.debug(TAG, "supportedLocales, posting to main thread");
    this.mainHandler.post(new Runnable() {
      @Override
      public void run() {
        Logger.debug(TAG, "supportedLocales (main thread)");

        getLocales();
      }
    });
  }

  @ReactMethod
  public void cancel() {
    Logger.debug(TAG, "cancel, posting to main thread");
    this.mainHandler.post(new Runnable() {
      @Override
      public void run() {
        Logger.debug(TAG, "cancel (main thread)");

        if (mSpeechRecognizer != null) {
          mSpeechRecognizer.cancel();
        }
      }
    });
  }

  @ReactMethod
  public void destroy() {
    Logger.debug(TAG, "destroy, posting to main thread");
    this.mainHandler.post(new Runnable() {
      @Override
      public void run() {
        Logger.debug(TAG, "destroy (main thread)");

        if (mSpeechRecognizer != null) {
          mSpeechRecognizer.destroy();
          mSpeechRecognizer = null;
        }
      }
    });
  }

  private String getLocale(String locale){
    if(locale != null && !locale.equals("")){
      return locale;
    }

    return Locale.getDefault().toString();
  }

  private Intent createIntent(String locale) {
    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        .putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
        .putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
        .putExtra(RecognizerIntent.EXTRA_LANGUAGE, getLocale(locale))
        .putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      intent.putExtra(RecognizerIntent.EXTRA_PREFER_OFFLINE, false);
    }

    return intent;
  }

  @ReactMethod
  public void createSpeechRecognizer(final Promise promise) {
    if (reactContext == null)
      throw new IllegalArgumentException("ReactApplicationContext must be defined!");

    if (mSpeechRecognizer != null) {
      mSpeechRecognizer.destroy();
      mSpeechRecognizer = null;
    }

    if (SpeechRecognizer.isRecognitionAvailable(reactContext)) {
      mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(reactContext);
      mSpeechRecognizer.setRecognitionListener(new SpeechRecognitionListener(
          this.reactContext.getJSModule(RCTNativeAppEventEmitter.class)
      ));
      promise.resolve(null);
    } else{
      promise.reject("error", "SpeechRecognizer not available");
    }
  }

  @ReactMethod
  public void start(String locale) {
    final Intent intent = createIntent(locale);
    Logger.debug(TAG, "startListening, posting to main thread");
    this.mainHandler.post(new Runnable() {
      @Override
      public void run() {
        Logger.debug(TAG, "startListening (main thread)");

        if (mSpeechRecognizer != null) {
          mSpeechRecognizer.startListening(intent);
        }
      }
    });
  }

  @ReactMethod
  public void stop() {
    Logger.debug(TAG, "stopListening, posting to main thread");
    this.mainHandler.post(new Runnable() {
      @Override
      public void run() {
        Logger.debug(TAG, "stopListening (main thread)");

        if (mSpeechRecognizer != null) {
          mSpeechRecognizer.stopListening();
        }
      }
    });
  }

}
