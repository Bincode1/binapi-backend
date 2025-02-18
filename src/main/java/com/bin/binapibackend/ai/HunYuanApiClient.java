package com.bin.binapibackend.ai;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.bin.binapiclientsdk.uitls.SignUtils;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.bin.binapiclientsdk.uitls.SignUtils.getSign;

public class HunYuanApiClient {

    private static final String GATEWAY_HOST = "http://localhost:8090";

    private static final String API_URL = "https://api.hunyuan.cloud.tencent.com";

    private static final String HUNYUAN_API_KEY = "sk-Gfoh5otaHEmgPST28dJ5rjIZZmaN0luhOvHvpe4nagX1x1Al"; // 替换为你的 API Key
    private String accessKey;
    private String secretKey;

    public HunYuanApiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    private Map<String, String> getHeaderMap() {
        Map<String, String> headerMap = new HashMap<>();
        String nonce = RandomUtil.randomNumbers(4);
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        String clientSign = SignUtils.generateSignature(accessKey, secretKey, nonce
                , timestamp);
        headerMap.put("accessKey", accessKey);
        //一定不可以发给后端
        headerMap.put("nonce", nonce);
        headerMap.put("timestamp", timestamp);
        headerMap.put("sign", clientSign);
        headerMap.put("Authorization", "Bearer " + HUNYUAN_API_KEY); // 将 API Key 加入请求头
        return headerMap;
    }

    public String callClient(String userMessage) {
        Map<String, String> headers = getHeaderMap();
        // 构建请求体
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", "hunyuan-turbo");

        // 系统消息


        // 用户消息
        JSONObject userMessageJson = new JSONObject();
        userMessageJson.put("role", "user");
        userMessageJson.put("content", userMessage);
        requestBody.put("messages", new JSONObject[]{userMessageJson});
        requestBody.put("enable_enhancement", true);

        // 发送 POST 请求
        HttpResponse response = HttpRequest.post(GATEWAY_HOST + "/v1/chat/completions")
                .header("Content-Type", "application/json; charset=UTF-8")
                .addHeaders(headers)
                .body(requestBody.toString(), "application/json; charset=UTF-8") // 强制转为UTF-8字节
                .timeout(50000) // 设置超时时间（可选）
                .execute();

        // 处理响应
        if (response.isOk()) {
            String result = response.body();
            // 解析 JSON 字符串
            JSONObject jsonResponse = JSONUtil.parseObj(result);

            // 提取 content 字段
            String content = jsonResponse
                    .getJSONArray("choices")        // 获取 choices 数组
                    .getJSONObject(0)               // 获取第一个元素（索引为 0）
                    .getJSONObject("message")       // 获取 message 对象
                    .getStr("content");             // 获取 content 字段的值

            System.out.println("AI 回复内容: " + content);
            return content;
        } else {
            System.err.println("Request failed. Status code: " + response.getStatus());
            System.err.println("Error message: " + response.body());
        }
        return "调用失败";
    }
}