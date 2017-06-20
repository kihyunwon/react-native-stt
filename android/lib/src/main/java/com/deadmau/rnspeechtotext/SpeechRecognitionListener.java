package com.deadmau.rnspeechtotext;

import android.os.Bundle;
import android.speech.RecognitionListener;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.RCTNativeAppEventEmitter;

public class SpeechRecognitionListener implements RecognitionListener {

  private static final String TAG = SpeechRecognitionListener.class.getSimpleName();

  private final String SPEECH_TO_TEXT = "SpeechToText";
  private final RCTNativeAppEventEmitter emitter;

  public SpeechRecognitionListener(RCTNativeAppEventEmitter emitter) {
    this.emitter = emitter;
  }

  private void emit(String event, Object data) {
    this.emitter.emit(event, data);
  }

  @Override
  public void onBeginningOfSpeech() {
    Logger.debug(TAG, "onBeginningOfSpeech");
    this.emit(SPEECH_TO_TEXT, null);
  }

  @Override
  public void onBufferReceived(byte[] buffer) {
    Logger.debug(TAG, "onBufferReceived");
    WritableMap data = Arguments.createMap();
    data.putArray("buffer", Arguments.fromArray(buffer));
    emit(SPEECH_TO_TEXT, data);
  }

  @Override
  public void onEndOfSpeech() {
    Logger.debug(TAG, "onEndOfSpeech");
    this.emit(SPEECH_TO_TEXT, null);
  }

  @Override
  public void onError(int error) {
    Logger.error(TAG, "onError: " + error);
    WritableMap data = Arguments.createMap();
    data.putInt("error", error);
    emit(SPEECH_TO_TEXT, data);
  }

  @Override
  public void onEvent(int eventType, Bundle params) {
    Logger.debug(TAG, "onEvent: " + eventType);
    WritableMap data = Arguments.createMap();
    data.putInt("eventType", eventType);
    data.putMap("params", Arguments.fromBundle(params));
    emit(SPEECH_TO_TEXT, data);
  }

  @Override
  public void onPartialResults(Bundle partialResults) {
    Logger.debug(TAG, "onPartialResults: " + partialResults);
    WritableMap data = Arguments.createMap();
    data.putMap("partialResults", Arguments.fromBundle(partialResults));
    emit(SPEECH_TO_TEXT, data);
  }

  @Override
  public void onReadyForSpeech(Bundle params) {
    Logger.debug(TAG, "onReadyForSpeech: " + params);
    WritableMap data = Arguments.createMap();
    data.putMap("params", Arguments.fromBundle(params));
    emit(SPEECH_TO_TEXT, data);
  }

  @Override
  public void onResults(Bundle results) {
    Logger.debug(TAG, "onResults: " + results);
    WritableMap data = Arguments.createMap();
    data.putMap("results", Arguments.fromBundle(results));
    emit(SPEECH_TO_TEXT, data);
  }

  @Override
  public void onRmsChanged(float rmsdB) {
    Logger.debug(TAG, "onRmsChanged: " + rmsdB);
    WritableMap data = Arguments.createMap();
    data.putDouble("rmsdB", rmsdB);
    emit(SPEECH_TO_TEXT, data);
  }
}
