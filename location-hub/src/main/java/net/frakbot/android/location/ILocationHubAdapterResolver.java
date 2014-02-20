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
