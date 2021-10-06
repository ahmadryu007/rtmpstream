import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:rtmpstream/rtmpstream.dart';


void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {

  @override
  void initState() {
    super.initState();

    // Rtmpstream.launchStream(url: 'rtmp-test.com');
  }


  @override
  Widget build(BuildContext context) {
    // This is used in the platform side to register the view.
    final String viewType = 'hybrid-view-type';
    // Pass parameters to the platform side.
    final Map<String, dynamic> creationParams = {
      "rtmpUrl" : "rtmp://cocorolife-api-development.inagri.asia:1935/live/PP5S3KCZxQ4l1dtfl9qgEj2E94IRBD3gX56mOUzPrGRh3E9L"
    };

    return AndroidView(
      viewType: viewType,
      layoutDirection: TextDirection.ltr,
      creationParams: creationParams,
      creationParamsCodec: const StandardMessageCodec(),
    );
  }
}
