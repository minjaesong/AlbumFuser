package net.torvald.albumfuser

import java.io.File
import java.io.FileWriter
import java.nio.charset.Charset

/**
 * Created by minjaesong on 2019-03-19.
 */
class Fuser(val outFormat: String = "wav", val charset: Charset = Charsets.UTF_8) {

    private val musicFormats: Regex = Regex(listOf(
        """flac""",
        """mp3""",
        """wav""",
        """m4a""",
        """ogg""",
        """opus""",
        """tak""",
        """ape"""
    ).reduce { acc, s -> "$acc|$s" })

    fun String.sanitiseQuotes() = this.replace("'", """'\''""")

    fun runAll(rootDir: File) {
        rootDir.listFiles().filter { it.isDirectory }.forEach { runForArtists(rootDir, it) }
    }

    fun runForArtists(rootDir: File, artistDir: File) {
        artistDir.listFiles().filter { it.isDirectory }.forEach { run(artistDir, it) }
        run(artistDir)
    }

    fun run(parentDir: File, singleDir: File) {
        val listFile = makeFileList(singleDir)
        val cmd = "ffmpeg -f concat -safe 0 -y -i \"${listFile.canonicalPath}\" \"${parentDir.canonicalPath} - ${singleDir.name}.$outFormat\""
        println(cmd)
        syscall(cmd)
    }

    fun run(singleDir: File) {
        val listFile = makeFileList(singleDir)
        val cmd = "ffmpeg -f concat -safe 0 -y -i \"${listFile.canonicalPath}\" \"${singleDir.canonicalPath}.$outFormat\""
        println(cmd)
        syscall(cmd)
    }

    private fun makeFileList(singleDir: File): File {
        val outFile = File(singleDir, "filelist.txt")
        val writer = FileWriter(outFile, charset)
        singleDir.listFiles().filter { it.isFile && musicFormats.containsMatchIn(it.extension.toLowerCase()) }.forEach { writer.write("file '${it.canonicalPath.sanitiseQuotes()}'\n") }
        writer.flush()
        writer.close()
        return outFile
    }

    fun syscall(cmd: String) {
        val p = Runtime.getRuntime().exec(cmd)
    }

}