package net.frakbot.android.location.demo.fragment;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.frakbot.android.location.LocationHub;
import net.frakbot.android.location.adapter.gms.resolver.GMSLocationHubAdapterResolver;
import net.frakbot.android.location.common.ConnectionCallbacks;
import net.frakbot.android.location.common.ConnectionResult;
import net.frakbot.android.location.common.LocationHubListener;
import net.frakbot.android.location.common.LocationHubRequest;
import net.frakbot.android.location.common.OnConnectionFailedListener;
import net.frakbot.android.location.demo.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private TextView mAdapterStatusTextView;
    private TextView mLocationTextView;
    private TextView mAdapterTextView;

    private LocationHub mLocationHub;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        if (rootView != null) {
            mAdapterStatusTextView = (TextView) rootView.findViewById(R.id.adapterStatusTextView);
            mLocationTextView = (TextView) rootView.findViewById(R.id.locationTextView);
            mAdapterTextView = (TextView) rootView.findViewById(R.id.adapterTextView);
        }

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mLocationHub = new LocationHub(getActivity(),
                new ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle connectionHint) {
                        whenConnected();
                    }

                    @Override
                    public void onDisconnected() {
                        whenDisconnected();
                    }
                },
                new OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult result) {
                        whenFailed();
                    }
                }
                // ,new GMSLocationHubAdapterResolver(getActivity())
        );
        mLocationHub.connect(null);
        mLocationHub.setMockMode(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        mAdapterStatusTextView.setText(R.string.disconnecting);
        mLocationHub.disconnect();
    }

    private void whenConnected() {
        mAdapterStatusTextView.setText(R.string.connected);
        mAdapterTextView.setText(mLocationHub.getAdapterImpl().getAdapterName());
        whenLocation(mLocationHub.getLastLocation());
        mLocationHub.requestLocationUpdates(
                new LocationHubRequest().setPriority(LocationHubRequest.PRIORITY_BALANCED_POWER_ACCURACY).setInterval(6000),
                new LocationHubListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        whenLocation(location);
                    }
                });
    }

    private void whenDisconnected() {
        mAdapterStatusTextView.setText(R.string.disconnected);
    }

    private void whenFailed() {
        mAdapterStatusTextView.setText(R.string.failed);
    }

    private void whenLocation(Location location) {
        if (location != null) {
            mLocationTextView.setText(location.toString());
        }
    }
}
