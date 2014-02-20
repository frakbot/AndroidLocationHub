package net.frakbot.android.location.common;

import android.os.Bundle;

/**
 * Created by Francesco on 16/02/14.
 */
public interface ConnectionCallbacks {

    /**
     * After calling connect(), this method will be invoked asynchronously when the connect request
     * has successfully completed.
     * After this callback, the application can make requests on other methods provided by the
     * client and expect that no user intervention is required to call methods that use account and
     * scopes provided to the client constructor.
     *
     * @param connectionHint Bundle of data provided to clients. May be null if no content is
     *                       provided by the service.
     */
    public abstract void onConnected(Bundle connectionHint);

    /**
     * Called when the client is disconnected.
     * This can happen if there is a problem with the remote service (e.g. a crash or resource
     * problem causes it to be killed by the system). When called, all requests have been canceled
     * and no outstanding listeners will be executed. Applications should disable UI components
     * that require the service, and wait for a call to onConnected(Bundle) to re-enable them.
     */
    public abstract void onDisconnected();
}
