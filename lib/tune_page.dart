import 'dart:typed_data';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:soundpool/soundpool.dart';
import 'package:wave_generator/wave_generator.dart';

import 'instruments.dart';

Map<String,String> unicodeSubscriptMap = {
  '0'        : '\u2080',
  '1'        : '\u2081',
  '2'        : '\u2082',
  '3'        : '\u2083',
  '4'        : '\u2084',
  '5'        : '\u2085',
  '6'        : '\u2086',
  '7'        : '\u2087',
  '8'        : '\u2088',
  '9'        : '\u2089',
};

Future<void> playTone(note) async {
  Soundpool pool = Soundpool(streamType: StreamType.notification);

  int soundId = await rootBundle
      .load("data/tones/" + note + ".wav")
      .then((ByteData soundData) {
    return pool.load(soundData);
  });
  int streamId = await pool.play(soundId);
}

class TunePage extends StatelessWidget {
  final Instrument instrument;
  TunePage(this.instrument);

  @override
  Widget build(BuildContext context) {
    List<String> notesSplit = instrument.notes.split(":");
    return new Scaffold(
      appBar: new AppBar(
        title: new Text("Tune " + instrument.name),
      ),
      body: GridView.count(
          crossAxisCount: 2,
          children: List.generate(notesSplit.length, (i) {
            return InkWell(
              onTap: () {
                playTone(notesSplit[i]).then((val) {
                  print("finished tone");
                });
              },
              child: new Container(
                child: FittedBox(
                  fit: BoxFit.fitHeight,
                  child: RichText(
                    text: TextSpan(
                      children: [
                        TextSpan(text: notesSplit[i][0], style: TextStyle(fontSize: 500, color: Colors.white)),
                        TextSpan(text: unicodeSubscriptMap[notesSplit[i][1]], style: TextStyle(fontSize: 500, color: Colors.black))
                      ]
                    )
                  ),
                ),
                color: Colors.indigoAccent,
                alignment: Alignment.center,
                margin: EdgeInsets.all(10.0),
                padding: EdgeInsets.all(10.0),
              ),
              enableFeedback: false,
            );
          })),
    );
  }
}


































