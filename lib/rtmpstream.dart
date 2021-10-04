
import 'dart:async';

import 'package:flutter/services.dart';

class Rtmpstream {
  static const MethodChannel _channel =
      const MethodChannel('rtmpstream');

  static Future<String?> get platformVersion async {
    final String? version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<void> launchStream({String url = ''}) async {
    await _channel.invokeMethod('launchStream', {'url': url});
  }
}
