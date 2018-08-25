package com.nathanmorin.stringtuner

import kotlin.math.pow


val steps = mapOf(
        "C" to 0,
        "C#" to 1,
        "Db" to 1,
        "D" to 2,
        "D#" to 3,
        "Eb" to 3,
        "E" to 4,
        "F" to 5,
        "F#" to 6,
        "Gb" to 6,
        "G" to 7,
        "G#" to 8,
        "Ab" to 8,
        "A" to 9,
        "A#" to 10,
        "Bb" to 10,
        "B" to 11
)

val baseNote = "A"
val baseOct = 4
val baseFreq = 440
val a = (2.0).pow(1.0/12.0)

/**
 * Format of note is [Note][Octive][#/b].  For example C4#, or B3b
 */
fun convertNoteToFrequency(note: String) : Double {

    val octive = note[1].toString().toInt()

    val relNote = note[0].toString() + (if (note.length > 2) note[2] else "")

    if (!steps.containsKey(relNote)){
        throw InvalidNote("Invalid Note $note")
    }

    val octDelta = (octive - baseOct) * 12
    val noteDelta = steps[relNote]!! - steps[baseNote]!!

    return baseFreq * a.pow(octDelta + noteDelta)
}