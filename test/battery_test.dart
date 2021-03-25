import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:battery/battery.dart';

void main() {
  const MethodChannel channel = MethodChannel('battery');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return 42;
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getBatteryLevel', () async {
    final battery = Battery(onBatteryChange: (value) {});
    expect(await battery.getBatteryLevel(), 42);
  });
}
