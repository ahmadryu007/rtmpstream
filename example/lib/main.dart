import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:rtmpstream/rtmpstream.dart';


void main() {
  runApp(MaterialApp(
    title : 'RTMP Test',
    home: MyApp(),
  ));
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
    return Scaffold(
      resizeToAvoidBottomInset: false,
      body: Container(
        width: MediaQuery.of(context).size.width,
        height: MediaQuery.of(context).size.height,
        child: Stack(
            children: <Widget>[
              Container(
                  child: Center(
                    child: AndroidView(
                      viewType: viewType,
                      layoutDirection: TextDirection.ltr,
                      creationParams: creationParams,
                      creationParamsCodec: const StandardMessageCodec(),
                    ),
                  ),
              ),
              Positioned(
                bottom: 0,
                left: 0,
                right: 0,
                child: Container(
                  width: 100,
                  height: 38,
                  decoration: BoxDecoration(
                    borderRadius: BorderRadius.circular(100),
                    color: Color(0xff26282B),
                  ),
                  child: TextFormField(
                    cursorColor: Color(0xffAA3E89),
                    keyboardType: TextInputType.multiline,
                    maxLines: 3,
                    minLines: 1,
                    style: TextStyle(color: Colors.white),
                    decoration: InputDecoration(
                      contentPadding: EdgeInsets.only(
                          left: 12, right: 12, bottom: 10),
                      hintText: "Beri Komentar...",
                      hintStyle:
                      TextStyle(color: Color(0xffB3B3B3)),
                      border: InputBorder.none,
                    ),
                  ),
                ),
              )
            ]
        ),
      ),
    );
  }
}
