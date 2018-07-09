package it.a4smart.vate;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;


/**
 * This fragment contains the page to be shown to the user, and corresponds to a beacon.
 *
 * Having a class to represent every single beacon's page is lets us store an unknown number
 * of beacon in memory, and keep them loaded to increase speed of the app, at the cost of some
 * memory, which in modern device is plenty. Moreover by using the fragment backstack we can
 * mostly ignore the memory consumption, since Android will take care of it.
 */
public class PageFragment extends Fragment{
    private final static String ID_KEY = "ID"; //Key for the parameter when the fragment is created
    private String ID; //ID to identify the beacon
    private WebView webView;


    /**
     * Creates a fragment with the given Beacon ID.
     * @param id Beacon's ID
     * @return The fragment created
     */
    public static PageFragment newInstance (String id) {

        Bundle args = new Bundle();
        args.putString(ID_KEY, id);

        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page, container, false);

        ID = getArguments().getString(ID_KEY);

        webView = view.findViewById(R.id.webView);

        return view;
    }

    public void load() {
        webView.loadUrl(Constants.WEB_ADDRESS+ID);
        webView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                //TODO tell main activity to switch to this fragment
            }
        });
    }

    public String getID () {
        return ID;
    }

}
