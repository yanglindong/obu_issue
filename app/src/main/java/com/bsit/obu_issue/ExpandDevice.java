package com.bsit.obu_issue;

import android.bluetooth.BluetoothDevice;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

public class ExpandDevice implements Serializable, Comparable<ExpandDevice> {
  private String macAddress;
  private String deviceName;
  private String originalDeviceId;//原始设备ID,做充电宝连接蓝牙用
  private String deviceId;
  private String operId;
  private int enery;
  private String version;
  private int versionInt;
  private int steps;
  private int duration;
  private int status;
  private int rssi;
  private boolean isConnect;
  private String broadcastMac;

  public ExpandDevice() {
    super();
  }

  public ExpandDevice(BluetoothDevice device, int rssi, byte[] scanData) {
    super();
    if (device != null) {
      this.macAddress = device.getAddress();
      this.deviceName = device.getName();
    }
    parseDate(scanData);
    this.rssi = rssi;
  }

  /**
   * 解析设备名称
   * @param data
   * @return
   */
  public  void parseDate(byte[] data) {
    String name = null;
    for (int index = 0; index < data.length; index++) {
      int fieldLength = data[index];
      if (fieldLength == 0)
        break;
      index++;
      int fieldName = data[index];

      if ((fieldName == 9) || (fieldName == 8)) {
        name = decodeLocalName(data, index + 1, fieldLength - 1);
        index += (fieldLength - 1);
        continue;
      }

      if (fieldName == -1) {
        decodeDeviceId(data, index + 1, fieldLength - 1);
        index += (fieldLength - 1);
        continue;
      }
      index += (fieldLength - 1);
    }
    this.deviceName=name;
  }

  public String decodeLocalName(byte[] data, int start, int length) {
    try {
      return new String(data, start, length, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      return null;
    } catch (IndexOutOfBoundsException ignored) {
    }
    return null;
  }


  /**
   * 解析设备ID
   * @param data
   * @return
   */
  public  void decodeDeviceId(byte[] data, int start, int length) {
    String date1 = Integer.toHexString(data[start] & 0XFF);
    String date2 = Integer.toHexString(data[start + 1] & 0XFF);
    String deviceId = (date2.length() > 1 ? date2 : "0" + date2) + (date1.length() > 1 ? date1 : "0" + date1);
    String macDevice = "";
    for (int i = 2; i < length; i++) {
      String mac1 = Integer.toHexString(data[start + i] & 0XFF).toUpperCase();
      macDevice += (mac1.length() > 1 ? mac1 : "0" + mac1);
    }
    this.deviceId=deviceId;
    this.broadcastMac=macDevice;
  }

  public String getOriginalDeviceId() {
    return originalDeviceId;
  }

  public void setOriginalDeviceId(String originalDeviceId) {
    this.originalDeviceId = originalDeviceId;
  }

  public String getMacAddress() {
    return macAddress;
  }

  public void setMacAddress(String macAddress) {
    this.macAddress = macAddress;
  }

  public String getDeviceName() {
    return deviceName;
  }

  public void setDeviceName(String deviceName) {
    this.deviceName = deviceName;
  }

  public String getDeviceId() {
    return deviceId;
  }

  public void setDeviceId(String deviceId) {
    this.deviceId = deviceId;
  }

  public int getEnery() {
    return enery;
  }

  public void setEnery(int enery) {
    this.enery = enery;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public int getSteps() {
    return steps;
  }

  public void setSteps(int steps) {
    this.steps = steps;
  }

  public int getDuration() {
    return duration;
  }

  public void setDuration(int duration) {
    this.duration = duration;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public int getRssi() {
    return rssi;
  }

  public void setRssi(int rssi) {
    this.rssi = rssi;
  }

  public boolean isConnect() {
    return isConnect;
  }

  public void setIsConnect(boolean isConnect) {
    this.isConnect = isConnect;
  }

  public int getVersionInt() {
    return versionInt;
  }

  public void setVersionInt(int versionInt) {
    this.versionInt = versionInt;
  }

  public String getOperId() {
    return operId;
  }

  public void setOperId(String operId) {
    this.operId = operId;
  }

  public String getBroadcastMac() {
    return broadcastMac;
  }

  public void setBroadcastMac(String broadcastMac) {
    this.broadcastMac = broadcastMac;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof ExpandDevice))
      return false;
    ExpandDevice device = (ExpandDevice) o;
    return device.getMacAddress().equals(getMacAddress());
  }

  @Override
  public int compareTo(@NonNull ExpandDevice another) {
    // TODO Auto-generated method stub
    if (this.rssi > another.rssi) {
      return -1;
    }
    return 1;
  }

}

