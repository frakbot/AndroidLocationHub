package net.frakbot.android.location.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by fpontillo on 17/02/14.
 */
public class LocationHubRequest {

    /**
     * Used with {@link LocationHubRequest#setPriority(int)} to request the best accuracy possible with zero additional power consumption.
     * No locations will be returned unless a different client has requested location updates in which case this request will act as a passive listener to those locations.
     */
    public static final int PRIORITY_NO_POWER = 105;

    /**
     * Used with {@link LocationHubRequest#setPriority(int)} to request "city" level accuracy.
     * City level accuracy is considered to be about 10km accuracy. Using a coarse accuracy such as this often consumes less power.
     */
    public static final int PRIORITY_LOW_POWER = 104;

    /**
     * Used with {@link LocationHubRequest#setPriority(int)} to request "block" level accuracy.
     * Block level accuracy is considered to be about 100 meter accuracy. Using a coarse accuracy such as this often consumes less power.
     */
    public static final int PRIORITY_BALANCED_POWER_ACCURACY = 102;

    /**
     * Used with {@link LocationHubRequest#setPriority(int)} to request the most accurate locations available.
     * This will return the finest location available.
     */
    public static final int PRIORITY_HIGH_ACCURACY = 100;

    /**
     * List of allowed priorities.
     */
    private static final List<Integer> PRIORITIES_ALLOWED = new ArrayList<Integer>(
            Arrays.asList(new Integer[] {
                    PRIORITY_BALANCED_POWER_ACCURACY, PRIORITY_HIGH_ACCURACY, PRIORITY_LOW_POWER, PRIORITY_NO_POWER
            }));

    private int mPriority;
    private long mFastestInterval;
    private long mInterval;
    private float mSmallestDisplacement = 0;

    /**
     * Get the quality of the request.
     *
     * @return an accuracy constant.
     */
    public int getPriority() {
        return mPriority;
    }

    /**
     * Set the priority of the request.
     * Use with a priority constant such as {@link LocationHubRequest#PRIORITY_HIGH_ACCURACY}. No other values are accepted.
     * The priority of the request is a strong hint for which location sources to use.
     * For example, {@link LocationHubRequest#PRIORITY_HIGH_ACCURACY} is more likely to use GPS,
     * and {@link LocationHubRequest#PRIORITY_BALANCED_POWER_ACCURACY} is more likely to use WIFI & Cell tower positioning,
     * but it also depends on many other factors (such as which sources are available) and is implementation dependent.
     *
     * @param priority an accuracy or power constant.
     * @return the same object, so that setters can be chained.
     */
    public LocationHubRequest setPriority(int priority) throws IllegalArgumentException {
        if (!PRIORITIES_ALLOWED.contains(priority)) {
            throw new IllegalArgumentException(String.format("Priority of %d is not an accepted value.", priority));
        } else {
            mPriority = priority;
        }
        return this;
    }

    /**
     * Get the fastest interval of this request, in milliseconds.
     * The system will never provide location updates faster than the minimum of {@link LocationHubRequest#getFastestInterval()} and {@link LocationHubRequest#getInterval()}.
     *
     * @return fastest interval in milliseconds, exact
     */
    public long getFastestInterval() {
        return mFastestInterval;
    }

    /**
     * Explicitly set the fastest interval for location updates, in milliseconds.
     * <p/>
     * This controls the fastest rate at which your application will receive location updates,
     * which might be faster than {@link LocationHubRequest#setInterval(long)}
     * in some situations (for example, if other applications are triggering location updates).
     * <p/>
     * This allows your application to passively acquire locations at a rate faster than it actively
     * acquires locations, saving power.
     * <p/>
     * Unlike {@link LocationHubRequest#setInterval(long)}, this
     * parameter is exact. Your application will never receive updates faster than this value.
     * <p/>
     * If you don't call this method, a fastest interval will be selected for you. It will be a
     * value faster than your active interval ({@link LocationHubRequest#setInterval(long)} ).
     * <p/>
     * An interval of 0 is allowed, but not recommended, since location updates may be extremely fast on future implementations.
     * <p/>
     * If {@link LocationHubRequest#getFastestInterval()} (long)}  is set slower than {@link LocationHubRequest#setInterval(long)},
     * then your effective fastest interval is {@link LocationHubRequest#setInterval(long)}.
     *
     * @param millis fastest interval for updates in milliseconds, exact.
     * @return the same object, so that setters can be chained.
     * @throws IllegalArgumentException if the interval is less than zero.
     */
    public LocationHubRequest setFastestInterval(long millis) throws IllegalArgumentException {
        if (millis < 0) {
            throw new IllegalArgumentException("Fastest interval cannot be less than 0.");
        }
        mFastestInterval = millis;
        return this;
    }

    /**
     * Get the desired interval of this request, in milliseconds.
     *
     * @return desired interval in milliseconds, inexact
     */
    public long getInterval() {
        return mInterval;
    }

    /**
     * Set the desired interval for active location updates, in milliseconds.
     * <p/>
     * The location client will actively try to obtain location updates for your application at this
     * interval, so it has a direct influence on the amount of power used by your application.
     * Choose your interval wisely.
     * <p/>
     * This interval is inexact. You may not receive updates at all (if no location sources are available),
     * or you may receive them slower than requested. You may also receive them faster than requested
     * (if other applications are requesting location at a faster interval).
     * The fastest rate that that you will receive updates can be controlled with
     * {@link LocationHubRequest#setFastestInterval(long)}.
     * By default this fastest rate is 6x the interval frequency.
     * <p/>
     * Applications with only the coarse location permission may have their interval silently throttled.
     * <p/>
     * An interval of 0 is allowed, but not recommended, since location updates may be extremely fast on future implementations.
     * <p/>
     * {@link LocationHubRequest#setPriority(int)} and
     * {@link LocationHubRequest#setInterval(long)} are the most
     * important parameters on a location request.
     *
     * @param millis desired interval in millisecond, inexact.
     * @return the same object, so that setters can be chained.
     * @throws IllegalArgumentException if the interval is less than zero.
     */
    public LocationHubRequest setInterval(long millis) throws IllegalArgumentException {
        if (millis < 0) {
            throw new IllegalArgumentException("Interval cannot be less than 0.");
        }
        mInterval = millis;
        return this;
    }

    /**
     * Get the minimum displacement between location updates in meters.
     * <p/>
     * By default this is 0.
     *
     * @return minimum displacement between location updates in meters.
     */
    public float getSmallestDisplacement() {
        return mSmallestDisplacement;
    }

    /**
     * Set the minimum displacement between location updates in meters.
     * <p/>
     * By default this is 0.
     *
     * @param smallestDisplacementMeters the smallest displacement in meters the user must move between location updates.
     * @return the same object, so that setters can be chained.
     * @throws IllegalArgumentException if smallestDisplacementMeters is negative.
     */
    public LocationHubRequest setSmallestDisplacement(float smallestDisplacementMeters) throws IllegalArgumentException {
        if (smallestDisplacementMeters < 0) {
            throw new IllegalArgumentException("Smallest displacement cannot be less than 0.");
        }
        mSmallestDisplacement = smallestDisplacementMeters;
        return this;
    }
}
