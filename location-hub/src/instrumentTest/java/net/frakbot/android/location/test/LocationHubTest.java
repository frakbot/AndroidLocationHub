package net.frakbot.android.location.test;

import android.location.Location;
import android.os.Bundle;
import android.test.InstrumentationTestCase;

import net.frakbot.android.location.LocationHub;
import net.frakbot.android.location.LocationHubAdapter;
import net.frakbot.android.location.common.ConnectionCallbacks;
import net.frakbot.android.location.common.ConnectionResult;
import net.frakbot.android.location.common.LocationHubListener;
import net.frakbot.android.location.common.LocationHubRequest;
import net.frakbot.android.location.common.OnConnectionFailedListener;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Instrumentation test for the {@link net.frakbot.android.location.LocationHub} library.
 */
public class LocationHubTest extends InstrumentationTestCase {
    LocationHub mLocationHub;

    ConnectionCallbacks mConnectionCallbacks;
    CountDownLatch connectionLatch;
    CountDownLatch disconnectionLatch;

    OnConnectionFailedListener mOnConnectionFailedListener;

    /**
     * On setup, instantiate the {@link net.frakbot.android.location.LocationHub}.
     *
     * @throws Exception
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mConnectionCallbacks = new ConnectionCallbacks() {
            @Override
            public void onConnected(Bundle connectionHint) {
                connectionLatch.countDown();
            }

            @Override
            public void onDisconnected() {
                disconnectionLatch.countDown();
            }
        };
        mOnConnectionFailedListener = new OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(ConnectionResult result) {
            }
        };
        mLocationHub = new LocationHub(getInstrumentation().getContext(), mConnectionCallbacks, mOnConnectionFailedListener);

        internalTestConnection();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        internalTestDisconnection();
    }

    /**
     * Simply checks that the {@link net.frakbot.android.location.LocationHub} is instantiated.
     */
    public void testLocationHub() {
        assertNotNull("The LocationHub should be instantiated.", mLocationHub);
    }

    /**
     * Tests the connection to the {@link net.frakbot.android.location.LocationHub} has been made,
     * so we are sure that it is ready.
     *
     * @throws InterruptedException if the timeout set for the connection expires.
     */
    public void testConnected() throws InterruptedException {
        assertTrue("The LocationHub should be connected", mLocationHub.isConnected());
    }

    /**
     * Test that {@link net.frakbot.android.location.LocationHub#requestLocationUpdates(net.frakbot.android.location.common.LocationHubRequest, net.frakbot.android.location.common.LocationHubListener)}
     * completes within 60 seconds with the highest accuracy level.
     */
    public void testGetLocation() throws InterruptedException {
        final CountDownLatch locationLatch = new CountDownLatch(1);
        final Location[] latestLocation = new Location[1];
        mLocationHub.setMockMode(true);
        mLocationHub.requestLocationUpdates(
                new LocationHubRequest()
                        .setPriority(LocationHubRequest.PRIORITY_HIGH_ACCURACY)
                        .setInterval(6000),
                new LocationHubListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        locationLatch.countDown();
                        latestLocation[0] = location;
                    }
                });
        mLocationHub.setMockLocation(getMockLocation());
        locationLatch.await(10, TimeUnit.SECONDS);
        assertNotNull("A location should be retrieved within 10 seconds.", latestLocation[0]);
    }

    /**
     * Internal method, use this to test the connection to the {@link net.frakbot.android.location.LocationHub}.
     *
     * @throws InterruptedException if the timeout for connection expires.
     */
    private void internalTestConnection() throws InterruptedException {
        // Try to connect
        connectionLatch = new CountDownLatch(1);
        mLocationHub.connect();
        boolean isConnected = connectionLatch.await(10, TimeUnit.SECONDS);
        String shouldBeConnected = "Connection should be established within 10 seconds.";
        assertTrue(shouldBeConnected, isConnected);
        assertTrue(shouldBeConnected, mLocationHub.isConnected());
    }

    /**
     * Internal method, use this to test the disconnection from the {@link net.frakbot.android.location.LocationHub}.
     *
     * @throws InterruptedException if the timeout for disconnection expires.
     */
    private void internalTestDisconnection() throws InterruptedException {
        // Try to disconnect
        disconnectionLatch = new CountDownLatch(1);
        mLocationHub.disconnect();
        boolean isDisconnected = disconnectionLatch.await(10, TimeUnit.SECONDS);
        String shouldBeDisconnected = "Connection should be closed within 10 seconds.";
        assertTrue(shouldBeDisconnected, isDisconnected);
        assertFalse(shouldBeDisconnected, mLocationHub.isConnected());
    }

    /**
     * Returns a (0,0,0) mock location.
     *
     * @return A {@link android.location.Location} used for testing.
     */
    private Location getMockLocation() {
        Location mockLocation = new Location(LocationHubAdapter.MOCK_PROVIDER);
        mockLocation.setLongitude(0);
        mockLocation.setLatitude(0);
        mockLocation.setAltitude(0);
        return mockLocation;
    }
}
