# evohome Binding

This binding connects your to your Honeywell evohome system, similar to what the offical app does. This means that an internet connection is needed. Currently it is possible to set the global system mode and read the values of setpoints and temperatures of individual zones.



## Supported Things

The binding supports the following things:

* evohome Account
* EvoTouch
* Heating zones

### evohome Account

This thing functions as the bridge between all the other things. It contains your credentials and acts like the gateway to the Honeywell web API. 

### EvoTouch

This thing represents the central display. It is used to display and changes the current system mode.

### Heating zone

The heating zone thing represents the evohome heating zone. It displays the current temperature, the temperature set point and the status of the set point. 

## Discovery

After setting up the evohome account, the evotouch and heating zones available to your account will be discovered after a manual scan.


## Binding Configuration

The evohome account needs to be configured with your username and password. Currently, only configuration through PaperUI is tested.

## Thing Configuration

None

## Channels

TBD

## Full Example

TBD

