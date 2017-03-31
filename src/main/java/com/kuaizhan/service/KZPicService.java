package com.kuaizhan.service;

import com.kuaizhan.exception.business.KZPicUploadException;
import com.kuaizhan.utils.HttpClientUtil;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lorin on 17-3-30.
 */
public interface KZPicService {

        File download(String url, int timeout);

        File download(String url);

        String uploadByPathAndUserId(String path, long userId) throws KZPicUploadException;

        String uploadByUrlAndUserId(String url, long userId) throws KZPicUploadException;

}
