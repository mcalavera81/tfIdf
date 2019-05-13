package com.example.tfidf

import java.io.File
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardWatchEventKinds
import java.nio.file.WatchService

fun getFiles(directory: String): List<String> =
        File(directory)
                .listFiles()
                .filter { it.isFile }
                .map { it.path }


fun pollDirectory(directory: String, nextUpdate: (Path) -> Unit, error: (Throwable) -> Unit) {
    try {
        val dirPath = Paths.get(directory)
        val watcher = dirPath.watch()

        while (true) {
            //The tfIdfStream blocks until an event is available
            val key = watcher.take()

            //Now go through each event on the folder
            key.pollEvents().forEach { it ->
                //Print output according to the event
                when (it.kind()) {
                    StandardWatchEventKinds.ENTRY_CREATE -> {
                        nextUpdate(dirPath.resolve(it.context() as Path))

                    }
                }
            }
            //Call reset() on the key to watch for future events
            key.reset()
        }
    } catch (t: Throwable) {
        error(t)
    }
}

private fun Path.watch(): WatchService {
    //Create a watch service
    val watchService = this.fileSystem.newWatchService()

    //Register the service, specifying which events to watch
    register(watchService, StandardWatchEventKinds.ENTRY_CREATE)

    return watchService
}


