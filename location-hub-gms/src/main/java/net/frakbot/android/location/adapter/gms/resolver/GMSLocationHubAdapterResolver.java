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
