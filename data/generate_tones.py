from synthesizer import Synthesizer, Writer, Waveform
import pandas as pd
from scipy.interpolate import make_interp_spline


def get_sound_leveler(level):
    df = pd.read_csv("Equal Loudness Contours.csv")

    return make_interp_spline(df['frequency'], df[level], k=3)  # type: BSpline


def write_chord(f, note_freq, sound_leveler):
    writer = Writer()
    synthesizer = Synthesizer(osc1_waveform=Waveform.sine, osc1_volume=sound_leveler(note_freq), use_osc2=False)
    wave = synthesizer.generate_chord([note_freq], 3.0)
    writer.write_wave(f, wave)


def read_notes():
    with open("frequencies.csv") as f:
        return [(x[0] + x[1], float(x[3])) for x in [l.strip("\n").split(",") for l in f.readlines()[1:]]]


def main():
    sound_leveler = get_sound_leveler(level="100")
    for note in read_notes():
        f = "tones/{note}.wav".format(note=note[0])
        write_chord(f, note_freq=note[1], sound_leveler=lambda x: 1.0)
        print("Wrote file: " + f)


if __name__ == "__main__":
    main()
