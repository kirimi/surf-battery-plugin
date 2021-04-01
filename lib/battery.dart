import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

typedef OnBatteryChange = Function(int);

/// Плагин для доступа к уровню заряда устройства
///
/// В конструктор передается калбек [onBatteryChange], который будет
/// вызван при изменении уровня заряда.
class Battery {
  Battery({@required this.onBatteryChange}) : assert(onBatteryChange != null) {
    _init();
  }

  static const MethodChannel _channel = const MethodChannel('battery');

  static const String _getBatteryMethod = 'get_battery';

  static const String _changeBatteryMethod = 'change_battery';

  final OnBatteryChange onBatteryChange;

  void _init() {
    _channel.setMethodCallHandler((call) async {
      switch (call.method) {
        case _changeBatteryMethod:
          onBatteryChange(call.arguments);
      }
    });
  }

  /// Получить текущий уровень заряда устройства в процентах
  Future<int> getBatteryLevel() async {
    return await _channel.invokeMethod(_getBatteryMethod);
  }
}
