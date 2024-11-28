package com.javaoop.smarthome;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class DeviceViewModel extends ViewModel {
    private final MutableLiveData<List<Device>> devices = new MutableLiveData<>(new ArrayList<>());

    public LiveData<List<Device>> getDevices() {
        return devices;
    }

    public MutableLiveData<List<Device>> getMutableDevices() {
        return devices;
    }

    public void addDevice(Device device) {
        List<Device> currentDevices = new ArrayList<>(devices.getValue());
        currentDevices.add(device);
        devices.setValue(currentDevices);
    }

    public void removeDevice(Device device) {
        List<Device> currentDevices = new ArrayList<>(devices.getValue());
        currentDevices.remove(device);
        devices.setValue(currentDevices);
    }

    public void setDevices(List<Device> updatedDevices) {
        devices.setValue(updatedDevices);
    }

    public void readJson(Context context) {
        try {
            // Đọc file từ res/raw
            InputStream inputStream = context.getResources().openRawResource(R.raw.sample);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();

            // Chuyển đổi dữ liệu thành chuỗi JSON
            String json = new String(buffer, StandardCharsets.UTF_8);

            // Parse chuỗi JSON
            JSONObject jsonObject = new JSONObject(json);

            // Lấy dữ liệu từ các trường trong JSON
            String status = jsonObject.getString("status");
            String message = jsonObject.getString("message");

            // Lấy mảng "data"
            JSONArray dataArray = jsonObject.getJSONArray("data");

            // Lặp qua từng phần tử trong mảng
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject device = dataArray.getJSONObject(i);
                String id = device.getString("id");
                int port = device.getInt("port");
                String deviceType = device.getString("deviceType");
                String deviceName = device.getString("deviceName");
                String deviceData = device.getString("deviceData"); // Có thể là boolean hoặc số

                // In thông tin ra log (hoặc lưu vào biến tùy ý)
                Device device1 = new Device(String.valueOf(i+1), String.valueOf(port), id, deviceData, deviceType, deviceName );
                addDevice(device1);
                //addevice
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeAllDevices() {
        devices.setValue(new ArrayList<>());
    }

    public void addNewDevice(Device device, Context context) {

        String url = "https://api.example.com/add_device"; // Thay đổi URL theo API của bạn

        // Tạo JSON object cho dữ liệu thiết bị
        JSONObject deviceData = new JSONObject();
        try {

            deviceData.put("id", device.getId());
            deviceData.put("port", device.getPort());
            deviceData.put("deviceID",device.getDeviceId());
            deviceData.put("deviceName", device.getDeviceName());
            deviceData.put("deviceType", device.getDeviceType());
            deviceData.put("deviceData", device.getDeviceType().equals("digitalDevice")? "0" : "false");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Gọi API
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, url, deviceData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Xử lý phản hồi từ API
                        Toast.makeText(context, "Thêm thiết bị thành công!", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Xử lý lỗi
                        Toast.makeText(context, "Lỗi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Thêm yêu cầu vào hàng đợi
        requestQueue.add(jsonObjectRequest);
    }
}
