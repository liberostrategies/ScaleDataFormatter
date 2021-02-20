import java.io.File

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

    val file = "data/Scale201801.txt"
    println("Reading file")
    var linesList = File(file).readLines()

    // Remove first line, which contains the file name.
    linesList = linesList.drop(1)

    // Remove user name.
    val userName = "Pinky"
    linesList = linesList.filterNot { it == userName }

    // Remove empty lines.
    linesList = linesList.filterNot { it.isEmpty() }

//    dumpFileLines(linesList)

    // Collect a day's measurement into array.
    val multiDayData = collectMeasurementsByDays(linesList)
    println("Multi-Day Data=$multiDayData")


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
        println("$line:($i)\t$m")

//        if (i < SIZE_OLD_DATA_MEASUREMENTS) {
        if (i in 0..SIZE_OLD_DATA_MEASUREMENTS) {
            // Current day's data. Keep collecting into same array index.
            println("\ti=$i, m=$m")
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

fun formatTimestamp() {

}
fun dumpFileLines(aLinesList: List<String>) {
    aLinesList.forEachIndexed { index, s ->
        println("line=$index : $s")
    }
}