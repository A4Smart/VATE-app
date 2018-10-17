package it.a4smart.vate.common;

import android.content.Context;
import android.speech.tts.TextToSpeech;

public class TTS {
    private static TTS instance = null;
    private final TextToSpeech ttsEngine;
    private boolean ttsEnabled;

    private TTS(Context context, Runnable runnable) {
        ttsEngine = new TextToSpeech(context, status -> {
            ttsEnabled = status == TextToSpeech.SUCCESS;
            runnable.run();
        });
    }

    public void speak(String textToSay) {
        ttsEngine.speak(textToSay, TextToSpeech.QUEUE_ADD, null, "VATE_tts");
    }

    public boolean isEnabled() {
        return ttsEnabled;
    }

    public static TTS getInstance() {
        return instance;
    }

    public static synchronized void createNewInstance(Context context, Runnable runnable) {
        instance = new TTS(context, runnable);
    }

    public boolean isSpeaking() {
        return ttsEngine.isSpeaking();
    }

    public void stop() {
        ttsEngine.stop();
    }

    private void shutdown() {
        ttsEngine.shutdown();
    }

    public static synchronized void destroy() {
        if (instance != null) instance.shutdown();
    }
}
