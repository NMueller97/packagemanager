package de.salocin.android

import com.google.common.jimfs.Configuration
import com.google.common.jimfs.Jimfs
import java.nio.file.FileSystem

val AndroidFileSystem: FileSystem = Jimfs.newFileSystem(Configuration.unix())
