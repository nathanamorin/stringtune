import 'dart:convert';

import 'dart:async' show Future;
import 'package:csv/csv.dart';
import 'package:flutter/services.dart' show rootBundle;
Future<String> loadCSV() async {
  return await rootBundle.loadString('data/instrument_tunings_parsed.csv');
}

List<Instrument> parseCSV(String csvStr) {
  List<List<dynamic>> rowsAsListOfValues = const CsvToListConverter().convert(csvStr);
  return rowsAsListOfValues.map((line) {
    return new Instrument(line[1], line[4]);
  }).toList();
}

class Instrument {
  Instrument(String name, String notes) {
    this.name = name;
    this.notes = notes;
  }
  String name;
  String notes;
}