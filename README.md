# AndroidLocationHub

Unified API for managing Android Location services in a hassle-free and transparent way.

-----------------

## API

**AndroidLocationHub** lets you use a subset of the Google Play Services location API with any location service/provider/library you want. No more `GooglePlayServicesUtil.isGooglePlayServicesAvailable(Context)` and other tricky stuff to make it work.

### LocationHub

TODO

#### ConnectionCallbacks

TODO

#### OnConnectionFailedListener

TODO

### LocationHubAdapter

TODO

#### AndroidLocationHubAdapter

TODO

#### GMSLocationHubAdapter

TODO

### ILocationHubAdapterResolver

TODO

#### DefaultLocationHubAdapterResolver

TODO

#### GMSLocationHubAdapterResolver

TODO

### LocationHubRequest

TODO

#### LocationHubListener

TODO

## Attributions

The core library, `net.frakbot.android.location` requires no external dependency, but its API is heavily based on the [Google Play Services one](https://developer.android.com/reference/com/google/android/gms/location/LocationClient.html).

The GMS adapter in the `net.frakbot.android.location.adapter.gms` package requires, of course, the Google Play Services library as a Maven dependency. You can use [Maven Android SDK Deployer](https://github.com/mosabua/maven-android-sdk-deployer) to install it on your local Maven repo.

## License

*AndroidLocationHub* is released under the *Apache 2.0* license.

```
Copyright 2014 Frakbot (Francesco Pontillo, Sebastiano Poggi)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```