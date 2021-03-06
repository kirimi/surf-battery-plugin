package ru.kirimi.battery.battery

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import androidx.annotation.NonNull

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar

/** BatteryPlugin */
class BatteryPlugin: FlutterPlugin, MethodCallHandler, ActivityAware {
  private lateinit var channel : MethodChannel

  private lateinit var binding : ActivityPluginBinding

  /// Ресивер, который вызывается при изменении уровня батареи Intent.ACTION_BATTERY_CHANGED
  private  var  batReceiver : BroadcastReceiver = object : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
      val  batteryLevel : Int = getBatteryLevel();
      channel.invokeMethod("change_battery", batteryLevel);
    }
  }

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "battery")
    channel.setMethodCallHandler(this)
  }

  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    if (call.method == "get_battery") {
      val  batteryLevel : Int = getBatteryLevel();
      result.success(batteryLevel)
    } else {
      result.notImplemented()
    }
  }

  /// получить уровень заряда батареи в %
  private fun getBatteryLevel() : Int {
    val ifilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
    val batteryStatus: Intent? = binding.activity.applicationContext.registerReceiver(null, ifilter)

    val level: Int? = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
    val scale: Int? = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1)

    if (level != null && scale != null) {
      return (level.toFloat() / scale.toFloat() * 100).toInt()
    }
    return 0;
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }

  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    this.binding = binding
    binding.activity.registerReceiver(this.batReceiver,
            IntentFilter(Intent.ACTION_BATTERY_CHANGED));
  }

  override fun onDetachedFromActivity() {
    TODO("Not yet implemented")
  }

  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    TODO("Not yet implemented")
  }

  override fun onDetachedFromActivityForConfigChanges() {
    TODO("Not yet implemented")
  }
}
