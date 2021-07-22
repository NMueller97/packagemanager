package de.salocin.device

class NotOnRemoteException(packageName: String, deviceName: String) :
    Exception("The package $packageName is not installed on $deviceName")
