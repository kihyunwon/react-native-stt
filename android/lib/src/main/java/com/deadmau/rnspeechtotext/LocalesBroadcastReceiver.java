package com.deadmau.rnspeechtotext;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.RCTNativeAppEventEmitter;

import java.util.List;

public class LocalesBroadcastReceiver extends BroadcastReceiver {

  private static final String TAG = LocalesBroadcastReceiver.class.getSimpleName();

  private final RCTNativeAppEventEmitter emitter;
  private final String SUPPORTED_LOCALES = "supportedLocales";

  public LocalesBroadcastReceiver(RCTNativeAppEventEmitter emitter) {
    this.emitter = emitter;
  }

  private void emit(String event, Object data) {
    this.emitter.emit(event, data);
  }

  @Override
  public void onReceive(Context context, Intent intent) {
    Logger.debug(TAG, "onReceive");
    Bundle results = getResultExtras(true);
    WritableMap data = Arguments.createMap();
    if (results.containsKey(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE))
    {
      String languagePreference =
          results.getString(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE);
      data.putString("languagePreference", languagePreference);
    }
    if (results.containsKey(RecognizerIntent.EXTRA_SUPPORTED_LANGUAGES))
    {
      List<String> supportedLanguages =
          results.getStringArrayList(
              RecognizerIntent.EXTRA_SUPPORTED_LANGUAGES);
      WritableArray languages =
          Arguments.fromArray(supportedLanguages.toArray(new String[0]));
      data.putArray("supportedLanguages", languages);
    }

    emit(SUPPORTED_LOCALES, data);
  }
}