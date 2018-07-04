import pandas as pd
import re


#
#  Parse Tuning Data
#


def parse_strings(strings):
    m = re.findall("([A-Z][0-9]+)", strings)
    if m:
        return ":".join(m)
    else:
        return None


def parse_row(row):
    row_str = bytes(str(row).strip("\t\n").encode('ascii', errors='ignore')).decode('utf-8')
    pattern = "[A-Za-z0-9#/: ]"
    # row_str = ''.join([s for s in row_str if re.match(pattern, s)])

    matcher = re.match("([A-Za-z0-9/\(\) ]*): ([A-Z0-9 ]*) ", row_str)
    standard_tuning = None
    if matcher:
        groups = matcher.groups()

        standard_tuning = parse_strings(groups[1])
    else:
        if re.match("^([A-Z0-9# ]*)$", row_str):
            standard_tuning = parse_strings(row_str)
        else:
            print(row_str)

    return standard_tuning




df = pd.read_csv("../instrument_tunings.csv")

df["StandardTuning"] = df["Tuning(s)"].apply(parse_row)

df.to_csv("../instrument_tunings_parsed.csv")



#
#  Parse Tune Frequencies
#

