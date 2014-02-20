/*
 * Copyright 2014 Frakbot (Francesco Pontillo, Sebastiano Poggi)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
