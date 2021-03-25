import 'package:battery/battery.dart';
import 'package:flutter/material.dart';

void main() {
  runApp(MyApp());
}

/// Пример использования плагина получения уровня заряда батареи
class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  int _batteryLevel = 0;
  Battery batteryManager;

  @override
  void initState() {
    super.initState();
    batteryManager = Battery(onBatteryChange: _onBatteryChange);
  }

  /// Вызывается при получении уровня заряда батареи с платформы
  void _onBatteryChange(int value) {
    setState(() {
      _batteryLevel = value;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              Text(
                'Battery level: $_batteryLevel',
                style: Theme.of(context).textTheme.headline3,
              ),
              ElevatedButton(
                onPressed: () => batteryManager.getBatteryLevel(),
                child: Text('Update battery'),
              )
            ],
          ),
        ),
      ),
    );
  }
}
