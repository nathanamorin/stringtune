import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:shared_preferences_web/shared_preferences_web.dart';
import 'tune_page.dart';

import 'instruments.dart';

class HomePage extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    return new _HomePage();
  }
}

class _HomePage extends State<HomePage> {
  String currentQuery = "";
  List<Instrument> instruments = new List();
  List<Instrument> displayedInstruments = new List();
  TextEditingController editingController = TextEditingController();
  static final String savedInstrumentsKey = "saved_instruments";

  void refreshListView() {
    if (currentQuery.isNotEmpty) {
        displayedInstruments.clear();
        instruments.forEach((instrument) {
          if (instrument.name.toLowerCase().contains(currentQuery.toLowerCase())) {
            displayedInstruments.add(instrument);
          }
        });
    } else {
        displayedInstruments.clear();
        displayedInstruments.addAll(instruments);
    }
  }

  List<String> getSavedList(SharedPreferences prefs, String key) {
    List<String> l = prefs.getStringList(key);
    if (l == null) {
      return new List<String>();
    } else {
      return l;
    }
  }

  void filterSearch(String query) {
    setState(() {
      currentQuery = query;
      refreshListView();
    });
  }

  Future<void> storeSavedInstrument(Instrument instrument) async {
    SharedPreferences prefs = await SharedPreferences.getInstance();

    List<String> savedInstruments = getSavedList(prefs, savedInstrumentsKey);
    savedInstruments.add(instrument.getId());

    prefs.setStringList(savedInstrumentsKey, savedInstruments);

    setState(() {
      loadInstruments(savedInstruments);
    });

  }

  Future<void> removeSavedInstrument(Instrument instrument) async {
    SharedPreferences prefs = await SharedPreferences.getInstance();

    List<String> savedInstruments = getSavedList(prefs, savedInstrumentsKey);
    savedInstruments.remove(instrument.getId());

    prefs.setStringList(savedInstrumentsKey, savedInstruments);


    setState(() {
      loadInstruments(savedInstruments);
    });
  }

  @override
  Widget build(BuildContext context) {
    return new Scaffold(
        appBar: new AppBar(
          title: new Text("String Tuner"),
        ),
        body: Column(children: <Widget>[
          Padding(
              padding: const EdgeInsets.all(8.0),
              child: Theme(
                data: Theme.of(context)
                    .copyWith(dialogBackgroundColor: Colors.transparent),
                child: TextField(
                  onChanged: (value) {
                    filterSearch(value);
                  },
                  controller: editingController,
                  decoration: InputDecoration(
                      labelText: "Search",
                      hintText: "Search",
                      prefixIcon: Icon(Icons.search),
                      border: OutlineInputBorder(
                          borderRadius:
                              BorderRadius.all(Radius.circular(25.0))),
                      fillColor: Colors.transparent),
                ),
              )),
          Expanded(
              child: ListView.builder(
                  itemCount: displayedInstruments.length,
                  itemBuilder: (context, rowNum) {
                    Instrument instrument = displayedInstruments[rowNum];

                    return Container(
                      child: Row(children: <Widget>[
                        Expanded(
                            flex: 2,
                            child: InkWell(
                              child: Icon(
                                Icons.star,
                                color: instrument.starred ? Colors.yellow : Colors.white ,
                                size: 50,
                              ),
                              onTap: () {
                                if (instrument.starred) {
                                  removeSavedInstrument(instrument);
                                } else {
                                  storeSavedInstrument(instrument);
                                }
                              },
                            )),
                        Expanded(
                            flex: 8,
                            child: InkWell(
                              child: FittedBox(
                                  fit: BoxFit.fitHeight,
                                  child: Text(instrument.name.toString(),
                                      style: TextStyle(fontSize: 50))),
                              onTap: () {
                                Navigator.push(
                                  context,
                                  MaterialPageRoute(
                                      builder: (context) =>
                                          TunePage(instrument)),
                                );
                              },
                            )),
                      ]),
                      color: Colors.indigoAccent,
                      margin: new EdgeInsets.all(10.0),
                      padding: new EdgeInsets.all(10.0),
                      alignment: Alignment.center,
                    );
                  }))
        ]));
  }

  void loadInstruments(List<String> savedInstruments) {
    instruments.forEach((instrument) {
      instrument.starred = savedInstruments.contains(instrument.getId());
    });

    instruments.sort((a, b) {
      if (a.starred && !b.starred) {
        return -1;
      } else if (!a.starred && b.starred){
        return 1;
      } else {
        return a.name.compareTo(b.name);
      }
    });
    refreshListView();
  }

  @override
  void initState() {
    instruments.addAll(instrumentsStore);

    // Uncomment SharedPreferences & Remove refreshListView after web issue is fixed
//    SharedPreferences.getInstance().then((prefs) {
//      List<String> savedInstruments = getSavedList(prefs, savedInstrumentsKey);
//      loadInstruments(savedInstruments);
//    });
    refreshListView();
    super.initState();
  }
}
