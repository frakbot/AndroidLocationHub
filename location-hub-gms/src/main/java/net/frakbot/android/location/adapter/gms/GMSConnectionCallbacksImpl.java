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

package net.frakbot.android.location.adapter.gms;

import android.os.Bundle;

import com.google.android.gms.common.GooglePlayServicesClient;

import net.frakbot.android.location.common.ConnectionCallbacks;

/**
 * Implementation of {@link com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks}
 * for the {@link net.frakbot.android.location.adapter.gms.GMSLocationHubAdapter}.
 * <p/>
 * It dispatches connection and disconnection events to a custom
 * {@link net.frakbot.android.location.common.ConnectionCallbacks} object.
 */
public class GMSConnectionCallbacksImpl implements GooglePlayServicesClient.ConnectionCallbacks {
    private ConnectionCallbacks mCallbacks;

    public GMSConnectionCallbacksImpl(ConnectionCallbacks callbacks) {
        mCallbacks = callbacks;
    }

    @Override
    public void onConnected(Bundle bundle) {
        mCallbacks.onConnected(bundle);
    }

    @Override
    public void onDisconnected() {
        mCallbacks.onDisconnected();
    }
}
