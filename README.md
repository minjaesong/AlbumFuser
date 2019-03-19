Creates one solid files from album directories.

### How it works

First of all, you must prepare directories such that:

```
ROOT/ArtistName/AlbumName/music.*
ROOT/AlbumName/music.*
```

Then you select the ROOT directory on the app, hit the GO! button, and concatenated files are generated. Naming rule is as follows:

```
ROOT/ArtistName - AlbumName.wav
ROOT/AlbumName.wav
```

File extension is WAV by default, you can change it.

This app spawns FFmpeg processes when you make conversion. If you have 100 directories, it will spawn 100 processes.

### System Requirements

* FFmpeg installed
* Preferably a computer with high core count
* Also preferably a computer with fast NVMe storage

### Tips for Operation

* Always open Task Manager
* Windows: If it seems that the conversion is halted, but not quite done, try quitting this app (normal exit operation, NOT force quit). The remaining process will attach to something else (this process re-allocation will temporarily freeze the whole system), after that the operation should resume (always look at the Task Manager). This is possible because the app is just a remote controller for FFmpeg.
