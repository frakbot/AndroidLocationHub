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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;

import net.frakbot.android.location.common.OnConnectionFailedListener;

/**
 * Implementation of {@link com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener}
 * for the {@link net.frakbot.android.location.adapter.gms.GMSLocationHubAdapter}.
 * <p/>
 * It dispatches connection and disconnection events to a custom
 * {@link net.frakbot.android.location.common.OnConnectionFailedListener} object.
 */
public class GMSOnConnectionFailedListenerImpl implements GooglePlayServicesClient.OnConnectionFailedListener {

    private OnConnectionFailedListener mListener;

    public GMSOnConnectionFailedListenerImpl(OnConnectionFailedListener listener) {
        mListener = listener;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // TODO: don't handle connection results for now
        mListener.onConnectionFailed(null);
    }
}
