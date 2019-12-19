import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:untitled/tune_page.dart';

import 'instruments.dart';

class HomePage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text("String Tuner"),),
      body: FutureBuilder<String>(
        future: loadCSV(),
        builder: (context, snapshot) {
          if (snapshot.hasData) {
            List<Instrument> instruments = parseCSV(snapshot.data);
            return new ListView.builder(
                itemCount: instruments.length,
                itemBuilder: (context, rowNum) {
                  return InkWell(
                    child: new Container(
                      child: new Text(
                          instruments[rowNum].name.toString()),
                      color: Colors.indigoAccent,
                      margin: new EdgeInsets.all(10.0),
                      padding: new EdgeInsets.all(10.0),
                      alignment: Alignment.center,
                    ),
                    onTap: () {
                      Navigator.push(
                        context,
                        MaterialPageRoute(builder: (context) => TunePage(instruments[rowNum].notes)),
                      );
                    },
                  );
                });
          } else {
            return Text("Loading");
          }
        },
      ),
    );
  }
}
