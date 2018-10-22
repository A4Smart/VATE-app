package it.a4smart.vate.common;

import android.content.Context;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;

import static android.speech.tts.TextToSpeech.QUEUE_ADD;

public class TTS {
    private static TTS instance = null;
    private final TextToSpeech ttsEngine;
    private boolean ttsEnabled;

    private TTS(Context context, Runnable runnable) {
        ttsEngine = new TextToSpeech(context, status -> {
            ttsEnabled = status == TextToSpeech.SUCCESS;
            runnable.run();
        });

        ttsEngine.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {

            }

            @Override
            public void onDone(String utteranceId) {
                endBeep();
            }

            @Override
            public void onError(String utteranceId) {

            }
        });
    }

    public void speak(String textToSay) {
        if (isSpeaking()) stop();
        new Handler().postDelayed(this::startBeep, 500);
        new Handler().postDelayed(() -> addToQueue(textToSay), 750);

    }

    public void addToQueue(String textToSay) {
        ttsEngine.speak(textToSay, QUEUE_ADD, null, "VATE_tts");
    }

    private void startBeep() {
        ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
        toneGen1.startTone(ToneGenerator.TONE_PROP_BEEP, 50);
    }

    private void endBeep() {
        ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
        toneGen1.startTone(ToneGenerator.TONE_PROP_BEEP2, 300);
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
        endBeep();
        ttsEngine.stop();
    }

    private void shutdown() {
        ttsEngine.shutdown();
    }

    public static synchronized void destroy() {
        if (instance != null) instance.shutdown();
    }
}
