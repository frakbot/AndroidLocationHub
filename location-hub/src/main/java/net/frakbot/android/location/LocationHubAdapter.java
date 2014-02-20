package net.frakbot.android.location;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import net.frakbot.android.location.common.ConnectionCallbacks;
import net.frakbot.android.location.common.LocationHubListener;
import net.frakbot.android.location.common.LocationHubRequest;
import net.frakbot.android.location.common.OnConnectionFailedListener;

/**
 * TODO: write doc
 */
public abstract class LocationHubAdapter implements ILocationHub {

    public final static String MOCK_PROVIDER = "mockProvider";

    /**
     * Generic method for configuring the particular instance of the {@link LocationHubAdapter}.
     * It can be used to setup, for example, the location accuracy.
     *
     * @param context                  The {@link android.content.Context} the Adapter will work on.
     * @param callbacks                The {@link net.frakbot.android.location.common.ConnectionCallbacks}
     *                                 implementation for receiving connection updates.
     * @param connectionFailedListener The {@link net.frakbot.android.location.common.OnConnectionFailedListener}
     *                                 implementation for receiving connection failure callbacks.
     * @param bundle                   The {@link android.os.Bundle} used for passing data to the Adapter instance.
     */
    protected abstract void setup(Context context, ConnectionCallbacks callbacks, OnConnectionFailedListener connectionFailedListener, Bundle bundle);

    /**
     * Checks if the service the Adapter is built for is actually available.
     * For instance, Google Play Services may not be available on every device.
     *
     * @param context The {@link android.content.Context} the Adapter will work on.
     * @return true if the matching service is available, false otherwise.
     */
    public abstract boolean isServiceAvailable(Context context);

    /**
     * Gets the name of the current {@link net.frakbot.android.location.LocationHubAdapter} implementation.
     * <p/>
     * This should be a significant name, so that it can be used for logging and debugging.
     *
     * @return the adapter implementation name.
     */
    public abstract String getAdapterName();

    /**
     * Returns a {@link java.lang.String} composed by the adapter name, returned by {@link LocationHubAdapter#getAdapterName()}
     * and the regular {@link Object#toString()} call.
     *
     * @return the {@link java.lang.String} representation of the adapter.
     */
    @Override
    public String toString() {
        return getAdapterName() + ": " + super.toString();
    }
}
