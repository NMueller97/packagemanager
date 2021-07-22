package de.salocin

object Config {

    const val workingDirPath = "."
    const val adb = "adb"
    const val keytool = "keytool"
    const val apksigner = "apksigner"
    const val keystore = "${workingDirPath}keystore.keystore"
    const val keystorePass = "123456"
    const val keystoreCertAlias = "keystore"
}
