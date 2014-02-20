package net.frakbot.android.location.resolver;

import android.content.Context;

import net.frakbot.android.location.ILocationHubAdapterResolver;
import net.frakbot.android.location.LocationHubAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Default implementation for the {@link net.frakbot.android.location.LocationHubAdapter} interface. It
 * always returns the first available implementation in the list.
 * If you want to use this Adapter implementation, build the {@link java.util.List} of {@link net.frakbot.android.location.LocationHubAdapter}
 * implementations in an ordered way.
 */
public class DefaultLocationHubAdapterResolver implements ILocationHubAdapterResolver {
    protected List<LocationHubAdapter> mLocationAdapters;
    private Context mContext;

    public DefaultLocationHubAdapterResolver(Context context) {
        mLocationAdapters = new ArrayList<LocationHubAdapter>();
        mContext = context;
    }

    @Override
    public List<LocationHubAdapter> getAdapterList() {
        return mLocationAdapters;
    }

    @Override
    public LocationHubAdapter getAdapter() {
        for (LocationHubAdapter adapter : mLocationAdapters) {
            if (adapter.isServiceAvailable(mContext)) {
                return adapter;
            }
        }
        return null;
    }
}
