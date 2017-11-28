package com.example.administrator.myuser;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class HttpUtil {

    public static String submitByPost(String postUrl, HashMap<String, Object> valueMap) {
        if (valueMap.size() > 0) {
            String path = postUrl;
            try {
                URL url = new URL(path);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(5000);
                connection.setRequestMethod("POST");
                //数据准备
                String data = "";
                for (String key : valueMap.keySet()) {
                    String str = valueMap.get(key).toString();
                    data += "&" + key + "=" + str;
                }
                data = data.replace("&", "");

                //至少要设置的两个请求头
//                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setRequestProperty("Content-Length", String.valueOf(data.length()));
                connection.setRequestProperty("Content-Type", "multipart/form-data; boundary= + NetUtil.BOUNDARY");

                //post的方式提交实际上是留的方式提交给服务器
                connection.setDoOutput(true);
                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(data.getBytes());

                //获得结果码
                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    //请求成功
                    InputStream is = connection.getInputStream();
                    return dealResponseResult(is);
                } else {
                    //请求失败
                    return "error";
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "error";
        }
        return "value null";
    }

    public static String submitByGet(String getUrl) {
        //get的方式提交就是url拼接的方式
        String path = getUrl;
        try {
            URL url = new URL(path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            //获得结果码
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                //请求成功 获得返回的流
                InputStream is = connection.getInputStream();
                return dealResponseResult(is);
            } else {
                //请求失败
                return "error";
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "value null";
    }

    //处理服务器的响应结果（将输入流转化成字符串）
    public static String dealResponseResult(InputStream inputStream) {
        String resultData = null;      //存储处理结果
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len = 0;
        try {
            while ((len = inputStream.read(data)) != -1) {
                byteArrayOutputStream.write(data, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            resultData = new String(byteArrayOutputStream.toByteArray(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return resultData;
    }

    //利用post模拟form表单提交头，提交文件
    public static String uploadFile(String uploadUrl, HashMap<String, Object> valueMap) {
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "******";
        try {
            URL url = new URL(uploadUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            DataOutputStream dos = new DataOutputStream(httpURLConnection.getOutputStream());


            for (String key : valueMap.keySet()) {
                String value = valueMap.get(key).toString();
                if (key.indexOf("file") > -1) {
                    if (new File(value).exists()) {
                        dos.writeBytes(twoHyphens + boundary + end);
                        dos.writeBytes("Content-Disposition: form-data; name=\"image_file\"; filename=\"test.jpg\"" + end);//提交文件
                        dos.writeBytes(end);
                        // 文件通过输入流读到代码中
                        FileInputStream fis = new FileInputStream(value);
                        byte[] buffer = new byte[8192]; // 8KB
                        int count = 0;
                        while ((count = fis.read(buffer)) != -1) {
                            dos.write(buffer, 0, count);
                        }
                        fis.close();
                        dos.writeBytes(end);
                    } else {
                        break;
                    }
                } else {
                    dos.writeBytes(twoHyphens + boundary + end);
                    dos.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"" + end);//提交value
                    dos.writeBytes(end);
                    dos.write(value.getBytes());
                    dos.writeBytes(end);
                }
            }

//            dos.writeBytes(twoHyphens + boundary + end);
//            dos.writeBytes("Content-Disposition: form-data; name=\"login_name\"" + end);
//            dos.writeBytes(end);
//            dos.write(login_name.getBytes());
//            dos.writeBytes(end);
//
//
//            dos.writeBytes(twoHyphens + boundary + end);
//            dos.writeBytes("Content-Disposition: form-data; name=\"login_password\"" + end);
//            dos.writeBytes(end);
//            dos.write(login_password.getBytes());
//            dos.writeBytes(end);
//
//
//            dos.writeBytes(twoHyphens + boundary + end);
//            dos.writeBytes("Content-Disposition: form-data; name=\"image_file\"; filename=\"test.jpg\"" + end);
//            dos.writeBytes(end);
//            // 文件通过输入流读到代码中
//            FileInputStream fis = new FileInputStream(uploadFilePath);
//            byte[] buffer = new byte[8192]; // 8KB
//            int count = 0;
//            while ((count = fis.read(buffer)) != -1) {
//                dos.write(buffer, 0, count);
//            }
//            fis.close();
//            dos.writeBytes(end);


            dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
            dos.flush();
//            MainActivity.logStr("dos: "+dos.g);
            dos.close();
            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == 200) {
                //请求成功 获得返回的流
                InputStream is = httpURLConnection.getInputStream();
                return dealResponseResult(is);
            } else {
                //请求失败
                return "error";
            }
        } catch (Exception e) {
            e.printStackTrace();
            MainActivity.logStr(e.getMessage());
        }
        return "ERROR";
    }

}
