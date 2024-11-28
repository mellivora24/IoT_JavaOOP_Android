package com.javaoop.smarthome;

import android.content.Context;

import com.javaoop.smarthome.R;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class JsonReader {

    public static void readJson(Context context) {
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
                Object deviceData = device.get("deviceData"); // Có thể là boolean hoặc số

                // In thông tin ra log (hoặc lưu vào biến tùy ý)
                System.out.println("Device ID: " + id);
                System.out.println("Port: " + port);
                System.out.println("Device Type: " + deviceType);
                System.out.println("Device Name: " + deviceName);
                System.out.println("Device Data: " + deviceData);
                //addevice
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
