package it.a4smart.vate.guide;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.altbeacon.beacon.Beacon;

import java.util.Collection;
import java.util.Collections;
import java.util.NoSuchElementException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import it.a4smart.vate.R;
import it.a4smart.vate.common.BeaconsFragment;
import it.a4smart.vate.common.Constants;
import it.a4smart.vate.common.TTS;
import it.a4smart.vate.common.VBeacon;

import static it.a4smart.vate.common.Constants.TTS_SCRIPT;

public class GuideFragment extends BeaconsFragment {
    private final static String TAG = "GuideFragment";
    private GuideFSM guideFSM;
    private WebView webView;
    private TTS tts;

    public static GuideFragment newInstance() {
        return new GuideFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guide, container, false);

        webView = view.findViewById(R.id.guideWV);

        guideFSM = new GuideFSM();
        tts = TTS.getInstance();

        return view;
    }

    @Override
    public void handleNewBeacons(Collection<Beacon> beacons) {
        try {
            VBeacon nearest = new VBeacon(Collections.max(beacons, (o1, o2) -> Double.compare(o1.getRunningAverageRssi(), o2.getRunningAverageRssi())));
            int minor = nearest.getMinor();
            int major = nearest.getMajor();
            if ((major == 42 || major == 10000) && nearest.isNear()) guide(major, minor);
        } catch (NoSuchElementException ignored) {
        }
    }

    private void guide(int major, int minor) {
        if (!guideFSM.isReady()) {
            int[] way = Routing.getRoute(major, minor);
            if (way != null) guideFSM.setWay(way);
        } else {
            int act = guideFSM.nextMove(minor);
            if (act == GuideFSM.NEXT || act == GuideFSM.STARTING) load(major, minor);
        }
    }

    public void load(int major, int minor) {
        webView.loadUrl(Constants.WEB_ADDRESS + major + "_" + minor + "24");
        if (tts.isEnabled()) speak();
    }

    private void speak() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                webView.evaluateJavascript(TTS_SCRIPT, text -> tts.speak(text));
            }
        });
    }

}
