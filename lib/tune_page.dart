
import 'dart:typed_data';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:soundpool/soundpool.dart';
import 'package:wave_generator/wave_generator.dart';


Future<void> playTone(note) async {

    Soundpool pool = Soundpool(streamType: StreamType.notification);

    int soundId = await rootBundle.load("data/tones/" + note + ".wav").then((ByteData soundData) {
      return pool.load(soundData);
    });
    int streamId = await pool.play(soundId);
}

class TunePage extends StatelessWidget {
  final String notes;
  TunePage(this.notes);

  @override
  Widget build(BuildContext context) {

    List<String> notesSplit = notes.split(":");
    return new Scaffold(
      appBar: new AppBar(
        title: new Text("String Tuner"),),
      body: GridView.count(crossAxisCount: 2,
          children: List.generate(notesSplit.length, (i) {
              return FlatButton(
                  onPressed: () {
                    playTone(notesSplit[i]).then((val) {
                      print("finished tone");
                    });
                  },
                  child: new Container(
                      child: new Text(notesSplit[i]),
                      color: Colors.teal,
                      alignment: Alignment.center,
                      margin: EdgeInsets.all(10.0),
                      padding: EdgeInsets.all(10.0),
                    )
              );
          })),
    );
  }
}