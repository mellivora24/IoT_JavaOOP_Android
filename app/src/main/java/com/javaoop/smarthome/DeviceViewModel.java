package com.javaoop.smarthome;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
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

    public void fetchDataFromAPI(Context context) {
        Users user = UserSingleton.getInstance().getUser();
        String uid = user.getUid();
        // Tạo hàng đợi Volley
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String URL = "http://192.168.0.103:8080/users/" + uid + "/devices";

        // Gửi yêu cầu GET
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Kiểm tra trạng thái API trả về
                            boolean success = response.getBoolean("success");
                            String message = response.getString("message");

                            Log.d(TAG, "Success: " + success);
                            Log.d(TAG, "Message: " + message);

                            // Lấy mảng "data"
                            JSONArray dataArray = response.getJSONArray("data");

                            // Lặp qua từng phần tử trong mảng
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject device = dataArray.getJSONObject(i);

                                // Lấy thông tin từ JSON
                                String deviceID = device.getString("deviceID");
                                String deviceType = device.getString("deviceType");
                                String deviceData = device.getString("deviceData");
                                String devicePort = device.optString("devicePort", "N/A");
                                String deviceName = device.optString("deviceName", "Unnamed");

                                // Tạo đối tượng Device
                                Device device1 = new Device(String.valueOf(i+1),devicePort,deviceID,deviceData,deviceType,deviceName);

                                // Thêm vào danh sách
                                addDevice(device1);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e(TAG, "JSON Parsing Error: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Volley Error: " + error.getMessage());
                    }
                }
        );
        // Thêm yêu cầu vào hàng đợi
        requestQueue.add(jsonObjectRequest);
    }

    public void removeAllDevices() {
        devices.setValue(new ArrayList<>());
    }

    public void addNewDevice(Device device, Context context) {
        Users user = UserSingleton.getInstance().getUser();
        String uid = user.getUid();
        String url = "http://192.168.0.103:8080/users/" + uid + "/devices"; // Thay đổi URL theo API của bạn

        // Tạo JSON object cho dữ liệu thiết bị
        JSONObject deviceData = new JSONObject();
        try {

            deviceData.put("id", device.getId());
            deviceData.put("port", device.getPort());
            deviceData.put("deviceID",device.getDeviceId());
            deviceData.put("deviceName", device.getDeviceName());
            deviceData.put("deviceType", device.getDeviceType());
            deviceData.put("deviceData", device.getDeviceType().equals("digitalDevice")? "false" : "0");

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
                });

        // Thêm yêu cầu vào hàng đợi
        requestQueue.add(jsonObjectRequest);
    }

    public void deleteDevice(Device device, Context context) {
        Users user = UserSingleton.getInstance().getUser();
        String uid = user.getUid();
        // Tạo URL với deviceID
        String deleteUrl = "http://192.168.0.103:8080/users/" + uid + "/devices/" + device.getDeviceId();
        // Tạo hàng đợi Volley
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        // Gửi yêu cầu DELETE
        JsonObjectRequest deleteRequest = new JsonObjectRequest(
                Request.Method.DELETE,
                deleteUrl,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Xử lý phản hồi từ API
                            boolean success = response.getBoolean("success");
                            String message = response.getString("message");

                            if (success) {
                                Log.d("DeleteDevice", "Success: " + message);
                                // Có thể cập nhật UI hoặc danh sách sau khi xóa thành công
                                Toast.makeText(context, "Xóa thiết bị thành công.", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.e("DeleteDevice", "Failed: " + message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("DeleteDevice", "JSON Parsing Error: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("DeleteDevice", "Volley Error: " + error.getMessage());
                    }
                }
        );

        // Thêm yêu cầu vào hàng đợi
        requestQueue.add(deleteRequest);
    }

    public void updateDeviceInfo(Device device,Context context) {
        Users user = UserSingleton.getInstance().getUser();
        String uid = user.getUid();
        String url = "http://192.168.0.103:8080/users/" + uid + "/devices/" + device.getDeviceId(); // Thay đổi URL theo API của bạn

        // Tạo JSON object cho dữ liệu thiết bị
        JSONObject deviceData = new JSONObject();
        try {
            deviceData.put("deviceID", device.getDeviceId()); // ID của thiết bị cần cập nhật
            deviceData.put("deviceName", device.getDeviceName());
            deviceData.put("deviceType", device.getDeviceType());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Gọi API
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.PUT, url, deviceData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Xử lý phản hồi từ API
                        Toast.makeText(context, "Cập nhật thiết bị thành công!", Toast.LENGTH_SHORT).show();
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
