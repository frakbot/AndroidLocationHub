package net.frakbot.android.location;

import java.util.List;

/**
 * Created by Francesco on 16/02/14.
 */
public interface ILocationHubAdapterResolver {

    /**
     * Returns a {@link java.util.List} of {@link LocationHubAdapter}s.
     * Each element of this collection is a candidate for being resolved and used by the {@link net.frakbot.android.location.LocationHub}.
     *
     * @return A {@link java.util.List} of {@link LocationHubAdapter}s of possible
     * adapter implementations.
     */
    public List<LocationHubAdapter> getAdapterList();

    /**
     * Returns the suggested {@link LocationHubAdapter} implementation instance.
     * This instance will be used by the {@link net.frakbot.android.location.LocationHub} that requested it.
     *
     * @return The chosen {@link LocationHubAdapter} implementation instance.
     */
    public LocationHubAdapter getAdapter();
}
