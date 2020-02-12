package com.iflytek.test.demo.dto;

import java.io.Serializable;

interface IResponse<T> extends Serializable {
    Class<T> clazz();
}