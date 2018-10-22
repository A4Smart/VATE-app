package it.a4smart.vate.proximity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import it.a4smart.vate.R;
import it.a4smart.vate.common.Constants;
import it.a4smart.vate.common.VBeacon;


/**
 * This fragment contains the page to be shown to the user, and corresponds to a beacon.
 *
 * Having a class to represent every single beacon's page lets us store an unknown number
 * of beacon in memory, and keep them loaded to increase speed of the app, at the cost of some
 * memory, which in modern device is plenty. Moreover by using the fragment backstack we can
 * mostly ignore the memory consumption, since Android will take care of it.
 */
public class SingleProximityFragment extends Fragment {
    private final static String TAG = "SingleProximityFragment";
    private final static String ID_KEY = "ID"; //Key for the parameter when the fragment is created
    private String ID; //ID to identify the beacon
    private WebView webView;
    private String tts_text;


    /**
     * Creates a fragment with the given Beacon.
     * @param beacon Beacon
     * @return The fragment created
     */
    public static SingleProximityFragment newInstance(VBeacon beacon) {

        Bundle args = new Bundle();
        args.putString(ID_KEY, beacon.getID());

        SingleProximityFragment fragment = new SingleProximityFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ID == null) ID = getArguments().getString(ID_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page, container, false);
        webView = view.findViewById(R.id.proximityWV);

        load();

        return view;
    }

    public void load() {

        webView.getSettings().setJavaScriptEnabled(true);

        webView.loadUrl(Constants.WEB_ADDRESS+ID);
        webView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                webView.evaluateJavascript(
                        "(function() { return document.getElementById('tts_text').innerHTML; })();",
                        text -> tts_text = text);
            }
        });
    }

    public String getID () {
        return (ID == null) ? ID = getArguments().getString(ID_KEY) : ID;
    }

    public String getTTSText() {
        return tts_text;
    }
}
