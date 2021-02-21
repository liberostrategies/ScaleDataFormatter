import java.io.File
import java.io.FileWriter

const val OUTFILE_DATA_FILENAME = "datafile.csv"
const val DATE_START = 201801
const val DATE_END = 202012 // Inclusive.

const val SIZE_OLD_DATA_MEASUREMENTS = 9
const val IDX_TIMESTAMP = 0
const val IDX_WEIGHT = 1
const val IDX_BODY_WATER = 2
const val IDX_BODY_FAT = 3
const val IDX_BONE = 4
const val IDX_BMI = 5
const val IDX_VISCERAL_FAT = 6
const val IDX_BMR = 7
const val IDX_MUSCLE_MASS = 8

fun main(args: Array<String>) {
    var dateCurr = -1
    var filename = "data/ScaleTBD.txt"

    // Delete any previous output files as a result of running this program.
    val fileCheck = File(OUTFILE_DATA_FILENAME)
    if (fileCheck.exists()) {
        fileCheck.delete()
    }

    // Add the header row.
    var fileWriter = FileWriter(OUTFILE_DATA_FILENAME)
    fileWriter.write(DayData.Companion.headerToCsv())

    // Keep reading input data files within the date range.
    while ((dateCurr != 0) && (dateCurr < DATE_END)) {
        dateCurr = nextDataFile(dateCurr)
        filename = "data/Scale$dateCurr.txt"

        println("Reading filename=$filename")
        // Check that input data files exists, before reading it.
        val f = File(filename)
        if (f.exists()) {
            f.readLines()
            var linesList = File(filename).readLines()

            // Remove first line, which contains the file name.
            linesList = linesList.drop(1)

            // Remove user name.
            val userName = "Pinky"
            linesList = linesList.filterNot { it == userName }

            // Remove empty lines.
            linesList = linesList.filterNot { it.isEmpty() }

            // Collect a day's measurement into array.
            val multiDayData = collectMeasurementsByDays(linesList)
            writeDataToFile(multiDayData, fileWriter)
        } // Input data file exists, so process it.
        else {
            println("$filename does not exist. Skipping it.")
        }
    }
    fileWriter.close()

    println("No more input data files. Ending program.")
    return
}

/**
 * Get next month data file within the range Scale201801.txt through Scale202012.txt.
 * @param dateLast Date of last processed data file.
 *                 -1, if initial range.
 * @return Date of data file in range. Format = YYYYMM.
 *         0, if exceeded end range.
 */
private fun nextDataFile(dateLast: Int): Int {
    val monthLast = if (dateLast > -1) dateLast.toString().substring(4).toInt() else -1
    val yearLast = if (dateLast > -1) dateLast.toString().substring(0, 4).toInt() else -1

    return when (dateLast) {
        -1 -> DATE_START                            // Start range.
        in DATE_START .. DATE_END -> {              // Within range.
            if (monthLast == 12) {
                // Reset to next year and Jan (1).
                ((yearLast + 1) * 100) + 1
            } else {
                dateLast + 1
            }
        }
        else -> 0                                   // Exceeded range.
    }
}

/**
 * Divide the raw data file into arrays containing data for one day.
 * Nine types of data is in one set of measurements.
 * @param theMeasurements Raw data file with measurements over multiple days.
 * @return Array of array data for each day from the multi-day data file.
 */
fun collectMeasurementsByDays(theMeasurements: List<String>): List<DayData> {
    var list: MutableList<DayData> = mutableListOf<DayData>()

    var timestamp = "TBDtimeStamp"
    var weight = "TBDweight"
    var bodyWater = "TBDbodyWater"
    var bodyFat = "TBDbodyFat"
    var bone = "TBDbone"
    var bmi = "TBDbmi" // TODO: Interpret to 0.0F
    var visceralFat = "TBDvisceralFat" // TODO: Interpret to 0.0F
    var bmr = "TBDbmr"
    var muscleMass = "TBDmuscleMass"

    var i = 0
    for ((line, m) in theMeasurements.withIndex()) {
//        println("$line:($i)\t$m")

        if (i in 0..SIZE_OLD_DATA_MEASUREMENTS) {
            // Current day's data. Keep collecting into same array index.
//            println("\ti=$i, m=$m")                                         // String templates.
            when (i) {
                IDX_TIMESTAMP -> timestamp = m
                IDX_WEIGHT -> weight = m
                IDX_BODY_WATER -> bodyWater = m
                IDX_BODY_FAT -> bodyFat = m
                IDX_BONE -> bone = m
                IDX_BMI -> bmi = m
                IDX_VISCERAL_FAT -> visceralFat = m
                IDX_BMR -> bmr = m
                IDX_MUSCLE_MASS -> muscleMass = m
            }
        }
        i++

        if (i >= SIZE_OLD_DATA_MEASUREMENTS) {
            // New day's data. Reset divider index. Create DayData object.
            i = 0
            list.add(DayData(timestamp, weight, bodyWater, bodyFat, bone, bmi, visceralFat, bmr, muscleMass))
        }
    }

    return list
}

/**
 * Write formatted data to file.
 * @param dataForFile
 */
fun writeDataToFile(dataForFile: List<DayData>, aFileWriter: FileWriter) {
    dataForFile.forEach {
        val s = it.toCsv()
        aFileWriter.write(it.toCsv())
    }
}

/**
 * For debug of file.
 * @param aLinesList Lines of file.
 */
fun dumpFileLines(aLinesList: List<String>) {
    aLinesList.forEachIndexed { index, s ->
        println("line=$index : $s")
    }
}