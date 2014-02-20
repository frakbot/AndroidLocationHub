package net.frakbot.android.location.adapter.gms.resolver;

import android.content.Context;

import net.frakbot.android.location.LocationHubAdapter;
import net.frakbot.android.location.adapter.android.AndroidLocationHubAdapter;
import net.frakbot.android.location.adapter.gms.GMSLocationHubAdapter;
import net.frakbot.android.location.resolver.DefaultLocationHubAdapterResolver;

import java.util.List;

/**
 * Default implementation for the {@link net.frakbot.android.location.LocationHubAdapter} interface. It
 * always returns the first available implementation in the list.
 * If you want to use this Adapter implementation, build the {@link java.util.List} of {@link net.frakbot.android.location.LocationHubAdapter}
 * implementations in an ordered way.
 */
public class GMSLocationHubAdapterResolver extends DefaultLocationHubAdapterResolver {
    public GMSLocationHubAdapterResolver(Context context) {
        super(context);
        mLocationAdapters.add(new GMSLocationHubAdapter());
        mLocationAdapters.add(new AndroidLocationHubAdapter());
    }
}
