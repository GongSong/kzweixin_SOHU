package com.kuaizhan.service;

import com.kuaizhan.exception.common.KZPicUploadException;

import java.io.File;

/**
 * Created by lorin on 17-3-30.
 */
public interface KZPicService {

        File download(String url, int timeout);

        File download(String url);

        String uploadByPathAndUserId(String path, long userId) throws KZPicUploadException;

        String uploadByUrlAndUserId(String url, long userId) throws KZPicUploadException;

}
