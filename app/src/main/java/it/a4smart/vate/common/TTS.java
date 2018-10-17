package it.a4smart.vate.common;

import android.content.Context;
import android.speech.tts.TextToSpeech;


//TODO async problem here need fix.
public class TTS {
    private static TTS instance = null;
    private final TextToSpeech ttsEngine;
    private boolean ttsEnabled;

    private TTS(Context context) {
        ttsEngine = new TextToSpeech(context, status -> ttsEnabled = status == TextToSpeech.SUCCESS);
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

    public static synchronized void createNewInstance(Context context) {
        instance = new TTS(context);
    }

    public boolean isSpeaking() {
        return ttsEngine.isSpeaking();
    }

    public void stop() {
        ttsEngine.stop();
    }

    public void destroy() {
        ttsEngine.shutdown();
    }
}
